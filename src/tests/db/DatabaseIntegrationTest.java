package tests.db;

import db.Schema;
import model.Direction;
import model.GameState;
import model.GameStatus;
import model.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test for DatabaseManager
 * 
 * Tests question building, queries, and hint system for correctness.
 * 
 * @author Nicolas Cortes
 * @version 1.0
 */
public class DatabaseIntegrationTest {

    private static final String URL = "jdbc:sqlite:resources/trivia.db";

    @BeforeAll
    static void initializeSchema() {
        Schema.initialize();
    }

    @Test
    void testDatabaseConnects() throws Exception {
        try (Connection con = DriverManager.getConnection(URL)) {
            assertNotNull(con);
            assertFalse(con.isClosed());
        }
    }

    @Test
    void testQuestionsTableHasSeededQuestions() throws Exception {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stat = con.createStatement();
             ResultSet result = stat.executeQuery(
                     "SELECT COUNT(*) AS total FROM questions")) {

            assertTrue(result.next());
            assertEquals(63, result.getInt("total"));
        }
    }

    @Test
    void testFirstQuestionHasRequiredFields() throws Exception {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stat = con.createStatement();
             ResultSet result = stat.executeQuery(
                     "SELECT text, answer, type, hint FROM questions LIMIT 1")) {

            assertTrue(result.next());
            assertNotNull(result.getString("text"));
            assertNotNull(result.getString("answer"));
            assertNotNull(result.getString("type"));
            assertNotNull(result.getString("hint"));
        }
    }

    @Test
    void testQuestionsTableHasHintColumn() throws Exception {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stat = con.createStatement();
             ResultSet result = stat.executeQuery("PRAGMA table_info(questions)")) {

            boolean found = false;
            while (result.next()) {
                found = found || "hint".equalsIgnoreCase(result.getString("name"));
            }
            assertTrue(found);
        }
    }

    @Test
    void testSeededQuestionsHaveHints() throws Exception {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stat = con.createStatement();
             ResultSet result = stat.executeQuery(
                     "SELECT COUNT(*) AS missing FROM questions "
                             + "WHERE hint IS NULL OR TRIM(hint) = ''")) {

            assertTrue(result.next());
            assertEquals(0, result.getInt("missing"));
        }
    }

    @Test
    void testKnownQuestionHasCorrectAnswer() throws Exception {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stat = con.createStatement();
             ResultSet result = stat.executeQuery(
                     "SELECT answer FROM questions " +
                             "WHERE text = 'How many elements are on the periodic table?'")) {

            assertTrue(result.next());
            assertEquals("118", result.getString("answer"));
        }
    }

    @Test
    void testGameStateInitializesCorrectly() {
        GameState state = new GameState(8, 8);

        assertNotNull(state.getMaze());
        assertEquals(new Position(0, 0), state.getCurrentPosition());
        assertEquals(Direction.NORTH, state.getCurrentDirection());
        assertEquals(GameStatus.ACTIVE, state.getStatus());
        assertTrue(state.getVisitedRooms().contains(new Position(0, 0)));
    }

    @Test
    void testGameStateUpdatesCorrectly() {
        GameState state = new GameState(8, 8);
        Position newPosition = new Position(1, 0);

        state.setCurrentDirection(Direction.EAST);
        state.setCurrentPosition(newPosition);
        state.addVisitedRoom(newPosition);
        state.setStatus(GameStatus.ACTIVE);

        assertEquals(Direction.EAST, state.getCurrentDirection());
        assertEquals(newPosition, state.getCurrentPosition());
        assertTrue(state.getVisitedRooms().contains(newPosition));
        assertEquals(GameStatus.ACTIVE, state.getStatus());
    }
}
