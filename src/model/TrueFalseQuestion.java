package model;

import java.util.Locale;

/**
 * A trivia question with a boolean correct answer. The answer is stored
 * internally as a canonical String ("true" or "false") to match the team's
 * UML, but both the constructor and {@link #checkAnswer(String)} accept the
 * common variations players (and authors of the question bank) are likely
 * to type: "true"/"false", "t"/"f", "yes"/"no", and "y"/"n", all
 * case-insensitive and trimmed.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class TrueFalseQuestion extends Question {

    private static final long serialVersionUID = 1L;

    /** Canonical correct answer, always exactly "true" or "false". */
    private final String myAnswer;

    /**
     * Creates a new TrueFalseQuestion. The given answer is validated and
     * normalized to the canonical form "true" or "false" before storage.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theAnswer the correct answer; accepts "true"/"false", "t"/"f",
     *        "yes"/"no", "y"/"n" (case-insensitive, trimmed)
     * @throws IllegalArgumentException if theAnswer is null or cannot be
     *         parsed as a boolean
     */
    public TrueFalseQuestion(final int theId, final String theQuestion,
                             final int theDifficulty, final String theAnswer) {
        this(theId, theQuestion, theDifficulty, theAnswer, null);
    }

    /**
     * Creates a new TrueFalseQuestion with hint text.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theAnswer the correct answer; accepts "true"/"false", "t"/"f",
     *        "yes"/"no", "y"/"n" (case-insensitive, trimmed)
     * @param theHint hint text shown to the player
     * @throws IllegalArgumentException if theAnswer is null or cannot be
     *         parsed as a boolean
     */
    public TrueFalseQuestion(final int theId, final String theQuestion,
                             final int theDifficulty, final String theAnswer,
                             final String theHint) {
        super(theId, theQuestion, theDifficulty, theHint);
        final String canonical = parseFlexibleBoolean(theAnswer);
        if (canonical == null) {
            throw new IllegalArgumentException(
                "Answer must be parseable as boolean (true/false, t/f, yes/no, y/n).");
        }
        myAnswer = canonical;
    }

    /**
     * Checks the player's answer against the correct boolean. Behavior:
     * <ul>
     *   <li>Returns {@code false} if theAnswer is null.</li>
     *   <li>Returns {@code true} if theAnswer equals {@link Question#SKIP},
     *       since a skip token is a free pass that unlocks the door.</li>
     *   <li>Otherwise parses theAnswer with the same flexible accepts list
     *       as the constructor and compares the canonical form to
     *       {@link #myAnswer}. Unparseable input returns {@code false}
     *       rather than throwing.</li>
     * </ul>
     *
     * @param theAnswer the player's submitted answer
     * @return {@code true} if theAnswer is SKIP or parses to the correct
     *         boolean, {@code false} otherwise
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {
        if (theAnswer == null) {
            return false;
        }
        if (SKIP.equals(theAnswer)) {
            return true;
        }
        final String canonical = parseFlexibleBoolean(theAnswer);
        return canonical != null && canonical.equals(myAnswer);
    }

    /**
     * @return {@link QuestionType#TRUE_FALSE}
     */
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_FALSE;
    }

    /**
     * @return the canonical correct answer string, always "true" or "false"
     */
    public String getCorrectAnswer() {
        return myAnswer;
    }

    /**
     * Parses a string flexibly into a canonical boolean form. Accepts
     * "true"/"false", "t"/"f", "yes"/"no", "y"/"n" (case-insensitive and
     * trimmed). Used both at construction (where unparseable input is an
     * error) and at answer check (where unparseable input is just wrong).
     *
     * @param theInput the raw input to parse
     * @return "true" or "false" if recognized, {@code null} if the input is
     *         null or unparseable
     */
    private static String parseFlexibleBoolean(final String theInput) {
        if (theInput == null) {
            return null;
        }
        final String normalized = theInput.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "true", "t", "yes", "y" -> "true";
            case "false", "f", "no", "n" -> "false";
            default -> null;
        };
    }
}
