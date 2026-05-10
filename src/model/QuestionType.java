package model;

/**
 * Enumeration of the kinds of trivia questions supported by the maze.
 * Each {@link Question} subclass reports its kind via
 * {@link Question#getQuestionType()}, which lets persistence and UI layers
 * branch on type without resorting to instanceof checks.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public enum QuestionType {

    /** Multiple-choice question with a fixed list of selectable options. */
    MULTIPLE_CHOICE,

    /** True/false question with a boolean answer. */
    TRUE_FALSE,

    /** Free-form short-answer question matched against expected text. */
    SHORT_ANSWER
}
