package model;

import java.io.Serializable;

/**
 * Represents the Trivia Maze grid containing all rooms.
 *
 * This is currently a simplified implementation used to
 * support controller integration and compilation.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class Maze implements Serializable {
    
    private Room[][] myRooms;
    private int myWidth;
    private int myHeight;

    /**
     * Creates a maze with the given dimensions.
     *
     * @param theWidth maze width
     * @param theHeight maze height
     */
    public Maze(final int theWidth, final int theHeight) {

        if (theWidth <= 0 || theHeight <= 0) {
            throw new IllegalArgumentException("Maze dimensions must be positive.");
        }

        myWidth = theWidth;
        myHeight = theHeight;

        myRooms = new Room[theHeight][theWidth];

        for (int row = 0; row < theHeight; row++) {
            for (int col = 0; col < theWidth; col++) {
                myRooms[row][col] = new Room(col, row);
            }
        }
    }

    /**
     * Returns the room at the given position.
     *
     * @param thePosition room position
     * @return room at that position
     */
    public Room getRoom(final Position thePosition) {

        if (thePosition == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }

        return myRooms[thePosition.getY()][thePosition.getX()];
    }

    /**
     * Temporary stub implementation.
     *
     * Later this should determine if a valid
     * path to the exit still exists.
     *
     * @param thePosition current player position
     * @return true for now
     */
    public boolean isPathAvailable(final Position thePosition) {
        return true;
    }

    public int getWidth() {
        return myWidth;
    }

    public int getHeight() {
        return myHeight;
    }

    public boolean isInBounds(final Position thePosition) {
        if (thePosition == null) {
            return false;
        }

        return thePosition.getX() >= 0
                && thePosition.getX() < myWidth
                && thePosition.getY() >= 0
                && thePosition.getY() < myHeight;
    }
}