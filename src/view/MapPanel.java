package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.JPanel;

import controller.GameController;
import model.GameState;
import model.Maze;
import model.Position;

/**
 * Visual map of the Trivia Maze. Renders the room grid, highlights
 * which rooms have been visited, and marks the player's current
 * location, the entrance, and the exit.
 *
 * MapPanel subscribes to the GameState's property change events and
 * repaints itself whenever the player's position changes.
 *
 * @author Anwar Noor
 * @version 2.0
 */
public class MapPanel extends JPanel {

    /** Pixel size of a single room cell. */
    private static final int CELL_SIZE = 60;

    /** Pixel margin around the grid. */
    private static final int MARGIN = 10;

    /** Inset, in pixels, between the player marker and the cell edge. */
    private static final int PLAYER_INSET = 12;

    /** Background color of the panel. */
    private static final Color BACKGROUND_COLOR = new Color(40, 40, 40);

    /** Fill color for rooms that have not been visited. */
    private static final Color UNVISITED_COLOR = new Color(80, 80, 80);

    /** Fill color for rooms the player has already visited. */
    private static final Color VISITED_COLOR = new Color(150, 200, 230);

    /** Fill color for the player's current room. */
    private static final Color CURRENT_ROOM_COLOR = new Color(255, 230, 130);

    /** Color used to draw the grid lines between rooms. */
    private static final Color GRID_LINE_COLOR = Color.BLACK;

    /** Color used to draw the entrance marker. */
    private static final Color ENTRANCE_COLOR = new Color(60, 160, 90);

    /** Color used to draw the exit marker. */
    private static final Color EXIT_COLOR = new Color(200, 70, 70);

    /** Color used to draw the player marker. */
    private static final Color PLAYER_COLOR = new Color(20, 60, 200);

    /** Controller queried for position, visited rooms, and maze. */
    private final GameController myController;

    /** The maze being drawn. */
    private Maze myMaze;

    /** The player's current position in the maze. */
    private Position myCurrentPos;

    /** Positions of rooms the player has already visited. */
    private Set<Position> myVisited;

    /**
     * Creates a MapPanel bound to the given game state. Registers itself
     * as a property change listener on the state so it can repaint when
     * the player moves.
     *
     * @param theState the active GameState; must not be null
     * @throws IllegalArgumentException if theState is null
     */
    public MapPanel(final GameController theController) {
        super();
        if (theController == null) {
            throw new IllegalArgumentException("GameController must not be null.");
        }
        myController = theController;

        // Fetch initial state from controller — not from GameState directly
        myMaze       = myController.getMaze();
        myCurrentPos = myController.getCurrentPosition();
        myVisited    = myController.getVisitedRooms();

        final int width  = myMaze.getWidth()  * CELL_SIZE + 2 * MARGIN;
        final int height = myMaze.getHeight() * CELL_SIZE + 2 * MARGIN;
        setPreferredSize(new Dimension(width, height));
        setBackground(BACKGROUND_COLOR);
    }

    public void refreshMaze() {
        myMaze       = myController.getMaze();
        myCurrentPos = myController.getCurrentPosition();
        myVisited    = myController.getVisitedRooms();

        // Recalculate preferred size in case maze dimensions changed
        final int width  = myMaze.getWidth()  * CELL_SIZE + 2 * MARGIN;
        final int height = myMaze.getHeight() * CELL_SIZE + 2 * MARGIN;
        setPreferredSize(new Dimension(width, height));

        revalidate();
        repaint();
    }

    /**
     * Paints the maze grid, marks entrance and exit, and draws the
     * player marker on top of the current room.
     *
     * @param theGraphics the Graphics context provided by Swing
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);

        final Graphics2D g2 = (Graphics2D) theGraphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        final Position entrance = myMaze.getEntrance();
        final Position exit     = myMaze.getExit();

        for (int y = 0; y < myMaze.getHeight(); y++) {
            for (int x = 0; x < myMaze.getWidth(); x++) {
                final Position pos    = new Position(x, y);
                final int     pixelX  = MARGIN + x * CELL_SIZE;
                final int     pixelY  = MARGIN + y * CELL_SIZE;

                final Color fill;
                if (pos.equals(myCurrentPos)) {
                    fill = CURRENT_ROOM_COLOR;
                } else if (myVisited.contains(pos)) {
                    fill = VISITED_COLOR;
                } else {
                    fill = UNVISITED_COLOR;
                }

                g2.setColor(fill);
                g2.fillRect(pixelX, pixelY, CELL_SIZE, CELL_SIZE);
                g2.setColor(GRID_LINE_COLOR);
                g2.drawRect(pixelX, pixelY, CELL_SIZE, CELL_SIZE);

                if (pos.equals(entrance)) {
                    drawLabel(g2, "S", ENTRANCE_COLOR, pixelX, pixelY);
                } else if (pos.equals(exit)) {
                    drawLabel(g2, "E", EXIT_COLOR, pixelX, pixelY);
                }
            }
        }

        // Draw player marker on top of current room
        final int playerX        = MARGIN + myCurrentPos.getX() * CELL_SIZE + PLAYER_INSET;
        final int playerY        = MARGIN + myCurrentPos.getY() * CELL_SIZE + PLAYER_INSET;
        final int playerDiameter = CELL_SIZE - 2 * PLAYER_INSET;
        g2.setColor(PLAYER_COLOR);
        g2.fillOval(playerX, playerY, playerDiameter, playerDiameter);

        g2.dispose();
    }

    /**
     * Draws a single-character label in the upper-left of a cell, used
     * to mark the entrance and exit rooms.
     *
     * @param theGraphics the graphics context
     * @param theLabel the label text to draw
     * @param theColor the color to draw the label in
     * @param thePixelX the cell's top-left X coordinate in pixels
     * @param thePixelY the cell's top-left Y coordinate in pixels
     */
    private void drawLabel(final Graphics2D theGraphics,
                           final String theLabel,
                           final Color theColor,
                           final int thePixelX,
                           final int thePixelY) {
        theGraphics.setColor(theColor);
        theGraphics.drawString(theLabel, thePixelX + 6, thePixelY + 16);
    }
}
