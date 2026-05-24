package tests.model;

import model.Direction;
import model.Door;
import model.Maze;
import model.MazeValidator;
import model.Position;
import model.Question;
import model.ShortAnswerQuestion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for MazeValidator.
 *
 * These tests focus on maze setup safety: complete door wiring, reciprocal
 * shared Door objects, and exit reachability after doors are blocked.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class MazeValidatorTest {

    @Test
    void requiredDoorCountMatchesRectangularGridEdges() {
        final Maze maze = new Maze(3, 2);

        assertEquals(7, MazeValidator.getRequiredDoorCount(maze));
    }

    @Test
    void filledMazeHasCompleteDoorWiring() {
        final Maze maze = new Maze(2, 2);
        maze.fillRoomsWithQuestions(createQuestions(
                MazeValidator.getRequiredDoorCount(maze)));

        assertTrue(MazeValidator.hasCompleteDoorWiring(maze));
        assertTrue(MazeValidator.findWiringProblems(maze).isEmpty());
    }

    @Test
    void emptyMazeReportsMissingDoors() {
        final Maze maze = new Maze(2, 2);

        assertFalse(MazeValidator.hasCompleteDoorWiring(maze));
        assertFalse(MazeValidator.findWiringProblems(maze).isEmpty());
    }

    @Test
    void oneWayDoorReportsReciprocalWiringProblem() {
        final Maze maze = new Maze(2, 1);
        final Door oneWayDoor = new Door(createQuestion(1));
        maze.getRoom(new Position(0, 0)).addDoor(Direction.EAST, oneWayDoor);

        assertFalse(MazeValidator.hasCompleteDoorWiring(maze));
    }

    @Test
    void filledMazeHasReachableExit() {
        final Maze maze = new Maze(2, 2);
        maze.fillRoomsWithQuestions(createQuestions(
                MazeValidator.getRequiredDoorCount(maze)));

        assertTrue(MazeValidator.hasReachableExit(maze));
    }

    @Test
    void blockedEntranceDoorsMakeExitUnreachable() {
        final Maze maze = new Maze(2, 2);
        maze.fillRoomsWithQuestions(createQuestions(
                MazeValidator.getRequiredDoorCount(maze)));

        maze.getRoom(maze.getEntrance()).getDoor(Direction.EAST).block();
        maze.getRoom(maze.getEntrance()).getDoor(Direction.SOUTH).block();

        assertFalse(MazeValidator.hasReachableExit(maze));
    }

    private static List<Question> createQuestions(final int theCount) {
        final List<Question> questions = new ArrayList<>();
        for (int i = 0; i < theCount; i++) {
            questions.add(createQuestion(i + 1));
        }
        return questions;
    }

    private static Question createQuestion(final int theId) {
        return new ShortAnswerQuestion(theId, "Question " + theId, 1, "answer");
    }
}
