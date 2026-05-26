package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.GameController;
import model.Direction;
import model.GameState;
import model.GameStatus;
import model.Question;

/**
 * Top-level Swing window for the Trivia Maze. Assembles the four view
 * panels — {@link MapPanel}, {@link RoomPanel}, {@link QuestionPanel}, and
 * {@link ControlPanel} — around a {@link BorderLayout} with a status label
 * along the top.
 *
 * <p>The panels are not built in the constructor because three of them
 * require both a {@link GameState} and a {@link GameController}, and the
 * controller must be constructed after the view. The constructor accepts
 * the state and renders the frame chrome; panel assembly happens in
 * {@link #setController(GameController)}, which is the moment both
 * dependencies are available.
 *
 * <p>{@code RoomPanel}, {@code QuestionPanel}, and {@code MapPanel} each
 * register themselves as {@link java.beans.PropertyChangeListener}s on
 * the {@code GameState} from their own constructors, so this class wires
 * no listeners directly.
 *
 * @author Nicholas Cortes
 * @version 2.0
 */
public class GuiView extends JFrame implements GameView, PropertyChangeListener {

    /** Default frame width in pixels. */
    private static final int FRAME_WIDTH = 1200;

    /** Default frame height in pixels. */
    private static final int FRAME_HEIGHT = 700;

    /** The active game state, shared with every panel. */
    private final GameState myState;

    /** Status banner shown across the top of the frame. */
    private JLabel myStatusLabel;

    /** The controller; null until {@link #setController(GameController)} runs. */
    private GameController myController;

    private MapPanel myMapPanel;
    private RoomPanel myRoomPanel;
    private QuestionPanel myQuestionPanel;
    private ControlPanel myControlPanel;

    /**
     * Creates a GuiView bound to the given state. Renders the frame
     * chrome and status banner; defers panel assembly until
     * {@link #setController(GameController)} is called.
     *
     * @param theState the active GameState; must not be null
     * @throws IllegalArgumentException if theState is null
     */
    public GuiView(final GameState theState) {
        super();
        if (theState == null) {
            throw new IllegalArgumentException("GameState must not be null.");
        }
        myState = theState;
        initialize();
    }

    /**
     * Builds the frame chrome and an empty status banner. Panel content
     * is added later by {@link #setController(GameController)}.
     */
    @Override
    public void initialize() {
        setTitle("Trivia Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        myStatusLabel = new JLabel("Welcome to Trivia Maze!", SwingConstants.CENTER);
        myStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(myStatusLabel, BorderLayout.NORTH);
    }

    /**
     * Stores the controller and assembles the four panels. Safe to call
     * exactly once; calling it again would stack a second copy of each
     * panel onto the frame.
     *
     * @param theController the controller for player input; must not be null
     * @throws IllegalArgumentException if theController is null
     */
    @Override
    public void setController(final GameController theController) {
        if (theController == null) {
            throw new IllegalArgumentException("GameController must not be null.");
        }
        myController = theController;

        myMapPanel = new MapPanel(myState);
        myRoomPanel = new RoomPanel(myState, myController);
        myQuestionPanel = new QuestionPanel(myState, myController);
        myControlPanel = new ControlPanel(myController);

        add(wrap(myMapPanel), BorderLayout.WEST);
        add(wrap(myRoomPanel), BorderLayout.CENTER);
        add(wrap(myQuestionPanel), BorderLayout.EAST);
        add(wrap(myControlPanel), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Wraps a panel in a JPanel container to prevent BorderLayout from
     * stretching it past its preferred size, keeping each child's
     * intended dimensions intact.
     */
    private static JPanel wrap(final JPanel theInner) {
        final JPanel wrapper = new JPanel();
        wrapper.add(theInner);
        return wrapper;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (myMapPanel == null || myQuestionPanel == null) {
            return;
        }

        switch (theEvent.getPropertyName()) {

            case "MAZE_UPDATED" -> {
                myMapPanel.repaint();
                myRoomPanel.repaint();
                updateDirectionButtons();
            }

            case "PLAYER_MOVED" -> {
                myMapPanel.repaint();
                myRoomPanel.repaint();
                updateDirectionButtons();
            }

            case "QUESTION_ASKED" -> {
                Question q = (Question) theEvent.getNewValue();
                myQuestionPanel.displayQuestion(q);
            }

            case "ANSWER_RESULT" -> {
                boolean correct = (boolean) theEvent.getNewValue();
                myQuestionPanel.showResult(correct);
            }

            case "GAME_OVER" -> {
                GameStatus status = (GameStatus) theEvent.getNewValue();
                showMessage(status == GameStatus.WON
                        ? "You reached the exit! You win!"
                        : "No paths remain. Game over.");
            }

            case "INVALID_MOVE" -> {
                showMessage("Invalid move.");
            }
        }
    }

    /**
     * Asks the controller which directions are currently available
     * and updates the control panel buttons accordingly.
     * Controller queries the model — GuiView never touches GameState directly.
     */
    private void updateDirectionButtons() {
        for (Direction dir : Direction.values()) {
            boolean enabled = myController.isDirectionAvailable(dir);
            myControlPanel.enableDirection(dir, enabled);
        }
    }

    /**
     * Full-frame repaint and layout pass. Each panel already repaints
     * itself in response to property change events, so this exists as a
     * safety net for callers that want a global refresh.
     */
    @Override
    public void updateView() {
        repaint();
        revalidate();
    }

    /**
     * No-op. The active {@link QuestionPanel} displays questions by
     * listening to {@code QUESTION_ASKED} on the {@code GameState}, so
     * this {@link GameView} method is redundant. Kept to satisfy the
     * interface contract.
     *
     * @param theQuestion ignored
     */
    @Override
    public void showQuestions(final Question theQuestion) {
        // intentionally empty — QuestionPanel handles question display via PCL
    }

    /**
     * Updates the status label at the top of the frame.
     *
     * @param theMessage the message to show
     */
    @Override
    public void showMessage(final String theMessage) {
        myStatusLabel.setText(theMessage);
    }
}
