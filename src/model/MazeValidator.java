package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for checking whether a Maze is ready for play.
 *
 * These checks are intentionally separate from Maze so they can be used by
 * tests, game setup, or future save/load validation without changing the core
 * model behavior.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public final class MazeValidator {

    /**
     * Utility class; do not instantiate.
     */
    private MazeValidator() {
    }

    /**
     * Calculates the number of interior doors needed to connect every
     * adjacent room pair in a rectangular maze.
     *
     * @param theMaze maze to inspect
     * @return required number of shared doors
     * @throws IllegalArgumentException if theMaze is null
     */
    public static int getRequiredDoorCount(final Maze theMaze) {
        if (theMaze == null) {
            throw new IllegalArgumentException("Maze must not be null.");
        }

        return (theMaze.getWidth() - 1) * theMaze.getHeight()
                + theMaze.getWidth() * (theMaze.getHeight() - 1);
    }

    /**
     * Returns whether every adjacent room pair has the same Door object wired
     * from both sides, and no room has a door pointing outside the maze.
     *
     * @param theMaze maze to inspect
     * @return true if maze door wiring is complete and reciprocal
     */
    public static boolean hasCompleteDoorWiring(final Maze theMaze) {
        return findWiringProblems(theMaze).isEmpty();
    }

    /**
     * Returns whether the exit is reachable from the maze entrance through
     * doors that are not permanently blocked.
     *
     * @param theMaze maze to inspect
     * @return true if the exit can still be reached
     * @throws IllegalArgumentException if theMaze is null
     */
    public static boolean hasReachableExit(final Maze theMaze) {
        if (theMaze == null) {
            throw new IllegalArgumentException("Maze must not be null.");
        }

        return theMaze.isPathPossible(theMaze.getEntrance());
    }

    /**
     * Returns human-readable wiring problems found in the maze. An empty list
     * means the maze has complete, reciprocal door wiring.
     *
     * @param theMaze maze to inspect
     * @return list of wiring problem descriptions
     * @throws IllegalArgumentException if theMaze is null
     */
    public static List<String> findWiringProblems(final Maze theMaze) {
        if (theMaze == null) {
            throw new IllegalArgumentException("Maze must not be null.");
        }

        final List<String> problems = new ArrayList<>();

        for (int y = 0; y < theMaze.getHeight(); y++) {
            for (int x = 0; x < theMaze.getWidth(); x++) {
                final Position currentPosition = new Position(x, y);
                final Room currentRoom = theMaze.getRoom(currentPosition);
                checkRoomDoors(theMaze, currentPosition, currentRoom, problems);
            }
        }

        return problems;
    }

    /**
     * Checks all four directions for one room.
     *
     * @param theMaze maze being inspected
     * @param thePosition current room position
     * @param theRoom current room
     * @param theProblems mutable list of problems found so far
     */
    private static void checkRoomDoors(final Maze theMaze,
                                       final Position thePosition,
                                       final Room theRoom,
                                       final List<String> theProblems) {
        for (final Direction direction : Direction.values()) {
            final Position neighborPosition = thePosition.translate(direction);

            if (!theMaze.isInBounds(neighborPosition)) {
                if (theRoom.hasDoor(direction)) {
                    theProblems.add("Door at " + thePosition + " points outside maze "
                            + "toward " + direction + ".");
                }
                continue;
            }

            final Door door = theRoom.getDoor(direction);
            if (door == null) {
                theProblems.add("Missing door from " + thePosition
                        + " toward " + direction + ".");
                continue;
            }

            final Room neighborRoom = theMaze.getRoom(neighborPosition);
            final Door oppositeDoor = neighborRoom.getDoor(direction.getOpposite());

            if (oppositeDoor == null) {
                theProblems.add("Neighbor at " + neighborPosition
                        + " is missing reciprocal door toward "
                        + direction.getOpposite() + ".");
            } else if (door != oppositeDoor) {
                theProblems.add("Rooms at " + thePosition + " and "
                        + neighborPosition + " do not share the same Door object.");
            }
        }
    }
}
