package controller;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.*;

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

        testState = new GameState() {
            // Await implementation of this class
        }

        testView = new GameView() {
            // Await implementation of this class
        }

        testController = new GameController(testState, testView);
    }

    // ~~~ Constructor tests ~~~
    @Test
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, 
            () -> new GameControllerTest(null, testView),
                "Should throw IllegalArgumentException on null input.");
        assertThrows(IllegalArgumentException.class, 
            () -> new GameController(testState, null),
                "Should throw IllegalArgumentException on null input.");
    }

    // ~~~ handleMove tests ~~~
    @Test
    void testHandleMoveNullInput() {
        assertThrows(IllegalArgumentException.class, 
            () -> testController.handleMove(null),
                "Should throw IllegalArgumentException on null input.");
    }

    // ~~~ handleAnswer tests ~~~
    void testHandleMoveThrowIllegalArgumentOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testController.handleAnswer(""));
    }

    // ~~~ saveGame tests ~~~
    @Test
    void testSaveGameThrowIllegalArgumentOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testController.saveGame(""));
    }

    // ~~~ exitGame tests ~~~
    @Test
    void testExitGameClose() {
        testController.exitGame();
        verify(testView).closeWindow();
    }
}
