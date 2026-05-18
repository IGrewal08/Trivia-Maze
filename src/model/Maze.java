package model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

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
     * Returns the entrance position of the maze. The entrance is the
     * fixed starting cell at (0, 0).
     *
     * @return the entrance position
     */
    public Position getEntrance() {
        return new Position(0, 0);
    }

    /**
     * Returns the exit position of the maze. The exit is the
     * bottom-right cell at (width - 1, height - 1).
     *
     * @return the exit position
     */
    public Position getExit() {
        return new Position(myWidth - 1, myHeight - 1);
    }

    /**
     * Returns whether a path from {@code theFrom} to the exit still
     * exists, where "path" means a sequence of adjacent rooms connected
     * by doors that are not permanently blocked. Locked-but-not-blocked
     * doors are considered traversable, since the player has not yet
     * foreclosed them; only a blocked door is treated as a dead end.
     * Uses BFS across the grid.
     *
     * @param theFrom the starting position
     * @return true if the exit is reachable from {@code theFrom} through
     *         non-blocked doors, false otherwise
     * @throws IllegalArgumentException if theFrom is null
     */
    public boolean isPathPossible(final Position theFrom) {
        if (theFrom == null) {
            throw new IllegalArgumentException("Starting position must not be null.");
        }
        final Position exit = getExit();
        final Queue<Position> queue = new ArrayDeque<>();
        final Set<Position> visited = new HashSet<>();
        queue.add(theFrom);
        visited.add(theFrom);

        while (!queue.isEmpty()) {
            final Position current = queue.poll();
            if (current.equals(exit)) {
                return true;
            }
            for (final Direction dir : Direction.values()) {
                final Position neighbor = current.translate(dir);
                if (!isInBounds(neighbor) || visited.contains(neighbor)) {
                    continue;
                }
                final Door door = getRoom(current).getDoor(dir);
                if (door != null && !door.isBlocked()) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return false;
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

    /**
     * Wires shared {@link Door} objects between every adjacent pair of
     * rooms in the grid, one question per door. Each door is created
     * with one question from {@code theQuestions} (in list order) and is
     * registered in both rooms it connects under opposite directions —
     * the SAME Door instance, so state changes (unlock/block) propagate
     * to both sides automatically.
     *
     * <p>Adjacent pairs are visited exactly once by iterating only the
     * {@code EAST} and {@code SOUTH} neighbors of each cell, which
     * together cover every horizontal and vertical edge of the grid
     * without duplication.
     *
     * <p>The required door count is
     * {@code (width - 1) * height + width * (height - 1)}. If
     * {@code theQuestions} has fewer entries than that, this method
     * throws before mutating any room.
     *
     * @param theQuestions the questions to assign to doors, in list order
     * @throws IllegalArgumentException if theQuestions is null or contains
     *         fewer entries than the maze has interior edges
     */
    public void fillRoomsWithQuestions(final List<Question> theQuestions) {
        if (theQuestions == null) {
            throw new IllegalArgumentException("Question list must not be null.");
        }
        final int required = (myWidth - 1) * myHeight + myWidth * (myHeight - 1);
        if (theQuestions.size() < required) {
            throw new IllegalArgumentException(
                    "Not enough questions to fill maze: need "
                            + required + ", got " + theQuestions.size() + ".");
        }

        final Direction[] forward = {Direction.EAST, Direction.SOUTH};
        int index = 0;
        for (int y = 0; y < myHeight; y++) {
            for (int x = 0; x < myWidth; x++) {
                final Room current = myRooms[y][x];
                final Position currentPos = new Position(x, y);
                for (final Direction dir : forward) {
                    final Position neighborPos = currentPos.translate(dir);
                    if (!isInBounds(neighborPos)) {
                        continue;
                    }
                    final Room neighborRoom = getRoom(neighborPos);
                    final Door door = new Door(theQuestions.get(index++));
                    current.addDoor(dir, door);
                    neighborRoom.addDoor(dir.getOpposite(), door);
                }
            }
        }
    }

    /**
     * Convenience overload of {@link #fillRoomsWithQuestions(List)} that
     * shuffles a defensive copy of {@code theQuestions} before wiring
     * doors. The caller's list is never modified.
     *
     * @param theQuestions the questions to assign to doors
     * @param theRandom the source of randomness used for shuffling
     * @throws IllegalArgumentException if either argument is null, or if
     *         theQuestions contains fewer entries than the maze requires
     */
    public void fillRoomsWithQuestions(final List<Question> theQuestions,
                                       final Random theRandom) {
        if (theQuestions == null) {
            throw new IllegalArgumentException("Question list must not be null.");
        }
        if (theRandom == null) {
            throw new IllegalArgumentException("Random must not be null.");
        }
        final List<Question> shuffled = new ArrayList<>(theQuestions);
        Collections.shuffle(shuffled, theRandom);
        fillRoomsWithQuestions(shuffled);
    }
}