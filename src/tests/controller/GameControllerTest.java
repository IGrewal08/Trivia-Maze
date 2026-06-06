package tests.controller;

import controller.GameController;
import model.Direction;
import model.Door;
import model.GameState;
import model.Position;
import model.Question;
import model.Room;
import model.ShortAnswerQuestion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameView;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 testing for GameController
 * 
 * Tests GameController constructor, position methods, maze methods, room visited status
 * movement method for handling player movement, handling player answers, possible directions the
 * player can move. New and save, and load game method.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class GameControllerTest {

    // ~~~ Setup for dependencies ~~~

    /**
     * Inner view class to test method related to updating the view from the controller
     */
    private static class View implements GameView {
        String lastMessage = "";
        Question lastQuestion = null;
        boolean closed = false;

        @Override public void initialize() {}
        @Override public void updateView() {}
        @Override public void setController(GameController c) {}
        @Override public void closeGame() { closed = true; }
        @Override public void propertyChange(PropertyChangeEvent e) {}

        @Override
        public void showMessage(final String theMessage) {
            lastMessage = theMessage;
        }

        @Override
        public void showQuestions(final Question theQuestion) {
            lastQuestion = theQuestion;
        }
    }

    /** helper method to make question for testing */
    private static Question makeQuestion() {
        return new ShortAnswerQuestion(1, "What is 2+2?", 1, "4");
    }

    /**
     * Builds a filled 2x2 GameState so every room has real doors.
     * A 2x2 maze needs (2-1)*2 + 2*(2-1) = 4 questions.
     */
    private static GameState makeFilledState() {
        GameState state = new GameState(2, 2);
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            questions.add(makeQuestion());
        }
        state.getMaze().fillRoomsWithQuestions(questions);
        return state;
    }

    private View myView;
    private GameState myState;
    private GameController myController;

    @BeforeEach
    void setUp() {
        myView = new View();
        myState = makeFilledState();
        myController = new GameController(myState, myView);
    }

    // ~~~ Constructor ~~~
    @Test
    void constructor_nullState_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new GameController(null, myView));
    }

    @Test
    void constructor_nullView_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new GameController(myState, null));
    }

    // ~~~ getCurrentPosition ~~~
    @Test
    void getCurrentPosition_returnsEntranceAtStart() {
        assertEquals(new Position(0, 0), myController.getCurrentPosition());
    }

    // ~~~ getCurrentRoom ~~~
    @Test
    void getCurrentRoom_returnsRoomAtCurrentPosition() {
        Room expected = myState.getMaze().getRoom(new Position(0, 0));
        assertSame(expected, myController.getCurrentRoom());
    }

    // ~~~ getMaze ~~~
    @Test
    void getMaze_returnsMazeFromState() {
        assertSame(myState.getMaze(), myController.getMaze());
    }

    // ~~~ getVisitedRooms ~~~
    @Test
    void getVisitedRooms_atStart_containsEntrance() {
        assertTrue(myController.getVisitedRooms().contains(new Position(0, 0)));
    }

    // ~~~ handleMove ~~~
    @Test
    void handleMove_nullDirection_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.handleMove(null));
    }

    @Test
    void handleMove_outOfBounds_showsMessage() {
        // (0,0) has no NORTH or WEST neighbor
        myController.handleMove(Direction.NORTH);
        assertTrue(myView.lastMessage.contains("outside the maze")
                || myView.lastMessage.toLowerCase().contains("cannot move"));
    }

    @Test
    void handleMove_lockedDoor_showsQuestion() {
        // All doors start locked — moving EAST from (0,0) should prompt a question
        myController.handleMove(Direction.EAST);
        assertNotNull(myView.lastQuestion);
    }

    @Test
    void handleMove_lockedDoor_positionDoesNotChange() {
        myController.handleMove(Direction.EAST);
        assertEquals(new Position(0, 0), myController.getCurrentPosition());
    }

    @Test
    void handleMove_openDoor_positionChanges() {
        // Manually unlock the EAST door then move
        Room startRoom = myState.getMaze().getRoom(new Position(0, 0));
        startRoom.getDoor(Direction.EAST).unlock();
        myController.handleMove(Direction.EAST);
        assertEquals(new Position(1, 0), myController.getCurrentPosition());
    }

    @Test
    void handleMove_blockedDoor_showsBlockedMessage() {
        Room startRoom = myState.getMaze().getRoom(new Position(0, 0));
        startRoom.getDoor(Direction.EAST).block();
        myController.handleMove(Direction.EAST);
        assertTrue(myView.lastMessage.toLowerCase().contains("blocked"));
    }

    @Test
    void handleMove_blockedDoor_positionDoesNotChange() {
        Room startRoom = myState.getMaze().getRoom(new Position(0, 0));
        startRoom.getDoor(Direction.EAST).block();
        myController.handleMove(Direction.EAST);
        assertEquals(new Position(0, 0), myController.getCurrentPosition());
    }

    // ~~~ handleAnswer ~~~
    @Test
    void handleAnswer_nullAnswer_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.handleAnswer(null));
    }

    @Test
    void handleAnswer_blankAnswer_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.handleAnswer("   "));
    }

    @Test
    void handleAnswer_correctAnswer_positionChanges() {
        // Trigger the question first so direction is set
        myController.handleMove(Direction.EAST);
        myController.handleAnswer("4");
        assertEquals(new Position(1, 0), myController.getCurrentPosition());
    }

    @Test
    void handleAnswer_wrongAnswer_positionDoesNotChange() {
        myController.handleMove(Direction.EAST);
        myController.handleAnswer("wrong");
        assertEquals(new Position(0, 0), myController.getCurrentPosition());
    }

    @Test
    void handleAnswer_wrongAnswer_doorBecomesBlocked() {
        myController.handleMove(Direction.EAST);
        myController.handleAnswer("wrong");
        Door door = myState.getMaze().getRoom(new Position(0, 0)).getDoor(Direction.EAST);
        assertTrue(door.isBlocked());
    }

    // ~~~ isDirectionAvailable ~~~
    @Test
    void isDirectionAvailable_lockedDoor_returnsTrue() {
        // Locked but not blocked — player can still attempt it
        assertTrue(myController.isDirectionAvailable(Direction.EAST));
    }

    @Test
    void isDirectionAvailable_blockedDoor_returnsFalse() {
        myState.getMaze().getRoom(new Position(0, 0)).getDoor(Direction.EAST).block();
        assertFalse(myController.isDirectionAvailable(Direction.EAST));
    }

    @Test
    void isDirectionAvailable_noDoor_returnsFalse() {
        // NORTH from (0,0) has no door — out of bounds
        assertFalse(myController.isDirectionAvailable(Direction.NORTH));
    }

    // ~~~ newGame ~~~
    @Test
    void newGame_negativeDimensions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.newGame(-1, 4));
    }

    // ~~~ saveGame ~~~
    @Test
    void saveGame_blankFileName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.saveGame("   "));
    }

    // ~~~ loadGame ~~~
    @Test
    void loadGame_nullFileName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.loadGame(null));
    }

    @Test
    void loadGame_blankFileName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> myController.loadGame("   "));
    }
}
