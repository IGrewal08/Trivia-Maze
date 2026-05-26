package tests.controller;

import controller.GameController;
import model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GuiView;
import view.GameView;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit 5 testing for GameController
 * 
 * All test are written as methods and grouped my method concerns for GameController.
 * Implemented inner GameState and GameView to keep modularity and promote loose coupling.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class GameControllerTest {

    private GameController testController;
    private GameState testState;
    private GameView testView;

    @BeforeEach
    void setUp() {
        testState = new GameState(5, 5);
        testView = new GuiView();
        testController = new GameController(testState, testView);
    }

    @Test
    void testConstructorThrowsOnNullState() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameController(null, testView));
    }

    @Test
    void testConstructorThrowsOnNullView() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameController(testState, null));
    }

    @Test
    void testHandleMoveThrowsOnNullDirection() {
        assertThrows(IllegalArgumentException.class,
                () -> testController.handleMove(null));
    }

    @Test
    void testHandleAnswerThrowsOnEmptyAnswer() {
        assertThrows(IllegalArgumentException.class,
                () -> testController.handleAnswer(""));
    }

    @Test
    void testSaveGameThrowsOnEmptyFileName() {
        assertThrows(IllegalArgumentException.class,
                () -> testController.saveGame(""));
    }
}
