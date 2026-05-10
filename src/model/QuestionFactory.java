package model;

import java.sql.ResultSet;
import java.util.List;

public class QuestionFactory {
    
    public static Question buildQuestion(final int theId) {
        return null;
    }

    public static List<Question> getAllQuestions() {
        return null;
    }

    public static List<Question> getAllQuestionsByCategory(final String theCategory) {
        return null;
    }

    public static List<Question> getAllQuestionsByType(final QuestionType theType) {
        return null;
    }

    public static Question getRandomQuestion() {
        return null;
    }

    public static Question getRandomQuestionsByCategory(final String theCategory) {
        return null;
    }

    public static Question getRandomQuestionsByType(final QuestionType theType) {
        return null;
    }

    private static Question sqlRowToQuestion(final ResultSet theResults) {
        return null;
    }
}
