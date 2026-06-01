package tests.model;

import db.DatabaseManager;
import db.Schema;
import model.MultipleChoiceQuestion;
import model.Question;
import model.QuestionFactory;
import model.QuestionType;
import model.ShortAnswerQuestion;
import model.TrueFalseQuestion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link QuestionFactory}. Boots the seeded SQLite
 * database once before all tests and verifies that the factory builds the
 * correct {@link Question} subclasses from row data, including type-aware
 * dispatch, difficulty text-to-int mapping, multiple-choice option parsing,
 * and the error contracts for empty results and unknown ids.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class QuestionFactoryTest {

    /** Total seeded questions: 7 categories x 3 types x 3 difficulties. */
    private static final int TOTAL_QUESTIONS = 63;

    /** Per-category count when evenly distributed across 7 categories. */
    private static final int PER_CATEGORY = TOTAL_QUESTIONS / 7;

    /** Per-type count when evenly distributed across 3 types. */
    private static final int PER_TYPE = TOTAL_QUESTIONS / 3;

    @BeforeAll
    static void connect() {
        Schema.initialize();
        DatabaseManager.connect();
    }

    @AfterAll
    static void disconnect() {
        DatabaseManager.disconnect();
    }

    @Test
    void buildQuestionReturnsValidQuestionForKnownId() {
        final Question q = QuestionFactory.buildQuestion(1);

        assertNotNull(q);
        assertEquals(1, q.getId());
        assertNotNull(q.getQuestionText());
        assertFalse(q.getQuestionText().isBlank());
        assertNotNull(q.getHint());
        assertFalse(q.getHint().isBlank());
        assertTrue(q.getDifficulty() >= 1 && q.getDifficulty() <= 3);
    }

    @Test
    void buildQuestionThrowsForUnknownId() {
        assertThrows(IllegalArgumentException.class,
                () -> QuestionFactory.buildQuestion(9999));
    }

    @Test
    void getAllQuestionsReturnsFullSeed() {
        final List<Question> all = QuestionFactory.getAllQuestions();

        assertEquals(TOTAL_QUESTIONS, all.size());
        all.forEach(q -> assertNotNull(q.getQuestionText()));
    }

    @Test
    void getAllQuestionsReturnsDatabaseHints() {
        QuestionFactory.getAllQuestions().forEach(q -> {
            assertNotNull(q.getHint());
            assertFalse(q.getHint().isBlank());
            assertFalse("No hint available for this question.".equals(q.getHint()));
        });
    }

    @Test
    void getAllQuestionsByCategoryReturnsExpectedCount() {
        final List<Question> science = QuestionFactory.getAllQuestionsByCategory("SCIENCE");

        assertEquals(PER_CATEGORY, science.size());
    }

    @Test
    void getAllQuestionsByTypeReturnsExpectedCount() {
        final List<Question> trueFalse = QuestionFactory.getAllQuestionsByType(QuestionType.TRUE_FALSE);

        assertEquals(PER_TYPE, trueFalse.size());
        trueFalse.forEach(q -> assertTrue(q instanceof TrueFalseQuestion,
                "expected TrueFalseQuestion, got " + q.getClass().getSimpleName()));
    }

    @Test
    void getAllQuestionsByTypeReturnsCorrectSubclassForMultipleChoice() {
        final List<Question> mc = QuestionFactory.getAllQuestionsByType(QuestionType.MULTIPLE_CHOICE);

        assertEquals(PER_TYPE, mc.size());
        mc.forEach(q -> {
            assertTrue(q instanceof MultipleChoiceQuestion);
            final MultipleChoiceQuestion casted = (MultipleChoiceQuestion) q;
            assertTrue(casted.getOptions().size() >= 2,
                    "multiple choice should have at least 2 options");
        });
    }

    @Test
    void getAllQuestionsByTypeReturnsCorrectSubclassForShortAnswer() {
        final List<Question> sa = QuestionFactory.getAllQuestionsByType(QuestionType.SHORT_ANSWER);

        assertEquals(PER_TYPE, sa.size());
        sa.forEach(q -> assertTrue(q instanceof ShortAnswerQuestion));
    }

    @Test
    void getRandomQuestionsReturnsRequestedCount() {
        final List<Question> picks = QuestionFactory.getRandomQuestions(10);

        assertEquals(10, picks.size());
        picks.forEach(q -> assertNotNull(q));
    }

    @Test
    void getRandomQuestionsRejectsNegativeCount() {
        assertThrows(IllegalArgumentException.class,
                () -> QuestionFactory.getRandomQuestions(-1));
    }

    @Test
    void getRandomQuestionsAcceptsZero() {
        final List<Question> picks = QuestionFactory.getRandomQuestions(0);
        assertTrue(picks.isEmpty());
    }

    @Test
    void getRandomQuestionByCategoryReturnsOne() {
        final Question q = QuestionFactory.getRandomQuestionByCategory("HISTORY");

        assertNotNull(q);
        assertNotNull(q.getQuestionText());
    }

    @Test
    void getRandomQuestionByCategoryThrowsForEmptyCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> QuestionFactory.getRandomQuestionByCategory("DOES_NOT_EXIST"));
    }

    @Test
    void getRandomQuestionByTypeReturnsCorrectSubclass() {
        final Question q = QuestionFactory.getRandomQuestionByType(QuestionType.MULTIPLE_CHOICE);

        assertTrue(q instanceof MultipleChoiceQuestion);
    }

    @Test
    void difficultyMapsToValidIntegerRange() {
        QuestionFactory.getAllQuestions()
                .forEach(q -> assertTrue(q.getDifficulty() >= 1 && q.getDifficulty() <= 3,
                        "difficulty must map to 1, 2, or 3 but was " + q.getDifficulty()));
    }
}
