package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShortAnswerQuestionTest {

    private ShortAnswerQuestion question;

    @BeforeEach
    void setUp() {
        question = new ShortAnswerQuestion(
                "q3",
                "Name a primary color.",
                1,
                "red"
        );
    }

    @Test
    void testCheckAnswerExactMatch() {
        // TODO: assertTrue(question.checkAnswer("red"));
    }

    @Test
    void testCheckAnswerCaseInsensitive() {
        // TODO: assertTrue(question.checkAnswer("RED"));
    }

    @Test
    void testCheckAnswerTrimWhitespace() {
        // TODO: assertTrue(question.checkAnswer(" red "));
    }

    @Test
    void testCheckAnswerIncorrect() {
        // TODO: assertFalse(question.checkAnswer("blue"));
    }

    @Test
    void testCheckAnswerNullInput() {
        // TODO: null handling
    }
}