package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.GameController;
import model.Direction;
import model.GameStatus;
import model.Question;

/**
 * Top-level Swing window for the Trivia Maze. Assembles the four view
 * panels — {@link MapPanel}, {@link RoomPanel}, {@link QuestionPanel}, and
 * {@link ControlPanel} — around a {@link BorderLayout}, with a menu bar, a
 * status + live-stats header, and titled "card" framing around each panel.
 *
 * <p>The panels are not built in the constructor because three of them
 * require a {@link GameController}, which must be constructed after the
 * view. The constructor renders the frame chrome; panel assembly happens
 * in {@link #setController(GameController)}, the moment the controller is
 * available.
 *
 * @author Nicholas Cortes
 * @version 3.0
 */
public class GuiView extends JFrame implements GameView {

    /** Default frame width in pixels. */
    private static final int FRAME_WIDTH = 1260;

    /** Default frame height in pixels. */
    private static final int FRAME_HEIGHT = 920;

    /** Status banner shown across the top of the frame. */
    private JLabel myStatusLabel;

    /** Live stats label (correct / wrong / rooms). */
    private JLabel myStatsLabel;

    /** Running count of correctly answered questions. */
    private int myCorrectCount;

    /** Running count of incorrectly answered questions. */
    private int myWrongCount;

    /** The controller; null until {@link #setController(GameController)} runs. */
    private GameController myController;

    private MapPanel myMapPanel;
    private RoomPanel myRoomPanel;
    private QuestionPanel myQuestionPanel;
    private ControlPanel myControlPanel;

    /**
     * Creates a GuiView. Renders the frame chrome and header; defers panel
     * assembly until {@link #setController(GameController)} is called.
     */
    public GuiView() {
        super();
        initialize();
    }

