package model;

import java.util.List;

public class MultipleChoiceQuestion extends Question {

    private List<String> myOptions;
    private String myCorrectAnswer;

    public MultipleChoiceQuestion(int theId, String theQuestion, int theDifficulty,
                                  List<String> theOptions, String theCorrectAnswer) {
        super(); // TODO: update when base constructor is implemented
        this.myOptions = theOptions;
        this.myCorrectAnswer = theCorrectAnswer;
    }

    @Override
    public boolean checkAnswer(String theAnswer) {
        // TODO: implement exact match logic
        if (theAnswer == null) {
            return false;
        }
        return theAnswer.equals(myCorrectAnswer);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    // TODO: getters for options and correct answer
}