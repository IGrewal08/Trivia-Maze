package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores an immutable 2D position used to identify locations in the Trivia Maze grid.
 * Two Positions with the same coordinates are equal and have the same hash code,
 * making this class safe to use as a key in HashMap or element in HashSet
 * (e.g. for tracking visited rooms during pathfinding).
 *
 * @author Anwar Noor
 * @version 1.0
 */
public final class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    /** X coordinate of this position (column in the maze grid). */
    private final int myX;

    /** Y coordinate of this position (row in the maze grid). */
    private final int myY;

    /**
     * Creates a new Position with the given coordinates.
     *
     * @param theX X coordinate
     * @param theY Y coordinate
     */
    public Position(final int theX, final int theY) {
        myX = theX;
        myY = theY;
    }

    /**
     * @return X coordinate of this position
     */
    public int getX() {
        return myX;
    }

    /**
     * @return Y coordinate of this position
     */
    public int getY() {
        return myY;
    }

    /**
     * Returns a new Position translated one step in the given direction.
     * This Position is not modified.
     *
     * @param theDirection direction to move toward
     * @return new Position one step in theDirection
     */
    public Position translate(final Direction theDirection) {
        return switch (theDirection) {
            case NORTH -> new Position(myX, myY - 1);
            case SOUTH -> new Position(myX, myY + 1);
            case EAST  -> new Position(myX + 1, myY);
            case WEST  -> new Position(myX - 1, myY);
        };
    }

    /**
     * Two Positions are equal if and only if they have the same X and Y coordinates.
     *
     * @param theOther object to compare to
     * @return true if theOther is a Position with the same coordinates
     */
    @Override
    public boolean equals(final Object theOther) {
        if (this == theOther) {
            return true;
        }
        if (theOther == null || getClass() != theOther.getClass()) {
            return false;
        }
        final Position other = (Position) theOther;
        return myX == other.myX && myY == other.myY;
    }

    /**
     * @return hash code consistent with equals
     */
    @Override
    public int hashCode() {
        return Objects.hash(myX, myY);
    }

    /**
     * @return string representation in the form "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + myX + ", " + myY + ")";
    }
}