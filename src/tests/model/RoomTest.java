package tests.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test for Room
 * 
 * Tests the Rooms constructor for valid input and error catching, Door method for add/get/ and has
 * checks and validates visited status of the door in this room, checks toString method for correct output.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
class RoomTest {

    private Room myRoom;
    private Door myDoor;

    public static Door makeDoor() {
        return new Door(new ShortAnswerQuestion(1, "What is 2+2?", 1, "4"));
    }

    @BeforeEach
    void setUp() {
        myRoom = new Room(2, 3);
        myDoor = makeDoor();
    }

    // ~~~ Constructor ~~~

    @Test
    void constructor_validCoordinates_setsXAndY() {
        assertEquals(2, myRoom.getX());
        assertEquals(3, myRoom.getY());
    }

    @Test
    void constructor_newRoom_isNotVisited() {
        assertFalse(myRoom.isVisited());
    }

    @Test
    void constructor_newRoom_hasNoDoors() {
        for (Direction dir : Direction.values()) {
            assertFalse(myRoom.hasDoor(dir));
        }
    }

    @Test
    void constructor_negativeX_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Room(-1, 0));
    }

    @Test
    void constructor_negativeY_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Room(0, -1));
    }

    @Test
    void constructor_zeroCoordinates_isAllowed() {
        assertDoesNotThrow(() -> new Room(0, 0));
    }

    // ~~~ addDoor/getDoor/hasDoor ~~~

    @Test
    void addDoor_validDirAndDoor_doorIsRetrievable() {
        myRoom.addDoor(Direction.NORTH, myDoor);
        assertSame(myDoor, myRoom.getDoor(Direction.NORTH));
    }

    @Test
    void addDoor_nullDirection_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> myRoom.addDoor(null, myDoor));
    }

    @Test
    void addDoor_nullDoor_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> myRoom.addDoor(Direction.NORTH, null));
    }

    @Test
    void addDoor_overwriteExistingDirection_replacesOldDoor() {
        Door anotherDoor = makeDoor();
        myRoom.addDoor(Direction.NORTH, myDoor);
        myRoom.addDoor(Direction.NORTH, anotherDoor);
        assertSame(anotherDoor, myRoom.getDoor(Direction.NORTH));
    }

    @Test
    void getDoor_directionWithNoDoor_returnsNull() {
        assertNull(myRoom.getDoor(Direction.SOUTH));
    }

    @Test
    void getDoor_nullDirection_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> myRoom.getDoor(null));
    }

    @Test
    void hasDoor_afterAddingDoor_returnsTrue() {
        myRoom.addDoor(Direction.EAST, myDoor);
        assertTrue(myRoom.hasDoor(Direction.EAST));
    }

    @Test
    void hasDoor_directionWithNoDoor_returnsFalse() {
        assertFalse(myRoom.hasDoor(Direction.WEST));
    }

    @Test
    void hasDoor_nullDirection_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> myRoom.hasDoor(null));
    }

    // ~~~ setVisited/isVisited ~~~

    @Test
    void setVisited_true_roomBecomesVisited() {
        myRoom.setVisited(true);
        assertTrue(myRoom.isVisited());
    }

    @Test
    void setVisited_falseAfterTrue_roomBecomesUnvisited() {
        myRoom.setVisited(true);
        myRoom.setVisited(false);
        assertFalse(myRoom.isVisited());
    }

    // ~~~ toString ~~~

    @Test
    void toString_containsCoordinatesAndVisitedState() {
        String result = myRoom.toString();
        assertTrue(result.contains("X=2"));
        assertTrue(result.contains("Y=3"));
        assertTrue(result.contains("visited=false"));
    }
}