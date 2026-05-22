package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.QuestionType;

public class DatabaseManager {

    private static transient Connection CONNECT;
    private static final String DB_PATH = "jdbc:sqlite:resource/trivia.db";

    public DatabaseManager() {}

    public static void connect() {
        try {
            CONNECT = DriverManager.getConnection(DB_PATH);
            System.out.println("QuestionFactory connected to: " + DB_PATH);
        } catch (SQLException e) {
            System.err.println("Error trying to establish a connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    public static ResultSet getAllQuestions() throws SQLException {
        final String sql = """
                SELECT *
                FROM questions
                ORDER BY id
                """;
        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        return stat.executeQuery();
    }

    public static ResultSet getAllQuestionsByCategory(final String theCategory) throws SQLException {
        if (theCategory == null || theCategory.equals("")) {
            throw new IllegalArgumentException("Category must not be null.");
        }

        final String sql = """
                SELECT * 
                FROM questions
                WHERE CATEGORY = ?
                ORDER BY RANDOM()
                LIMIT 1
                """;

        final PreparedStatement stat = CONNECT.prepareStatement(sql);
        stat.setString(1, theCategory);
        return stat.executeQuery();
    }

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
