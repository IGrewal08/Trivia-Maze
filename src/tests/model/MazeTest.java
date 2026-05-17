package tests.model;

import model.Position;
import org.junit.jupiter.api.Test;
import model.Maze;
import model.Room;

import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit 5 testing for Maze.
 *
 * Tests maze construction, dimension validation,
 * room retrieval behavior, and temporary path validation logic.
 * Ensures the maze correctly initializes rooms and rejects
 * invalid maze dimensions or positions.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class MazeTest {

    @Test
    void constructorCreatesMazeWithCorrectDimensions() {
        Maze maze = new Maze(8, 8);

        assertEquals(8, maze.getWidth());
        assertEquals(8, maze.getHeight());
    }

    @Test
    void constructorThrowsForInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Maze(0, 8));
        assertThrows(IllegalArgumentException.class, () -> new Maze(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Maze(-1, 8));
    }

    @Test
    void getRoomReturnsRoomAtPosition() {
        Maze maze = new Maze(8, 8);
        Room room = maze.getRoom(new Position(2, 3));

        assertNotNull(room);
    }

    @Test
    void getRoomThrowsForNullPosition() {
        Maze maze = new Maze(8, 8);

        assertThrows(IllegalArgumentException.class,
                () -> maze.getRoom(null));
    }

    @Test
    void pathAvailableCurrentlyReturnsTrue() {
        Maze maze = new Maze(8, 8);

        assertTrue(maze.isPathAvailable(new Position(0, 0)));
    }
}
