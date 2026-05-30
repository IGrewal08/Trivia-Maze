package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.QuestionType;

/**
 * DatabaseManager handles all SQLite queries for the Trivia Maze application.
 * Returns ResultSet objects, keeping database access decoupled from questions construction.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class DatabaseManager {

    /** Activates JDBC connection to the SQLite db */
    private static transient Connection CONNECT;
    /** Path to the SQLite database relative to project root. */
    private static final String DB_PATH = "jdbc:sqlite:resources/trivia.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found! Make sure the JAR is on the classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Opens a JDBC connection to the SQLite database at path.
     * @throws SQLException if a connection cannot be established.
     */
    public static void connect() {
        try {
            CONNECT = DriverManager.getConnection(DB_PATH);
            System.out.println("QuestionFactory connected to: " + DB_PATH);
        } catch (SQLException e) {
            System.err.println("Error trying to establish a connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns DB connection string.
     * Intended for Schema to create schema for database at a selected location.
     * @return db path string
     */
    protected static String getDBPath() {
        return DB_PATH;
    }

    /**
     * Close the active JDBC connection, call when the application exits.
     */
    public static void disconnect() {
        try {
            if (CONNECT != null && !CONNECT.isClosed()) {
                CONNECT.close();
                System.out.println("QuestionFactory closed.");
            }

        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the question row with the given primary key ID.
     * 
     * @param theId the primary key of the question to retrieve.
     * @return a ResultSet positioned before the first row.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getQuestionById(final int theId) throws SQLException {
        final String sql = """
                SELECT *
                FROM questions
                WHERE id = ?
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setInt(1, theId);
        return stat.executeQuery();

    }

    /**
     * Returns all rows from the questions table with no filters applied.
     * 
     * @return a ResultSet containing every row in the questions table.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getAllQuestions() throws SQLException {
        final String sql = """
                SELECT *
                FROM questions
                ORDER BY id
                """;
        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        return stat.executeQuery();
    }

    /**
     * Returns all question rows that match the given category.
     *
     * @param theCategory the category to filter by.
     * @return a ResultSet containing all questions in the category.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getAllQuestionsByCategory(final String theCategory) throws SQLException {
        if (theCategory == null || theCategory.equals("")) {
            throw new IllegalArgumentException("Category must not be null.");
        }

        final String sql = """
                SELECT *
                FROM questions
                WHERE category = ?
                ORDER BY id
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setString(1, theCategory);
        return stat.executeQuery();
    }

    /**
     * Returns a single randomly-selected question row matching the given category.
     *
     * @param theCategory the category to filter by.
     * @return a ResultSet positioned before a single row, or empty if no match.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getRandomQuestionByCategory(final String theCategory) throws SQLException {
        if (theCategory == null || theCategory.equals("")) {
            throw new IllegalArgumentException("Category must not be null.");
        }

        final String sql = """
                SELECT *
                FROM questions
                WHERE category = ?
                ORDER BY RANDOM()
                LIMIT 1
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setString(1, theCategory);
        return stat.executeQuery();
    }

    /**
     * Returns a single randomly-selected question row matching the given type.
     *
     * @param theType the question type to filter by.
     * @return a ResultSet positioned before a single row, or empty if no match.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getRandomQuestionByType(final QuestionType theType) throws SQLException {
        if (theType == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        final String sql = """
                SELECT *
                FROM questions
                WHERE type = ?
                ORDER BY RANDOM()
                LIMIT 1
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setString(1, theType.name());
        return stat.executeQuery();
    }

    /**
     * Returns all question rows that match the given question type.
     * 
     * @param theType the question type to filter by.
     * @return a ResultSet containing all questions of that type.
     * @throws SQLException if the query fails.
     */
    public static ResultSet getAllQuestionsByType(final QuestionType theType) throws SQLException {
        if (theType == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        final String sql = """
                SELECT *
                FROM questions
                WHERE type = ?
                ORDER BY id
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setString(1, theType.name());
        return stat.executeQuery();
    }

    /**
     * Returns a list of all categories names in the database.
     * 
     * @return a list of category name strings.
     * @throws SQLException if the query fails.
     */
    public static List<String> getCategories() throws SQLException {
        final String sql = """
                SELECT DISTINCT category
                FROM questions
                ORDER BY category
                """;

        final List<String> categories = new ArrayList<>();

        try (PreparedStatement stat = CONNECT.prepareStatement(sql);
            ResultSet rs = stat.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
            }
        return categories;
    }
}
