package model;

/**
 * A trivia question that requires a free-form short text answer. The player's
 * input is compared against the correct answer with case-insensitive trimmed
 * equality, so leading/trailing whitespace and capitalization differences are
 * tolerated.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class ShortAnswerQuestion extends Question {

    private static final long serialVersionUID = 1L;

    /** The correct short-answer text. */
    private final String myAnswer;

    /**
     * Creates a new ShortAnswerQuestion.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theAnswer the correct answer text (must not be null or blank)
     * @throws IllegalArgumentException if theAnswer is null or blank
     */
    public ShortAnswerQuestion(final int theId, final String theQuestion,
                               final int theDifficulty, final String theAnswer) {
        super(theId, theQuestion, theDifficulty);
        if (theAnswer == null || theAnswer.isBlank()) {
            throw new IllegalArgumentException("Answer must not be null or blank.");
        }
        myAnswer = theAnswer;
    }

    /**
     * Compares theAnswer against the correct answer. Behavior:
     * <ul>
     *   <li>Returns {@code false} if theAnswer is null.</li>
     *   <li>Returns {@code true} if theAnswer equals {@link Question#SKIP},
     *       since a skip token is a free pass that unlocks the door.</li>
     *   <li>Otherwise compares theAnswer to the correct answer using
     *       case-insensitive trimmed equality.</li>
     * </ul>
     *
     * @param theAnswer the player's submitted answer
     * @return {@code true} if theAnswer is SKIP or matches the correct answer
     *         ignoring case and surrounding whitespace, {@code false} otherwise
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {
        if (theAnswer == null) {
            return false;
        }
        if (SKIP.equals(theAnswer)) {
            return true;
        }
        return myAnswer.trim().equalsIgnoreCase(theAnswer.trim());
    }

    /**
     * @return {@link QuestionType#SHORT_ANSWER}
     */
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }
}
