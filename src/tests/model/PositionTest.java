package tests.model;
import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test for Position
 * 
 * Tests for constructor to create a position for the maze, translation to for
 * checking player position after a movement (position update), equals method to check if the
 * player is in a room, and to string method to validate correct current position.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class PositionTest {
    
    // ~~~ constructor ~~~
    @Test
    void constructor_storesCoordinates() {
        Position p = new Position(3, 7);
        assertEquals(3, p.getX());
        assertEquals(7, p.getY());
    }

    @Test
    void constructor_negativeCoordinates_areAllowed() {
        assertDoesNotThrow(() -> new Position(-1, -1));
    }

    // ~~~ translate ~~~
    @Test
    void translate_north_decreasesY() {
        Position p = new Position(2, 2).translate(Direction.NORTH);
        assertEquals(new Position(2, 1), p);
    }

    @Test
    void translate_south_increasesY() {
        Position p = new Position(2, 2).translate(Direction.SOUTH);
        assertEquals(new Position(2, 3), p);
    }

    @Test
    void translate_east_increasesX() {
        Position p = new Position(2, 2).translate(Direction.EAST);
        assertEquals(new Position(3, 2), p);
    }

    @Test
    void translate_west_decreasesX() {
        Position p = new Position(2, 2).translate(Direction.WEST);
        assertEquals(new Position(1, 2), p);
    }

    @Test
    void translate_doesNotMutateOriginal() {
        Position original = new Position(2, 2);
        original.translate(Direction.NORTH);
        assertEquals(new Position(2, 2), original);
    }

    // ~~~ equals/hashCode ~~~

    @Test
    void equals_sameCoordinates_returnsTrue() {
        assertEquals(new Position(1, 2), new Position(1, 2));
    }

    @Test
    void equals_differentCoordinates_returnsFalse() {
        assertNotEquals(new Position(1, 2), new Position(3, 4));
    }

    @Test
    void equals_null_returnsFalse() {
        assertNotEquals(null, new Position(1, 2));
    }

    @Test
    void hashCode_equalPositions_haveSameHashCode() {
        assertEquals(new Position(1, 2).hashCode(), new Position(1, 2).hashCode());
    }

    // ~~~ toString ~~~

    @Test
    void toString_returnsExpectedFormat() {
        assertEquals("(3, 7)", new Position(3, 7).toString());
    }
}