    /**
     * Builds the frame chrome, menu bar, and the status + stats header.
     * Panel content is added later by {@link #setController(GameController)}.
     */
    @Override
    public void initialize() {
        setTitle("Trivia Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.BG);

        add(buildHeader(), BorderLayout.NORTH);
    }

    /**
     * Builds the top header: a large status message with a live stats line
     * beneath it, on a white surface separated by a hairline divider.
     *
     * @return the assembled header panel
     */
    private JPanel buildHeader() {
        final JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UiTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        myStatusLabel = new JLabel("Welcome to Trivia Maze!", SwingConstants.CENTER);
        myStatusLabel.setFont(UiTheme.HEADING);
        myStatusLabel.setForeground(UiTheme.TEXT);
        myStatusLabel.setAlignmentX(CENTER_ALIGNMENT);

        myStatsLabel = new JLabel(statsText(), SwingConstants.CENTER);
        myStatsLabel.setFont(UiTheme.CAPTION);
        myStatsLabel.setForeground(UiTheme.TEXT_MUTED);
        myStatsLabel.setAlignmentX(CENTER_ALIGNMENT);

        header.add(myStatusLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(myStatsLabel);
        return header;
    }

    /**
     * Builds the menu bar wiring the existing controller actions (new,
     * save, load, exit) plus a help menu. Installed in
     * {@link #setController(GameController)} once the controller exists.
     *
     * @return the assembled menu bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();

        final JMenu game = new JMenu("Game");
        final JMenuItem newGame = new JMenuItem("New Game");
        final JMenuItem save = new JMenuItem("Save Game");
        final JMenuItem load = new JMenuItem("Load Game");
        final JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(e -> startNewGame());
        save.addActionListener(e -> doSave());
        load.addActionListener(e -> doLoad());
        exit.addActionListener(e -> {
            myController.exitGame();
        });

        game.add(newGame);
        game.addSeparator();
        game.add(save);
        game.add(load);
        game.addSeparator();
        game.add(exit);

        final JMenu help = new JMenu("Help");
        final JMenuItem instructions = new JMenuItem("How to Play");
        final JMenuItem about = new JMenuItem("About");
        instructions.addActionListener(e -> showInstructions());
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Trivia Maze\nAnswer trivia to unlock doors and reach the exit.\nVersion 3.0",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(instructions);
        help.add(about);

        bar.add(game);
        bar.add(help);
        return bar;
    }

    /**
     * Stores the controller, installs the menu bar, and assembles the four
     * panels inside titled cards. Safe to call exactly once.
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

        setJMenuBar(buildMenuBar());

        myMapPanel = new MapPanel(myController);
        myRoomPanel = new RoomPanel(myController);
        myQuestionPanel = new QuestionPanel(myController);
        myControlPanel = new ControlPanel(myController);

        myControlPanel.registerKeyBindings(this);

        add(card(myMapPanel, "Maze Map"), BorderLayout.WEST);
        add(card(myRoomPanel, "Current Room"), BorderLayout.CENTER);
        add(card(myQuestionPanel, "Question"), BorderLayout.EAST);
        add(card(myControlPanel, "Move  —  WASD / Arrow Keys"), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Wraps a panel in a centered, themed container with a titled-card
     * border so each zone reads as a distinct region. The inner panel keeps
     * its preferred size; the card supplies the frame, title, and padding.
     *
     * @param theInner the panel to frame
     * @param theTitle the card title
     * @return the card container
     */
    private static JPanel card(final JPanel theInner, final String theTitle) {
        final JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.add(theInner);
        UiTheme.asCard(wrapper, theTitle);
        return wrapper;
    }

    /**
     * Dedicates method call depending on which event was fired; serves as
     * the connecting point for all components in the application.
     *
     * @param theEvent the fired event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (myMapPanel == null || myQuestionPanel == null) {
            return;
        }

        switch (theEvent.getPropertyName()) {

            case "MAZE_UPDATED" -> {
                myMapPanel.refreshMaze();
                myRoomPanel.refreshRoom();
                updateDirectionButtons();
                refreshStats();
            }

            case "PLAYER_MOVED" -> {
                myMapPanel.refreshMaze();
                myRoomPanel.refreshRoom();
                updateDirectionButtons();
                refreshStats();
            }

            case "QUESTION_ASKED" -> {
                Question q = (Question) theEvent.getNewValue();
                myQuestionPanel.displayQuestion(q);
                myControlPanel.setKeyBindingsEnabled(this, false);
            }

            case "ANSWER_RESULT" -> {
                boolean correct = (boolean) theEvent.getNewValue();
                if (correct) {
                    myCorrectCount++;
                } else {
                    myWrongCount++;
                }
                myQuestionPanel.showResult(correct);
                myRoomPanel.refreshRoom();
                myControlPanel.setKeyBindingsEnabled(this, true);
                refreshStats();
            }

            case "GAME_OVER" -> {
                final String[] choices = {"Play Again", "Quit"};
                for (Direction dir : Direction.values()) {
                    myControlPanel.enableDirection(dir, false);
                }
                GameStatus status = (GameStatus) theEvent.getNewValue();
                final int res = JOptionPane.showOptionDialog(this,
                    status == GameStatus.WON ? "You Win!" : "No paths remain. Game over.",
                    "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, choices, choices[0]);
                if (res == 0) {
                    startNewGame();
                } else {
                    this.closeGame();
                }
            }

            case "INVALID_MOVE" -> showMessage("Invalid move.");

            default -> { /* ignore unrelated events */ }
        }
    }

    /**
     * Starts a fresh game, resets the stats counters, and returns the
     * panels to a playable state. Shared by the menu and the play-again
     * prompt.
     */
    private void startNewGame() {
        myCorrectCount = 0;
        myWrongCount = 0;
        myController.newGame(8, 8);
        myControlPanel.setKeyBindingsEnabled(this, true);
        myQuestionPanel.showIdleState();
        refreshStats();
        showMessage("New game started — reach the exit (E)!");
    }

    /**
     * Prompts for a save name and delegates to the controller.
     */
    private void doSave() {
        final String name = JOptionPane.showInputDialog(this,
                "Save name:", "Save Game", JOptionPane.QUESTION_MESSAGE);
        if (name != null && !name.isBlank()) {
            myController.saveGame(name.trim());
            showMessage("Game saved as \"" + name.trim() + "\".");
        }
    }

    /**
     * Prompts for a save name to load and delegates to the controller.
     */
    private void doLoad() {
        final String name = JOptionPane.showInputDialog(this,
                "Load name:", "Load Game", JOptionPane.QUESTION_MESSAGE);
        if (name != null && !name.isBlank()) {
            myController.loadGame(name.trim());
            myCorrectCount = 0;
            myWrongCount = 0;
            myControlPanel.setKeyBindingsEnabled(this, true);
            myQuestionPanel.showIdleState();
            refreshStats();
            showMessage("Loaded \"" + name.trim() + "\".");
        }
    }

    /**
     * Shows a styled, modal welcome / start screen over the game window.
     * Called once at launch.
     */
    public void showWelcome() {
        final JDialog dialog = new JDialog(this, "Welcome", true);
        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UiTheme.SURFACE);
        content.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        final JLabel title = new JLabel("Trivia Maze");
        title.setFont(UiTheme.TITLE);
        title.setForeground(UiTheme.PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        final JLabel tagline = new JLabel("Answer trivia to unlock doors and escape the maze.");
        tagline.setFont(UiTheme.BODY);
        tagline.setForeground(UiTheme.TEXT_MUTED);
        tagline.setAlignmentX(CENTER_ALIGNMENT);

        final JLabel rules = new JLabel("<html><div style='text-align:center;'>"
                + "Move with <b>WASD</b> or the <b>arrow keys</b>, or click a door.<br>"
                + "A locked door asks a question — answer it right to pass.<br>"
                + "A wrong answer blocks that door for good. Reach <b>E</b> to win."
                + "</div></html>");
        rules.setFont(UiTheme.BODY);
        rules.setForeground(UiTheme.TEXT);
        rules.setAlignmentX(CENTER_ALIGNMENT);

        final JButton start = new JButton("Start Game");
        UiTheme.styleButton(start, true);
        start.setAlignmentX(CENTER_ALIGNMENT);
        start.addActionListener(e -> dialog.dispose());

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(tagline);
        content.add(Box.createVerticalStrut(18));
        content.add(rules);
        content.add(Box.createVerticalStrut(24));
        content.add(start);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Shows the how-to-play instructions in a dialog.
     */
    private void showInstructions() {
        JOptionPane.showMessageDialog(this,
                "How to Play\n\n"
                + "• Move with WASD or the arrow keys, or click a door.\n"
                + "• Locked doors ('?') pose a trivia question.\n"
                + "• Answer correctly to open the door (green) and pass through.\n"
                + "• A wrong answer blocks the door ('X') permanently.\n"
                + "• Use Skip sparingly — it grants a free pass.\n"
                + "• Reach the exit (E) before every path is blocked.",
                "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Asks the controller which directions are currently available and
     * updates the control panel buttons accordingly.
     */
    private void updateDirectionButtons() {
        for (Direction dir : Direction.values()) {
            boolean enabled = myController.isDirectionAvailable(dir);
            myControlPanel.enableDirection(dir, enabled);
        }
    }

    /** Recomputes and repaints the stats line. */
    private void refreshStats() {
        if (myStatsLabel != null) {
            myStatsLabel.setText(statsText());
        }
    }

    /**
     * Builds the stats line text from the current counters and the
     * controller's visited-room count.
     *
     * @return the formatted stats string
     */
    private String statsText() {
        final int rooms = myController == null ? 0 : myController.getVisitedRooms().size();
        return "Correct: " + myCorrectCount
                + "      Wrong: " + myWrongCount
                + "      Rooms explored: " + rooms;
    }

    /**
     * Closes the JFrame, ending the game.
     */
    @Override
    public void closeGame() {
        dispose();
    }

    /**
     * Full-frame repaint and layout pass.
     */
    @Override
    public void updateView() {
        repaint();
        revalidate();
    }

    /**
     * No-op. The active {@link QuestionPanel} displays questions by
     * listening to {@code QUESTION_ASKED}, so this is redundant. Kept to
     * satisfy the interface contract.
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
