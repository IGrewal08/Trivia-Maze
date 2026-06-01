package tests.model;

import model.MultipleChoiceQuestion;
import model.Question;
import model.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for {@link MultipleChoiceQuestion}. Covers construction
 * validation (null/blank answer, answer-not-in-options, too-few-options),
 * case-insensitive trimmed answer matching, SKIP semantics, and the
 * defensive-copy behavior of {@link MultipleChoiceQuestion#getOptions()}.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class MultipleChoiceQuestionTest {

    private MultipleChoiceQuestion myQuestion;

    @BeforeEach
    void setUp() {
        myQuestion = new MultipleChoiceQuestion(
            1,
            "Pick a primary color.",
            2,
            "red",
            Arrays.asList("red", "blue", "green", "yellow")
        );
    }

    @Test
    void testConstructorValid() {
        assertEquals(1, myQuestion.getId());
        assertEquals("Pick a primary color.", myQuestion.getQuestionText());
        assertEquals(2, myQuestion.getDifficulty());
        assertEquals(QuestionType.MULTIPLE_CHOICE, myQuestion.getQuestionType());
        assertEquals(4, myQuestion.getOptions().size());
    }

    @Test
    void testConstructorRejectsNullAnswer() {
        assertThrows(IllegalArgumentException.class,
            () -> new MultipleChoiceQuestion(2, "Q?", 1, null,
                Arrays.asList("a", "b")));
    }

    @Test
    void testConstructorRejectsAnswerNotInOptions() {
        assertThrows(IllegalArgumentException.class,
            () -> new MultipleChoiceQuestion(3, "Q?", 1, "missing",
                Arrays.asList("a", "b")));
    }

    @Test
    void testConstructorRejectsTooFewOptions() {
        assertThrows(IllegalArgumentException.class,
            () -> new MultipleChoiceQuestion(4, "Q?", 1, "a",
                Arrays.asList("a")));
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
        assertEquals(QuestionType.MULTIPLE_CHOICE, myQuestion.getQuestionType());
    }

    @Test
    void testGetHintReturnsStoredHint() {
        final MultipleChoiceQuestion question = new MultipleChoiceQuestion(
            5,
            "Pick a primary color.",
            2,
            "red",
            Arrays.asList("red", "blue", "green", "yellow"),
            "Think about basic color categories."
        );

        assertEquals("Think about basic color categories.", question.getHint());
    }

    @Test
    void testGetHintUsesFallbackWhenMissing() {
        assertEquals("No hint available for this question.", myQuestion.getHint());
    }

    @Test
    void testGetOptionsIsDefensiveCopy() {
        final List<String> returned = myQuestion.getOptions();
        assertThrows(UnsupportedOperationException.class,
            () -> returned.add("purple"));
        assertEquals(4, myQuestion.getOptions().size());
    }
}
