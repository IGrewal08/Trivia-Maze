package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Schema {
    
    private static final String CONNECTION_URL = "jdbc:sqlite:resources/trivia.db";

    public static void main(final String[] theArgs) {
        try (Connection con = DriverManager.getConnection(CONNECTION_URL);
            Statement stat = con.createStatement()) {
                stat.execute("""
                    CREATE TABLE IF NOT EXISTS questions (
                        id
                        type            TEXT        NOT NULL CHECK(type IN (
                                            'MULTIPLE_CHOICE',
                                            'TRUE_FALSE',
                                            'SHORT_ANSWER'
                                        )),
                        text            TEXT        NOT NULL,
                        option_1        TEXT,
                        option_2        TEXT,
                        option_3        TEXT,
                        option_3        TEXT,
                        answer          TEXT        NOT NULL,
                        category        TEXT        NOT NULL CHECK(category IN (
                                            'SCIENCE',
                                            'HISTORY',
                                            'GEOGRAPHY',
                                            'SPORTS',
                                            'ENTERTAINMENT',
                                            'TECHNOLOGY'
                                        )),
                        difficulty      TEXT        NOT NULL CHECK(difficulty IN (
                                            'EASY',
                                            'MEDIUM',
                                            'HARD'
                                        )),
                    );
                """);
                // Seeding...
                stat.execute("""
                        INSERT INTO questions
                            (type, text, answer, option_1, option_2, option_3, option_4)
                        VALUES
                            ('MULTIPLE_CHOICE',
                                'SAMPLE QUESTION?',
                                'SAMPLE ANSWER',
                                'SAMPLE CHOICE 1', 'SAMPLE CHOICE 2', 'SAMPLE CHOICE 3', 'SAMPLE CHOICE 4'
                            );
                        """);

            } catch (Exception e) {
            System.err.println("Database setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
