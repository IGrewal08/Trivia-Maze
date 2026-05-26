package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores and manages the active state of the Trivia Maze game.
 * This includes the maze, player position, current direction,
 * visited rooms, and overall game status.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class GameState implements Serializable {

    /** Current maze for the active game. */
    private Maze myMaze;

    /** Current player position in the maze. */
    private Position myCurrentPosition;

    /** Current direction the player is facing. */
    private Direction myCurrentDirection;

    /** All rooms visited by the player. */
    private Set<Position> myVisitedRooms;

    /** Current game status. */
    private GameStatus myStatus;

    /** Handles property change events for GUI updates. */
    private transient PropertyChangeSupport myPCS;

    /**
     * Creates a new game state with a maze of the given size.
     *
     * @param theWidth maze width
     * @param theHeight maze height
     */
    public GameState(final int theWidth, final int theHeight) {

        myMaze = new Maze(theWidth, theHeight);
        myCurrentPosition = new Position(0, 0);
        myCurrentDirection = Direction.NORTH;

        myVisitedRooms = new HashSet<>();
        myVisitedRooms.add(myCurrentPosition);

        myStatus = GameStatus.ACTIVE;

        myPCS = new PropertyChangeSupport(this);
    }

    /**
     * Returns the active maze.
     *
     * @return current maze
     */
    public Maze getMaze() {
        return myMaze;
    }

    /**
     * Returns the player's current position.
     *
     * @return current position
     */
    public Position getCurrentPosition() {
        return myCurrentPosition;
    }

    /**
     * Updates the player's current position.
     *
     * @param thePosition new player position
     */
    public void setCurrentPosition(final Position thePosition) {

        Position oldPosition = myCurrentPosition;

        myCurrentPosition = thePosition;

        firePropertyChange("CURRENT_POSITION",
                oldPosition,
                thePosition);
    }

    /**
     * Returns the player's current direction.
     *
     * @return current direction
     */
    public Direction getCurrentDirection() {
        return myCurrentDirection;
    }

    /**
     * Updates the player's current direction.
     *
     * @param theDirection new direction
     */
    public void setCurrentDirection(final Direction theDirection) {

        Direction oldDirection = myCurrentDirection;

        myCurrentDirection = theDirection;

        firePropertyChange("CURRENT_DIRECTION",
                oldDirection,
                theDirection);
    }

    /**
     * Adds a room position to the visited set.
     *
     * @param thePosition visited room position
     */
    public void addVisitedRoom(final Position thePosition) {
        myVisitedRooms.add(thePosition);
    }

    /**
     * Returns all visited rooms.
     *
     * @return visited room set
     */
    public Set<Position> getVisitedRooms() {
        return new HashSet<>(myVisitedRooms);
    }

    /**
     * Returns the current game status.
     *
     * @return current status
     */
    public GameStatus getStatus() {
        return myStatus;
    }

    /**
     * Updates the current game status.
     *
     * @param theStatus new game status
     */
    public void setStatus(final GameStatus theStatus) {

        GameStatus oldStatus = myStatus;

        myStatus = theStatus;

        firePropertyChange("GAME_STATUS",
                oldStatus,
                theStatus);
    }

    /**
     * Adds a property change listener.
     *
     * @param theListener listener to add
     */
    public void addPropertyChangeListener(
            final PropertyChangeListener theListener) {

        getPCS().addPropertyChangeListener(theListener);
    }

    /**
     * Removes a property change listener.
     *
     * @param theListener listener to remove
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener theListener) {

        getPCS().removePropertyChangeListener(theListener);
    }

    /**
     * Fires a property change event.
     *
     * @param theProperty property name
     * @param theOldValue old value
     * @param theNewValue new value
     */
    public void firePropertyChange(final String theProperty,
                                   final Object theOldValue,
                                   final Object theNewValue) {

        getPCS().firePropertyChange(
                theProperty,
                theOldValue,
                theNewValue);
    }

    /**
     * Returns the property change support object.
     *
     * @return property change support
     */
    private PropertyChangeSupport getPCS() {

        if (myPCS == null) {
            myPCS = new PropertyChangeSupport(this);
        }

        return myPCS;
    }
}