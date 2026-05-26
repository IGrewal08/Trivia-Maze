package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.GameController;
import model.MultipleChoiceQuestion;
import model.Question;

/**
 * Displays the current trivia question and collects the player's answer.
 *
 * The panel renders differently per question type:
 * <ul>
 *   <li>{@code MULTIPLE_CHOICE} — one JRadioButton per option in a
 *       ButtonGroup, plus a Submit button.</li>
 *   <li>{@code TRUE_FALSE} — two buttons "True" and "False" that submit
 *       directly when clicked.</li>
 *   <li>{@code SHORT_ANSWER} — a JTextField for free-form input plus a
 *       Submit button.</li>
 * </ul>
 * A Skip button is always offered and submits {@link Question#SKIP},
 * which every question type treats as a free pass. When no question is
 * active the panel shows an idle message.
 *
 * QuestionPanel subscribes to the GameState's PropertyChangeSupport so
 * it can rebuild itself when {@code "QUESTION_ASKED"} fires and clear
 * itself when {@code "ANSWER_RESULT"} fires.
 *
 * @author Anwar Noor
 * @version 2.0
 */
public class QuestionPanel extends JPanel {

        /** Preferred panel width in pixels. */
    private static final int PANEL_WIDTH = 350;

    /** Preferred panel height in pixels. */
    private static final int PANEL_HEIGHT = 250;

    /** Number of columns in the short-answer text field. */
    private static final int ANSWER_FIELD_COLUMNS = 20;

    /** Font used for the question prompt. */
    private static final Font PROMPT_FONT = new Font("SansSerif", Font.BOLD, 14);

    /** Controller used to submit answers. */
    private final GameController myController;

    /** Currently displayed question, or null when idle. */
    private Question myQuestion;

    /** Reusable input field for short-answer questions. */
    private final JTextField myAnswerField;

    /** Radio buttons for the active multiple-choice question, if any. */
    private final List<JRadioButton> myOptionButtons;

    /**
     * Creates a QuestionPanel bound to the given controller.
     * Starts in the idle state.
     *
     * @param theController the controller used to submit answers; must not be null
     * @throws IllegalArgumentException if theController is null
     */
    public QuestionPanel(final GameController theController) {
        super();
        if (theController == null) {
            throw new IllegalArgumentException("GameController must not be null.");
        }

        myController = theController;
        myQuestion = null;
        myAnswerField = new JTextField(ANSWER_FIELD_COLUMNS);
        myOptionButtons = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        showIdleState();
    }

    /**
     * Displays the given question with the appropriate input widget.
     * Called by GuiView when it receives a QUESTION_ASKED event.
     *
     * @param theQuestion the question to display; must not be null
     */
    public void displayQuestion(final Question theQuestion) {
        if (theQuestion == null) {
            return;
        }
        myQuestion = theQuestion;
        buildQuestionUI();
    }

    /**
     * Shows a transient verdict message after an answer has been judged,
     * then returns the panel to its idle state. The label persists until
     * the next question arrives or the panel is interacted with.
     *
     * @param theCorrect true if the answer was judged correct
     */
    public void showResult(final boolean theCorrect) {
        removeAll();
        myQuestion = null;

        final JLabel verdict = new JLabel(
                theCorrect ? "Correct!" : "Incorrect.",
                SwingConstants.CENTER);
        verdict.setAlignmentX(Component.LEFT_ALIGNMENT);
        verdict.setFont(PROMPT_FONT);
        add(verdict);

        revalidate();
        repaint();
    }

    /**
     * Rebuilds the panel widgets for the current question.
     * Routes to the correct input style by question type.
     */
    private void buildQuestionUI() {
        removeAll();

        final JTextArea prompt = new JTextArea(myQuestion.getQuestionText());
        prompt.setEditable(false);
        prompt.setLineWrap(true);
        prompt.setWrapStyleWord(true);
        prompt.setOpaque(false);
        prompt.setFont(PROMPT_FONT);
        prompt.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(prompt);
        add(Box.createVerticalStrut(10));

        myOptionButtons.clear();

        switch (myQuestion.getQuestionType()) {
            case MULTIPLE_CHOICE -> addMultipleChoiceWidgets();
            case TRUE_FALSE      -> addTrueFalseWidgets();
            case SHORT_ANSWER    -> addShortAnswerWidgets();
        }

        add(Box.createVerticalStrut(10));
        add(buildSkipButton());

        revalidate();
        repaint();
    }

    /**
     * Adds one radio button per option of the active multiple-choice
     * question, grouped so only one can be selected, followed by a
     * Submit button that forwards the selected option's text.
     */
    private void addMultipleChoiceWidgets() {
        final MultipleChoiceQuestion mc = (MultipleChoiceQuestion) myQuestion;
        final List<String> options = mc.getOptions();
        final ButtonGroup group = new ButtonGroup();

        for (final String option : options) {
            final JRadioButton button = new JRadioButton(option);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(button);
            myOptionButtons.add(button);
            add(button);
        }

        final JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.addActionListener(e -> {
            for (final JRadioButton button : myOptionButtons) {
                if (button.isSelected()) {
                    submitAnswer(button.getText());
                    return;
                }
            }
        });
        add(Box.createVerticalStrut(6));
        add(submit);
    }

    /**
     * Adds two buttons that submit "true" or "false" directly when
     * clicked.
     */
    private void addTrueFalseWidgets() {
        final JButton trueButton = new JButton("True");
        final JButton falseButton = new JButton("False");
        trueButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        falseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        trueButton.addActionListener(e -> submitAnswer("true"));
        falseButton.addActionListener(e -> submitAnswer("false"));
        add(trueButton);
        add(Box.createVerticalStrut(4));
        add(falseButton);
    }

        /**
     * Adds the reusable answer field and a Submit button that forwards
     * the field's current text.
     */
    private void addShortAnswerWidgets() {
        myAnswerField.setText("");
        myAnswerField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, myAnswerField.getPreferredSize().height));
        myAnswerField.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(myAnswerField);

        final JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.addActionListener(e -> submitAnswer(myAnswerField.getText()));
        add(Box.createVerticalStrut(6));
        add(submit);
    }
    
    /**
     * Builds the Skip button. Skipping submits {@link Question#SKIP},
     * which every question type treats as a correct free-pass.
     *
     * @return the configured Skip button
     */
    private JButton buildSkipButton() {
        final JButton skip = new JButton("Skip");
        skip.setAlignmentX(Component.LEFT_ALIGNMENT);
        skip.addActionListener(e -> submitAnswer(Question.SKIP));
        return skip;
    }

    /**
     * Forwards an answer to the controller, guarding against the
     * null/blank input that {@code GameController.handleAnswer} would
     * otherwise reject with an exception.
     *
     * @param theAnswer the answer to submit
     */
    private void submitAnswer(final String theAnswer) {
        if (theAnswer == null || theAnswer.isBlank()) {
            return;
        }
        myController.handleAnswer(theAnswer);
    }

    /**
     * Shows the idle "no active question" state. Used at construction
     * and any time the panel needs to clear back to the resting view.
     */
    private void showIdleState() {
        removeAll();
        final JLabel idle = new JLabel("No active question.",
                SwingConstants.CENTER);
        idle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(idle);
        revalidate();
        repaint();
    }
}