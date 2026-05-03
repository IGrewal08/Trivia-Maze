package model;

import java.io.Serializable;

/**
 * TODO: class JavaDoc
 *
 * @author Anwar Noor
 * @version 1.0
 */
public abstract class Question implements Serializable {

    // TODO: serialVersionUID

    // TODO: SKIP sentinel constant (public static final String)

    /** TODO */
    private int myId;

    /** TODO */
    private String myQuestion;

    /** TODO */
    private int myDifficulty;

    /**
     * TODO constructor JavaDoc
     */
    public Question(/* params */) {
        // assign + validate
    }

    // TODO: getId, getQuestionText, getDifficulty (with JavaDocs)

    /**
     * Checks the given answer against this question's correct answer.
     * Implementation differs per question type (Strategy pattern).
     *
     * @param theAnswer the user's submitted answer
     * @return true if correct, false otherwise
     */
    public abstract boolean checkAnswer(String theAnswer);

    /**
     * @return the QuestionType of this question
     */
    public abstract QuestionType getQuestionType();
}