package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for {@link TrueFalseQuestion}. Covers flexible boolean
 * parsing accepted by the constructor (true/false, t/f, yes/no, y/n —
 * case-insensitive, trimmed), the same flexibility in
 * {@link TrueFalseQuestion#checkAnswer(String)}, SKIP semantics, and the
 * canonical form returned by {@link TrueFalseQuestion#getCorrectAnswer()}.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class TrueFalseQuestionTest {

    private TrueFalseQuestion myTrueQuestion;

    @BeforeEach
    void setUp() {
        myTrueQuestion = new TrueFalseQuestion(1, "The sky is blue.", 1, "true");
    }

    @Test
    void testConstructorAcceptsTrueString() {
        final TrueFalseQuestion q = new TrueFalseQuestion(2, "Q?", 1, "true");
        assertEquals("true", q.getCorrectAnswer());
    }

    @Test
    void testConstructorAcceptsFalseString() {
        final TrueFalseQuestion q = new TrueFalseQuestion(3, "Q?", 1, "false");
        assertEquals("false", q.getCorrectAnswer());
    }

    @Test
    void testConstructorAcceptsYesNo() {
        assertEquals("true",
            new TrueFalseQuestion(4, "Q?", 1, "yes").getCorrectAnswer());
        assertEquals("false",
            new TrueFalseQuestion(5, "Q?", 1, "no").getCorrectAnswer());
    }

    @Test
    void testConstructorAcceptsTF() {
        assertEquals("true",
            new TrueFalseQuestion(6, "Q?", 1, "T").getCorrectAnswer());
        assertEquals("false",
            new TrueFalseQuestion(7, "Q?", 1, "f").getCorrectAnswer());
    }

    @Test
    void testConstructorRejectsUnparseable() {
        assertThrows(IllegalArgumentException.class,
            () -> new TrueFalseQuestion(8, "Q?", 1, "maybe"));
    }

    @Test
    void testConstructorRejectsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new TrueFalseQuestion(9, "Q?", 1, null));
    }

    @Test
    void testCheckAnswerCorrect() {
        assertTrue(myTrueQuestion.checkAnswer("true"));
    }

    @Test
    void testCheckAnswerFlexibleParsing() {
        assertTrue(myTrueQuestion.checkAnswer("y"));
        assertTrue(myTrueQuestion.checkAnswer("YES"));
        assertTrue(myTrueQuestion.checkAnswer(" T "));
    }

    @Test
    void testCheckAnswerIncorrect() {
        assertFalse(myTrueQuestion.checkAnswer("false"));
        assertFalse(myTrueQuestion.checkAnswer("n"));
    }

    @Test
    void testCheckAnswerNullReturnsFalse() {
        assertFalse(myTrueQuestion.checkAnswer(null));
    }

    @Test
    void testCheckAnswerSkipReturnsTrue() {
        assertTrue(myTrueQuestion.checkAnswer(Question.SKIP));
    }

    @Test
    void testGetQuestionType() {
        assertEquals(QuestionType.TRUE_FALSE, myTrueQuestion.getQuestionType());
    }

    @Test
    void testGetCorrectAnswerReturnsCanonical() {
        assertEquals("true",
            new TrueFalseQuestion(10, "Q?", 1, "Yes").getCorrectAnswer());
        assertEquals("false",
            new TrueFalseQuestion(11, "Q?", 1, "N").getCorrectAnswer());
    }
}
