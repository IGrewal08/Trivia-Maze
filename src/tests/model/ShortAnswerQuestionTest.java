package tests.model;

import model.Question;
import model.QuestionType;
import model.ShortAnswerQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for {@link ShortAnswerQuestion}. Covers construction
 * validation (null and blank/whitespace-only answers are rejected),
 * case-insensitive trimmed answer matching, and SKIP semantics.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class ShortAnswerQuestionTest {

    private ShortAnswerQuestion myQuestion;

    @BeforeEach
    void setUp() {
        myQuestion = new ShortAnswerQuestion(1, "Name a primary color.", 1, "red");
    }

    @Test
    void testConstructorValid() {
        assertEquals(1, myQuestion.getId());
        assertEquals("Name a primary color.", myQuestion.getQuestionText());
        assertEquals(1, myQuestion.getDifficulty());
        assertEquals(QuestionType.SHORT_ANSWER, myQuestion.getQuestionType());
    }

    @Test
    void testConstructorRejectsNullAnswer() {
        assertThrows(IllegalArgumentException.class,
            () -> new ShortAnswerQuestion(2, "Q?", 1, null));
    }

    @Test
    void testConstructorRejectsBlankAnswer() {
        assertThrows(IllegalArgumentException.class,
            () -> new ShortAnswerQuestion(3, "Q?", 1, "   "));
    }

    @Test
    void testCheckAnswerCorrect() {
        assertTrue(myQuestion.checkAnswer("red"));
    }

    @Test
    void testCheckAnswerCaseInsensitive() {
        assertTrue(myQuestion.checkAnswer("RED"));
        assertTrue(myQuestion.checkAnswer("Red"));
    }

    @Test
    void testCheckAnswerWithWhitespace() {
        assertTrue(myQuestion.checkAnswer("  red  "));
    }

    @Test
    void testCheckAnswerIncorrect() {
        assertFalse(myQuestion.checkAnswer("blue"));
    }

    @Test
    void testCheckAnswerNullReturnsFalse() {
        assertFalse(myQuestion.checkAnswer(null));
    }

    @Test
    void testCheckAnswerSkipReturnsTrue() {
        assertTrue(myQuestion.checkAnswer(Question.SKIP));
    }

    @Test
    void testGetQuestionType() {
        assertEquals(QuestionType.SHORT_ANSWER, myQuestion.getQuestionType());
    }

    @Test
    void testGetHintReturnsStoredHint() {
        final ShortAnswerQuestion question = new ShortAnswerQuestion(
            4,
            "Name a primary color.",
            1,
            "red",
            "Think of a basic warm color."
        );

        assertEquals("Think of a basic warm color.", question.getHint());
    }

    @Test
    void testGetHintUsesFallbackWhenMissing() {
        assertEquals("No hint available for this question.", myQuestion.getHint());
    }
}
