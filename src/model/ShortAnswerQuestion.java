package model;

public class ShortAnswerQuestion extends Question {

    private String myCorrectAnswer;

    public ShortAnswerQuestion(int theId, String theQuestion, int theDifficulty,
                               String theCorrectAnswer) {
        super(); // TODO: update base constructor
        this.myCorrectAnswer = theCorrectAnswer;
    }

    @Override
    public boolean checkAnswer(String theAnswer) {
        // TODO: implement case-insensitive + trim logic
        if (theAnswer == null) {
            return false;
        }

        return theAnswer.trim().equalsIgnoreCase(myCorrectAnswer);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }
}