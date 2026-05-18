package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import controller.GameController;
import model.Direction;
import model.Door;
import model.GameState;
import model.Room;

/**
 * Renders the player's current room: the room itself plus its up-to-four
 * doors and their state (locked, open, or blocked). Doors with no
 * underlying Door object are drawn as plain wall segments.
 *
 * RoomPanel subscribes to the GameState's PropertyChangeSupport so it
 * can refresh itself when the player moves into a new room
 * ({@code "CURRENT_POSITION"}) or when a door in the current room
 * changes state after an answer ({@code "ANSWER_RESULT"}). Clicking a
 * door forwards a move attempt to the controller.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public class RoomPanel extends JPanel implements PropertyChangeListener {

    /** Property name fired when the player's position changes. */
    private static final String CURRENT_POSITION = "CURRENT_POSITION";

    /** Property name fired when a trivia answer is submitted. */
    private static final String ANSWER_RESULT = "ANSWER_RESULT";

    /** Pixel width and height of the panel. */
    private static final int PANEL_SIZE = 300;

    /** Pixel inset from the panel edge to the room's wall. */
    private static final int ROOM_INSET = 60;

    /** Pixel thickness of a door's short edge. */
    private static final int DOOR_THICKNESS = 18;

    /** Pixel length of a door's long edge. */
    private static final int DOOR_LENGTH = 56;

    /** Background color of the panel. */
    private static final Color BACKGROUND_COLOR = new Color(40, 40, 40);

    /** Fill color of the room floor. */
    private static final Color FLOOR_COLOR = new Color(230, 220, 190);

    /** Color of the room's walls. */
    private static final Color WALL_COLOR = new Color(60, 40, 20);

    /** Fill color used for a door that is open. */
    private static final Color OPEN_DOOR_COLOR = new Color(80, 170, 90);

    /** Fill color used for a door that is permanently blocked. */
    private static final Color BLOCKED_DOOR_COLOR = new Color(190, 70, 70);

    /** Fill color used for a door that is still locked. */
    private static final Color LOCKED_DOOR_COLOR = new Color(170, 170, 170);

    /** Color used for the symbol drawn on locked/blocked doors. */
    private static final Color DOOR_SYMBOL_COLOR = Color.BLACK;

    /** Color used for the small coordinate label. */
    private static final Color LABEL_COLOR = new Color(80, 60, 30);

    /** Pixel thickness of the room's wall stroke. */
    private static final int WALL_STROKE = 4;

    /** Game state observed for moves and answer results. */
    private final GameState myState;

    /** Controller used to forward door-click move attempts. */
    private final GameController myController;

    /** The room currently being rendered. */
    private Room myRoom;

    /**
     * Creates a RoomPanel bound to the given state and controller.
     * Derives the initial room from the state's current position and
     * registers as a property change listener.
     *
     * @param theState the active GameState; must not be null
     * @param theController the controller used for click-to-move; must not be null
     * @throws IllegalArgumentException if either argument is null
     */
    public RoomPanel(final GameState theState,
                     final GameController theController) {
        super();
        if (theState == null) {
            throw new IllegalArgumentException("GameState must not be null.");
        }
        if (theController == null) {
            throw new IllegalArgumentException("GameController must not be null.");
        }

        myState = theState;
        myController = theController;
        myRoom = theState.getMaze().getRoom(theState.getCurrentPosition());

        theState.addPropertyChangeListener(this);

        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(BACKGROUND_COLOR);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent theEvent) {
                handleClick(theEvent.getX(), theEvent.getY());
            }
        });
    }

    /**
     * Handles property change events from the GameState. Refreshes the
     * tracked room on {@code "CURRENT_POSITION"} and repaints. On
     * {@code "ANSWER_RESULT"} the room reference is unchanged but a
     * door's state has flipped, so the panel just repaints. All other
     * events are ignored.
     *
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String name = theEvent.getPropertyName();
        if (CURRENT_POSITION.equals(name)) {
            myRoom = myState.getMaze().getRoom(myState.getCurrentPosition());
            repaint();
        } else if (ANSWER_RESULT.equals(name)) {
            repaint();
        }
    }

    /**
     * Paints the room's floor, walls, and each of its four door slots.
     * Each side is rendered based on whether {@code Room.getDoor} returns
     * a Door (state-dependent visual) or null (solid wall).
     *
     * @param theGraphics the Graphics context provided by Swing
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);

        if (myRoom == null) {
            theGraphics.setColor(Color.WHITE);
            theGraphics.drawString("No room to display.", 20, 20);
            return;
        }

        final Graphics2D g2 = (Graphics2D) theGraphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        final int floorSize = PANEL_SIZE - 2 * ROOM_INSET;
        g2.setColor(FLOOR_COLOR);
        g2.fillRect(ROOM_INSET, ROOM_INSET, floorSize, floorSize);

        g2.setColor(WALL_COLOR);
        g2.setStroke(new java.awt.BasicStroke(WALL_STROKE));
        g2.drawRect(ROOM_INSET, ROOM_INSET, floorSize, floorSize);

        for (final Direction dir : Direction.values()) {
            paintDoor(g2, dir);
        }

        g2.setColor(LABEL_COLOR);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("(" + myRoom.getX() + ", " + myRoom.getY() + ")",
                ROOM_INSET + 6, ROOM_INSET + 16);

        g2.dispose();
    }

    /**
     * Paints the door slot for one direction. If the room has no door
     * on that side, nothing is drawn (the wall already painted by
     * {@code paintComponent} represents a solid wall).
     *
     * @param theGraphics the graphics context
     * @param theDirection the side of the room to paint
     */
    private void paintDoor(final Graphics2D theGraphics,
                           final Direction theDirection) {
        final Door door = myRoom.getDoor(theDirection);
        if (door == null) {
            return;
        }

        final Rectangle rect = doorRect(theDirection);

        final Color fill;
        final String symbol;
        if (door.isOpen()) {
            fill = OPEN_DOOR_COLOR;
            symbol = null;
        } else if (door.isBlocked()) {
            fill = BLOCKED_DOOR_COLOR;
            symbol = "X";
        } else if (door.isLocked()) {
            fill = LOCKED_DOOR_COLOR;
            symbol = "?";
        } else {
            fill = LOCKED_DOOR_COLOR;
            symbol = null;
        }

        theGraphics.setColor(fill);
        theGraphics.fillRect(rect.x, rect.y, rect.width, rect.height);

        theGraphics.setColor(WALL_COLOR);
        theGraphics.drawRect(rect.x, rect.y, rect.width, rect.height);

        if (symbol != null) {
            theGraphics.setColor(DOOR_SYMBOL_COLOR);
            theGraphics.setFont(new Font("SansSerif", Font.BOLD, 14));
            final int textX = rect.x + rect.width / 2 - 4;
            final int textY = rect.y + rect.height / 2 + 5;
            theGraphics.drawString(symbol, textX, textY);
        }
    }

    /**
     * Computes the door rectangle for one side of the room. The
     * rectangle straddles the wall on that side so it visually
     * "interrupts" the wall stroke.
     *
     * @param theDirection the side of the room
     * @return the door rectangle in panel coordinates
     */
    private Rectangle doorRect(final Direction theDirection) {
        final int centerX = PANEL_SIZE / 2;
        final int centerY = PANEL_SIZE / 2;
        final int wallTop = ROOM_INSET;
        final int wallBottom = PANEL_SIZE - ROOM_INSET;
        final int wallLeft = ROOM_INSET;
        final int wallRight = PANEL_SIZE - ROOM_INSET;

        return switch (theDirection) {
            case NORTH -> new Rectangle(
                    centerX - DOOR_LENGTH / 2,
                    wallTop - DOOR_THICKNESS / 2,
                    DOOR_LENGTH,
                    DOOR_THICKNESS);
            case SOUTH -> new Rectangle(
                    centerX - DOOR_LENGTH / 2,
                    wallBottom - DOOR_THICKNESS / 2,
                    DOOR_LENGTH,
                    DOOR_THICKNESS);
            case WEST -> new Rectangle(
                    wallLeft - DOOR_THICKNESS / 2,
                    centerY - DOOR_LENGTH / 2,
                    DOOR_THICKNESS,
                    DOOR_LENGTH);
            case EAST -> new Rectangle(
                    wallRight - DOOR_THICKNESS / 2,
                    centerY - DOOR_LENGTH / 2,
                    DOOR_THICKNESS,
                    DOOR_LENGTH);
        };
    }

    /**
     * Handles a mouse click on the panel. If the click falls within a
     * door slot AND that side actually has a door, forwards a move
     * attempt to the controller. Clicks on solid walls or the floor
     * are ignored.
     *
     * @param thePixelX click X coordinate
     * @param thePixelY click Y coordinate
     */
    private void handleClick(final int thePixelX, final int thePixelY) {
        if (myRoom == null) {
            return;
        }
        for (final Direction dir : Direction.values()) {
            if (myRoom.getDoor(dir) != null
                    && doorRect(dir).contains(thePixelX, thePixelY)) {
                myController.handleMove(dir);
                return;
            }
        }
    }
}
