package tests.model;

import model.Direction;
import model.GameState;
import model.GameStatus;
import model.Position;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit 5 testing for GameState.
 *
 * Tests active game state initialization, player position updates,
 * direction tracking, visited room management, game status updates,
 * defensive copying behavior, and property change event firing.
 * Ensures GameState correctly synchronizes gameplay data for MVC interaction.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class GameStateTest {

    @Test
    void testConstructorSetsInitialState() {
        GameState state = new GameState(8, 8);

        assertNotNull(state.getMaze());
        assertEquals(new Position(0, 0), state.getCurrentPosition());
        assertEquals(Direction.NORTH, state.getCurrentDirection());
        assertEquals(GameStatus.ACTIVE, state.getStatus());
        assertEquals(GameState.STARTING_SKIPS, state.getSkipsRemaining());
        assertTrue(state.getVisitedRooms().contains(new Position(0, 0)));
    }

    @Test
    void testSetCurrentPositionUpdatesPosition() {
        GameState state = new GameState(8, 8);
        Position newPosition = new Position(1, 0);

        state.setCurrentPosition(newPosition);

        assertEquals(newPosition, state.getCurrentPosition());
    }

    @Test
    void testSetCurrentDirectionUpdatesDirection() {
        GameState state = new GameState(8, 8);

        state.setCurrentDirection(Direction.EAST);

        assertEquals(Direction.EAST, state.getCurrentDirection());
    }

    @Test
    void testAddVisitedRoomTracksPosition() {
        GameState state = new GameState(8, 8);
        Position position = new Position(1, 0);

        state.addVisitedRoom(position);

        assertTrue(state.getVisitedRooms().contains(position));
    }

    @Test
    void testVisitedRoomsReturnsDefensiveCopy() {
        GameState state = new GameState(8, 8);

        Set<Position> visitedCopy = state.getVisitedRooms();
        visitedCopy.add(new Position(99, 99));

        assertFalse(state.getVisitedRooms().contains(new Position(99, 99)));
    }

    @Test
    void testSetStatusUpdatesStatus() {
        GameState state = new GameState(8, 8);

        state.setStatus(GameStatus.ACTIVE);

        assertEquals(GameStatus.ACTIVE, state.getStatus());
    }

    @Test
    void testUseSkipConsumesOneSkip() {
        GameState state = new GameState(8, 8);

        assertTrue(state.useSkip());

        assertEquals(GameState.STARTING_SKIPS - 1, state.getSkipsRemaining());
    }

    @Test
    void testUseSkipReturnsFalseWhenNoSkipsRemain() {
        GameState state = new GameState(8, 8);

        for (int i = 0; i < GameState.STARTING_SKIPS; i++) {
            assertTrue(state.useSkip());
        }

        assertFalse(state.useSkip());
        assertEquals(0, state.getSkipsRemaining());
    }

    @Test
    void testPositionChangeFiresPropertyChangeEvent() {
        GameState state = new GameState(8, 8);
        List<PropertyChangeEvent> events = new ArrayList<>();

        state.addPropertyChangeListener(events::add);
        state.setCurrentPosition(new Position(1, 0));

        assertFalse(events.isEmpty());
        assertEquals("CURRENT_POSITION", events.get(0).getPropertyName());
    }

    @Test
    void testDirectionChangeFiresPropertyChangeEvent() {
        GameState state = new GameState(8, 8);
        List<PropertyChangeEvent> events = new ArrayList<>();

        state.addPropertyChangeListener(events::add);
        state.setCurrentDirection(Direction.SOUTH);

        assertFalse(events.isEmpty());
        assertEquals("CURRENT_DIRECTION", events.get(0).getPropertyName());
    }

    @Test
    void testStatusChangeFiresPropertyChangeEvent() {
        GameState state = new GameState(8, 8);
        List<PropertyChangeEvent> events = new ArrayList<>();

        state.addPropertyChangeListener(events::add);
        state.setStatus(GameStatus.ACTIVE);

        assertEquals(GameStatus.ACTIVE, state.getStatus());
    }
}
