package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(1, 2);
    }

    @Test
    void testConstructorSetsCoordinates() {
        assertEquals(1, room.getX());
        assertEquals(2, room.getY());
    }

    @Test
    void testRoomStartsUnvisited() {
        assertFalse(room.isVisited());
    }

    @Test
    void testSetVisitedTrue() {
        room.setVisited(true);
        assertTrue(room.isVisited());
    }

    @Test
    void testSetVisitedFalse() {
        room.setVisited(true);
        room.setVisited(false);
        assertFalse(room.isVisited());
    }

    @Test
    void testConstructorRejectsNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> new Room(-1, 2));
    }

    @Test
    void testConstructorRejectsNegativeY() {
        assertThrows(IllegalArgumentException.class, () -> new Room(1, -2));
    }

    @Test
    void testHasDoorRejectsNullDirection() {
        assertThrows(IllegalArgumentException.class, () -> room.hasDoor(null));
    }

    @Test
    void testGetDoorRejectsNullDirection() {
        assertThrows(IllegalArgumentException.class, () -> room.getDoor(null));
    }

    @Test
    void testAddDoorRejectsNullDirection() {
        // TODO: replace null Door once Door constructor is confirmed
        assertThrows(IllegalArgumentException.class, () -> room.addDoor(null, null));
    }

    @Test
    void testToStringReturnsRoomState() {
        // TODO: update once doors map is initialized
        assertNotNull(room.toString());
    }
}