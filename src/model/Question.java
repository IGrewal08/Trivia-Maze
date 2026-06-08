package model;

import java.io.Serializable;

/**
 * Abstract base class for all trivia questions in the Trivia Maze.
 * A Question stores a unique id (matching the SQLite primary key), the prompt
 * text shown to the player, and an integer difficulty rating. Subclasses
 * implement {@link #checkAnswer(String)} and {@link #getQuestionType()} to
 * provide type-specific answer evaluation (Strategy pattern).
 *
 * @author Anwar Noor
 * @version 1.0
 */
public abstract class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Sentinel value submitted as the answer when the player chooses to skip
     * a question rather than attempt it. Every subclass's
     * {@link #checkAnswer(String)} treats this value as a free pass.
     */
    public static final String SKIP = "__SKIP__";

    /** Unique identifier of this question (matches the SQLite primary key). */
    private final int myId;

    /** The prompt text shown to the player. */
    private final String myQuestion;

    /** Difficulty rating of this question (higher means harder). */
    private final int myDifficulty;

    /** Hint text shown when the player asks for help. */
    private final String myHint;

    /**
     * Creates a new Question with the given id, prompt, and difficulty.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @throws IllegalArgumentException if theQuestion is null or blank,
     *         or if theDifficulty is negative
     */
    public Question(final int theId, final String theQuestion, final int theDifficulty) {
        this(theId, theQuestion, theDifficulty, null);
    }

    /**
     * Creates a new Question with the given id, prompt, difficulty, and hint.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theHint hint text shown to the player
     * @throws IllegalArgumentException if theQuestion is null or blank,
     *         or if theDifficulty is negative
     */
    public Question(final int theId, final String theQuestion,
                    final int theDifficulty, final String theHint) {
        if (theQuestion == null || theQuestion.isBlank()) {
            throw new IllegalArgumentException("Question text must not be null or blank.");
        }
        if (theDifficulty < 0) {
            throw new IllegalArgumentException("Difficulty must be non-negative.");
        }
        myId = theId;
        myQuestion = theQuestion;
        myDifficulty = theDifficulty;
        myHint = theHint == null || theHint.isBlank()
                ? "No hint available for this question."
                : theHint;
    }

    /**
     * @return the unique id of this question
     */
    public int getId() {
        return myId;
    }

    /**
     * @return the prompt text shown to the player
     */
    public String getQuestionText() {
        return myQuestion;
    }

    /**
     * @return the difficulty rating of this question
     */
    public int getDifficulty() {
        return myDifficulty;
    }

    /**
     * @return hint text suitable for display to the player
     */
    public String getHint() {
        return myHint;
    }

    /**
     * Checks the given answer against this question's correct answer.
     * Implementation differs per question type (Strategy pattern).
     *
     * @param theAnswer the user's submitted answer
     * @return true if correct, false otherwise
     */
    public abstract boolean checkAnswer(final String theAnswer);

    /**
     * @return the QuestionType of this question
     */
    public abstract QuestionType getQuestionType();
}
