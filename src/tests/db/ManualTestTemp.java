package tests.db;

import model.GameState;
import model.GameStatus;
import model.Direction;
import model.Position;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Temporary manual test class for checking database and GameState setup.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class ManualTestTemp {

    public static void main(final String[] theArgs) {
        testDatabase();
        testGameState();
    }

    private static void testDatabase() {
        System.out.println("=== Database Test ===");

        String url = "jdbc:sqlite:./resources/trivia.db";

        try (Connection con = DriverManager.getConnection(url);
             Statement stat = con.createStatement()) {

            ResultSet countResult =
                    stat.executeQuery("SELECT COUNT(*) AS total FROM questions");

            if (countResult.next()) {
                System.out.println("Question count: "
                        + countResult.getInt("total"));
            }

            ResultSet questionResult =
                    stat.executeQuery("SELECT * FROM questions LIMIT 1");

            if (questionResult.next()) {
                System.out.println("Sample question: "
                        + questionResult.getString("text"));
                System.out.println("Answer: "
                        + questionResult.getString("answer"));
                System.out.println("Type: "
                        + questionResult.getString("type"));
            }

            System.out.println("Database test passed.\n");

        } catch (Exception e) {
            System.out.println("Database test failed.");
            e.printStackTrace();
        }
    }

    private static void testGameState() {
        System.out.println("=== GameState Test ===");

        try {
            GameState state = new GameState(8, 8);

            System.out.println("Maze created: " + state.getMaze());
            System.out.println("Current position: "
                    + state.getCurrentPosition());
            System.out.println("Current direction: "
                    + state.getCurrentDirection());
            System.out.println("Status: " + state.getStatus());
            System.out.println("Visited rooms: "
                    + state.getVisitedRooms());

            state.setCurrentDirection(Direction.EAST);
            state.setCurrentPosition(new Position(1, 0));
            state.addVisitedRoom(new Position(1, 0));
            state.setStatus(GameStatus.ACTIVE);

            System.out.println("Updated position: "
                    + state.getCurrentPosition());
            System.out.println("Updated direction: "
                    + state.getCurrentDirection());
            System.out.println("Updated visited rooms: "
                    + state.getVisitedRooms());

            System.out.println("GameState test passed.\n");

        } catch (Exception e) {
            System.out.println("GameState test failed.");
            e.printStackTrace();
        }
    }
}