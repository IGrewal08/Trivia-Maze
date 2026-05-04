package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrueFalseQuestionTest {

    private TrueFalseQuestion question;

    @BeforeEach
    void setUp() {
        question = new TrueFalseQuestion(
                "q2",
                "The sky is blue.",
                1,
                true
        );
    }

    @Test
    void testCheckAnswerTrue() {
        // TODO: assertTrue(question.checkAnswer("true"));
    }

    @Test
    void testCheckAnswerFalse() {
        // TODO: assertFalse(question.checkAnswer("false"));
    }

    @Test
    void testCheckAnswerCaseInsensitive() {
        // TODO: verify case-insensitive matching
    }

    @Test
    void testCheckAnswerInvalidInput() {
        // TODO: handle non-boolean input
    }

    @Test
    void testCheckAnswerNullInput() {
        // TODO: null handling
    }
}