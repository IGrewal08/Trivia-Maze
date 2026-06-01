package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A trivia question with a fixed list of options where exactly one option is
 * the correct answer. The correct answer is stored as the full text of one of
 * the options (not as a letter label like "A"/"B") so the question is
 * self-contained, robust to option reordering, and readable directly from
 * the SQLite row without joining a separate options table to resolve labels.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class MultipleChoiceQuestion extends Question {

    private static final long serialVersionUID = 1L;

    /** The correct answer; matches one entry in {@link #myOptions} exactly. */
    private final String myAnswer;

    /** The choices presented to the player, in display order. */
    private final List<String> myOptions;

    /**
     * Creates a new MultipleChoiceQuestion. The given options list is copied
     * defensively, so later mutation of the caller's list does not affect
     * this question.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theAnswer the correct answer (must equal one element of theOptions)
     * @param theOptions the answer choices in display order (at least 2 entries)
     * @throws IllegalArgumentException if theAnswer is null or blank,
     *         theOptions is null or has fewer than 2 entries, or theAnswer
     *         is not contained in theOptions
     */
    public MultipleChoiceQuestion(final int theId, final String theQuestion,
                                  final int theDifficulty, final String theAnswer,
                                  final List<String> theOptions) {
        this(theId, theQuestion, theDifficulty, theAnswer, theOptions, null);
    }

    /**
     * Creates a new MultipleChoiceQuestion with hint text.
     *
     * @param theId unique identifier for this question
     * @param theQuestion prompt text shown to the player
     * @param theDifficulty difficulty rating (must be non-negative)
     * @param theAnswer the correct answer (must equal one element of theOptions)
     * @param theOptions the answer choices in display order (at least 2 entries)
     * @param theHint hint text shown to the player
     * @throws IllegalArgumentException if theAnswer is null or blank,
     *         theOptions is null or has fewer than 2 entries, or theAnswer
     *         is not contained in theOptions
     */
    public MultipleChoiceQuestion(final int theId, final String theQuestion,
                                  final int theDifficulty, final String theAnswer,
                                  final List<String> theOptions, final String theHint) {
        super(theId, theQuestion, theDifficulty, theHint);
        if (theAnswer == null || theAnswer.isBlank()) {
            throw new IllegalArgumentException("Answer must not be null or blank.");
        }
        if (theOptions == null || theOptions.size() < 2) {
            throw new IllegalArgumentException("Options must contain at least 2 entries.");
        }
        if (!theOptions.contains(theAnswer)) {
            throw new IllegalArgumentException("Answer must be one of the provided options.");
        }
        myAnswer = theAnswer;
        myOptions = new ArrayList<>(theOptions);
    }

    /**
     * Checks the player's answer against the correct option. Behavior:
     * <ul>
     *   <li>Returns {@code false} if theAnswer is null.</li>
     *   <li>Returns {@code true} if theAnswer equals {@link Question#SKIP},
     *       since a skip token is a free pass that unlocks the door.</li>
     *   <li>Otherwise compares theAnswer to the correct option using
     *       case-insensitive trimmed equality.</li>
     * </ul>
     *
     * @param theAnswer the player's submitted answer
     * @return {@code true} if theAnswer is SKIP or matches the correct option
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
        return myAnswer.equalsIgnoreCase(theAnswer.trim());
    }

    /**
     * @return {@link QuestionType#MULTIPLE_CHOICE}
     */
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    /**
     * Returns an unmodifiable view of this question's options in display
     * order. Callers cannot mutate the underlying list.
     *
     * @return unmodifiable view of the answer choices
     */
    public List<String> getOptions() {
        return Collections.unmodifiableList(myOptions);
    }
}
