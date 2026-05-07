package model;
import java.io.Serializable;
import java.util.Map;

/**
 * Represents a room within the Trivia Maze consisting of doors in different directions.
 * 
 * Each room has it's own visited state and Map to store all possible doors/directions the player can take
 * once a player has entered a un-visited room the room becomes visited. Each door within this room is initially
 * locked, and player must answer the trivia question to proceed to the next room.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class Room implements Serializable {

    /** serialization Unique ID for version control. */
    private static final long serialVersionUID = 1L;
    
    /** X coordinate of this room within the Trivia Maze. */
    private int myX;
    /** Y coordinate fo this room within the Trivia Maze. */
    private int myY;
    /** Whether this room has been visited before .*/
    private boolean myIsVisited;
    /** Hash map to hold all of the doors within this room (contain Direction and Door objects). */
    private Map<Direction, Door> myDoors;

    /**
     * Construct a Room with up to four door max, the rooms initial visited state being false.
     * 
     * The provided X and Y locations are assigned once and are permanent for this door after construction.
     * 
     * @param theX X coordinate location on the 2D trivia maze
     * @param theY Y coordinate location on the 2D trivia maze
     */
    public Room(final int theX, final int theY) {
        if (theX < 0 || theY < 0) {
            throw new IllegalArgumentException("X and Y coordinates must be positive integers.");
        }
        this.myX = theX;
        this.myY = theY;
        this.myIsVisited = false;
    }

    /**
     * Returns the door at which the player is currently looking at.
     * 
     * @param theDir the direction the player is looking at
     * @return the door in the direction the player is looking at
     */
    public Door getDoor(final Direction theDir) {
        if (theDir == null) {
            throw new IllegalArgumentException("Direction must not be null.");
        }
        return myDoors.get(theDir);
    }

    /**
     * Returns whether this direction has a door or not.
     * 
     * @param theDir the direction being checked
     * @return {@code true} if there is a door in that direction, {@code false} otherwise
     */
    public boolean hasDoor(final Direction theDir) {
        if (theDir == null) {
            throw new IllegalArgumentException("Direction must not be null");
        }
        return myDoors.containsKey(theDir);
    }

    /**
     * Returns the X coordinate assigned to this room
     * 
     * @return the current X coordinate of this room
     */
    public int getX() {
        return myX;
    }

    /**
     * Returns the Y coordinate assigned to this room
     * 
     * @return the current Y coordinate of this room
     */
    public int getY() {
        return myY;
    }

    /**
     * Returns whether this room has been visited before
     * 
     * @return {@code true} if room was visited before, {@code false} otherwise
     */
    public boolean isVisited() {
        return myIsVisited;
    }

    /**
     * Changes this rooms visited status to from given parameter.
     * 
     * @param theVisited the value indicating if this room is marked visited
     */
    public void setVisited(final boolean theVisited) {
        this.myIsVisited = theVisited;
    }

    /**
     * Constructs and add doors to doors map to build this room.
     * 
     * @param theDir the given direction to build a door
     * @param theDoor the door being place within this room
     */
    protected void addDoor(final Direction theDir, final Door theDoor) {
        if (theDir == null || theDoor == null) {
            throw new IllegalArgumentException("Direction or Door must not be null.");
        }
        myDoors.put(theDir, theDoor);
    }

    /**
     * Returns string representation of this room's current state.
     * 
     * @return the current state of this room (X, Y, doors, visited)
     */
    public String toString() {
        return String.format("Room [X=%b, Y=%b, doors=%b, visited=%b]", 
            myX, myY, myDoors.toString(), myIsVisited);
    }
}
