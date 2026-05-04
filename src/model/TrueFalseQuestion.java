package model;

public class TrueFalseQuestion extends Question {

    private boolean myCorrectAnswer;

    public TrueFalseQuestion(int theId, String theQuestion, int theDifficulty,
                             boolean theCorrectAnswer) {
        super(); // TODO: update base constructor
        this.myCorrectAnswer = theCorrectAnswer;
    }

    @Override
    public boolean checkAnswer(String theAnswer) {
        // TODO: implement boolean parsing logic
        if (theAnswer == null) {
            return false;
        }

        boolean parsed = Boolean.parseBoolean(theAnswer);
        return parsed == myCorrectAnswer;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_FALSE;
    }
}