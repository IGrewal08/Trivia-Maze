package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleChoiceQuestionTest {

    private MultipleChoiceQuestion question;

    @BeforeEach
    void setUp() {
        List<String> options = Arrays.asList("A", "B", "C", "D");
        question = new MultipleChoiceQuestion(
                "q1",
                "What is the correct answer?",
                1,
                options,
                "A"
        );
    }

    @Test
    void testCheckAnswerCorrect() {
        // TODO: verify correct answer returns true
        // assertTrue(question.checkAnswer("A"));
    }

    @Test
    void testCheckAnswerIncorrect() {
        // TODO: verify incorrect answer returns false
        // assertFalse(question.checkAnswer("B"));
    }

    @Test
    void testCheckAnswerCaseSensitivity() {
        // TODO: determine if MC should be case-sensitive
    }

    @Test
    void testCheckAnswerInvalidOption() {
        // TODO: handle answers not in options list
    }

    @Test
    void testCheckAnswerNullInput() {
        // TODO: verify behavior for null input
    }
}