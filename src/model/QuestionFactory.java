package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import db.DatabaseManager;

/**
 * Static factory for constructing {@link Question} instances from rows in the
 * SQLite questions table. Talks to {@link DatabaseManager} as the data source
 * and dispatches to the appropriate {@link Question} subclass based on each
 * row's {@code type} column.
 *
 * <p>The factory consumes (and closes) every {@link ResultSet} it opens via
 * try-with-resources; callers receive plain {@link Question} objects and never
 * see JDBC types. Any underlying {@link SQLException} is wrapped as an
 * {@link IllegalStateException}; empty-result lookups by category or type
 * throw {@link IllegalArgumentException}.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public final class QuestionFactory {

    /** Shared PRNG for all random-selection methods. */
    private static final Random RNG = new Random();

    /** Maps the database's textual difficulty values to integer ratings. */
    private static final Map<String, Integer> DIFFICULTY = Map.of(
            "EASY", 1,
            "MEDIUM", 2,
            "HARD", 3);

    /** Functional interface mirroring {@link java.util.function.Supplier} but
     *  permitting checked {@link SQLException}, so DatabaseManager method
     *  references can be passed directly into {@link #readAll(SqlSupplier)}. */
    @FunctionalInterface
    private interface SqlSupplier<T> {
        T get() throws SQLException;
    }

    /** Prevent instantiation; static utility. */
    private QuestionFactory() { }

    /**
     * Returns the question with the given primary-key id.
     *
     * @param theId the question id to look up
     * @return the constructed Question
     * @throws IllegalArgumentException if no question exists with that id
     * @throws IllegalStateException if the underlying query fails
     */
    public static Question buildQuestion(final int theId) {
        try (ResultSet rs = DatabaseManager.getQuestionById(theId)) {
            if (rs == null || !rs.next()) {
                throw new IllegalArgumentException("No question with id " + theId);
            }
            return sqlRowToQuestion(rs);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load question " + theId, e);
        }
    }

    /**
     * @return every question in the database, in id order
     */
    public static List<Question> getAllQuestions() {
        return readAll(DatabaseManager::getAllQuestions);
    }

    /**
     * @param theCategory category to filter by (e.g. "SCIENCE")
     * @return every question in that category, in id order
     */
    public static List<Question> getAllQuestionsByCategory(final String theCategory) {
        return readAll(() -> DatabaseManager.getAllQuestionsByCategory(theCategory));
    }

    /**
     * @param theType question type to filter by
     * @return every question of that type, in id order
     */
    public static List<Question> getAllQuestionsByType(final QuestionType theType) {
        return readAll(() -> DatabaseManager.getAllQuestionsByType(theType));
    }

    /**
     * Returns {@code theCount} random questions sampled with replacement from
     * every question in the database. Sampling with replacement matches the
     * needs of {@code Maze.fillRoomsWithQuestions}, which may need more
     * questions than the database holds.
     *
     * @param theCount how many questions to return (must be non-negative)
     * @return a new list of {@code theCount} questions
     * @throws IllegalArgumentException if theCount is negative, or the pool is empty
     */
    public static List<Question> getRandomQuestions(final int theCount) {
        if (theCount < 0) {
            throw new IllegalArgumentException("Count must be non-negative.");
        }
        final List<Question> pool = getAllQuestions();
        if (pool.isEmpty()) {
            throw new IllegalArgumentException("No questions available.");
        }
        final List<Question> picked = new ArrayList<>(theCount);
        for (int i = 0; i < theCount; i++) {
            picked.add(pool.get(RNG.nextInt(pool.size())));
        }
        return picked;
    }

    /**
     * @param theCategory category to draw from
     * @return one random question in that category
     * @throws IllegalArgumentException if the category yields no questions
     */
    public static Question getRandomQuestionByCategory(final String theCategory) {
        try (ResultSet rs = DatabaseManager.getRandomQuestionByCategory(theCategory)) {
            if (rs == null || !rs.next()) {
                throw new IllegalArgumentException("No questions for category " + theCategory);
            }
            return sqlRowToQuestion(rs);
        } catch (SQLException e) {
            throw new IllegalStateException("DB read failed for category " + theCategory, e);
        }
    }

    /**
     * @param theType type to draw from
     * @return one random question of that type
     * @throws IllegalArgumentException if the type yields no questions
     */
    public static Question getRandomQuestionByType(final QuestionType theType) {
        try (ResultSet rs = DatabaseManager.getRandomQuestionByType(theType)) {
            if (rs == null || !rs.next()) {
                throw new IllegalArgumentException("No questions for type " + theType);
            }
            return sqlRowToQuestion(rs);
        } catch (SQLException e) {
            throw new IllegalStateException("DB read failed for type " + theType, e);
        }
    }

    /**
     * Walks a ResultSet supplied by {@code theQuery}, building a Question from
     * each row. Closes the ResultSet via try-with-resources.
     */
    private static List<Question> readAll(final SqlSupplier<ResultSet> theQuery) {
        try (ResultSet rs = theQuery.get()) {
            final List<Question> out = new ArrayList<>();
            while (rs != null && rs.next()) {
                out.add(sqlRowToQuestion(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new IllegalStateException("DB read failed", e);
        }
    }

    /**
     * Constructs the appropriate Question subclass from the current row of
     * {@code theResults}. Does not advance the cursor; the caller must call
     * {@code next()} first.
     */
    private static Question sqlRowToQuestion(final ResultSet theResults) throws SQLException {
        final int id = theResults.getInt("id");
        final String typeRaw = theResults.getString("type");
        final String text = theResults.getString("text");
        final String answer = theResults.getString("answer");
        final String difficultyRaw = theResults.getString("difficulty");

        final Integer difficulty = DIFFICULTY.get(difficultyRaw);
        if (difficulty == null) {
            throw new IllegalStateException("Unknown difficulty value: " + difficultyRaw);
        }

        final QuestionType type;
        try {
            type = QuestionType.valueOf(typeRaw);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown question type: " + typeRaw, e);
        }

        return switch (type) {
            case TRUE_FALSE -> new TrueFalseQuestion(id, text, difficulty, answer);
            case SHORT_ANSWER -> new ShortAnswerQuestion(id, text, difficulty, answer);
            case MULTIPLE_CHOICE -> new MultipleChoiceQuestion(
                    id, text, difficulty, answer, collectOptions(theResults));
        };
    }

    /**
     * Collects the non-null option_1..option_4 columns of the current row into
     * a list, preserving order.
     */
    private static List<String> collectOptions(final ResultSet theResults) throws SQLException {
        final List<String> options = new ArrayList<>(4);
        for (int i = 1; i <= 4; i++) {
            final String opt = theResults.getString("option_" + i);
            if (opt != null) {
                options.add(opt);
            }
        }
        return options;
    }
}
