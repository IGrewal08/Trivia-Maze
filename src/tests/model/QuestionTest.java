package tests.model;

import model.Question;
import model.QuestionType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * Junit 5 test for Question
 * 
 * Tests Question class's constructor, the hints and skip question systems with
 * input validations, and check answer method to validate user answers.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class QuestionTest {
    
    // Private class set-up
    private static class ConcreteQuestion extends Question {
        private final String myAnswer;

        ConcreteQuestion(int id, String question, int difficulty, String answer, String hint) {
            super(id, question, difficulty, hint);
            this.myAnswer = answer;
        }

        ConcreteQuestion(int id, String question, int difficulty, String answer) {
            super(id, question, difficulty);
            this.myAnswer = answer;
        }

        @Override
        public boolean checkAnswer(String theAnswer) {
            return myAnswer.equalsIgnoreCase(theAnswer);
        }

        @Override
        public QuestionType getQuestionType() {
            return QuestionType.SHORT_ANSWER;
        }
    }

    // ~~~ Constructor ~~~
    
    @Test
    void constructor_validArgs_storesFieldsCorrectly() {
        ConcreteQuestion q = new ConcreteQuestion(1, "What is 2+2?", 1, "4");
        assertEquals(1, q.getId());
        assertEquals("What is 2+2?", q.getQuestionText());
        assertEquals(1, q.getDifficulty());
    }

    @Test
    void constructor_nullQuestion_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new ConcreteQuestion(1, null, 1, "answer"));
    }

    @Test
    void constructor_blankQuestion_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new ConcreteQuestion(1, "   ", 1, "answer"));
    }

    @Test
    void constructor_negativeDifficulty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new ConcreteQuestion(1, "Valid?", -1, "answer"));
    }

    @Test
    void constructor_zeroDifficulty_isAllowed() {
        assertDoesNotThrow(() -> new ConcreteQuestion(1, "Valid?", 0, "answer"));
    }

    // ~~~ Hints ~~~
    @Test
    void constructor_nullHint_returnsDefaultHintMessage() {
        ConcreteQuestion q = new ConcreteQuestion(1, "Valid?", 1, "answer", null);
        assertEquals("No hint available for this question.", q.getHint());
    }

    @Test
    void constructor_blankHint_returnsDefaultHintMessage() {
        ConcreteQuestion q = new ConcreteQuestion(1, "Valid?", 1, "answer", "   ");
        assertEquals("No hint available for this question.", q.getHint());
    }

    @Test
    void constructor_validHint_storesHint() {
        ConcreteQuestion q = new ConcreteQuestion(1, "Valid?", 1, "answer", "Think carefully.");
        assertEquals("Think carefully.", q.getHint());
    }

    // ~~~ Skips ~~~

    @Test
    void skipSentinel_hasExpectedValue() {
        assertEquals("__SKIP__", Question.SKIP);
    }

    // ~~~ checkAnswer ~~~

    @Test
    void checkAnswer_correctAnswer_returnsTrue() {
        ConcreteQuestion q = new ConcreteQuestion(1, "Valid?", 1, "42");
        assertTrue(q.checkAnswer("42"));
    }

    @Test
    void checkAnswer_wrongAnswer_returnsFalse() {
        ConcreteQuestion q = new ConcreteQuestion(1, "Valid?", 1, "42");
        assertFalse(q.checkAnswer("99"));
    }
}
