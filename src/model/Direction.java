package model;

/**
 * Enumeration of the four cardinal directions a player can travel in the Trivia Maze.
 * Each direction has an opposite, used for connecting adjacent rooms via shared doors.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public enum Direction {

    /** North direction (decreases Y coordinate). */
    NORTH,
    /** South direction (increases Y coordinate). */
    SOUTH,
    /** East direction (increases X coordinate). */
    EAST,
    /** West direction (decreases X coordinate). */
    WEST;

    /**
     * Returns the direction opposite to this one.
     * NORTH ↔ SOUTH, EAST ↔ WEST.
     *
     * @return the opposite Direction
     */
    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}

