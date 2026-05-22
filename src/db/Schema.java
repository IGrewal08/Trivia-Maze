package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Schema class to create a SQLite database table and seed on a single run.
 * 
 * Seeds total of 63 questions, with 7 categories in science, history, geography, sports,
 * technology, entertainment, and math. With 3 types of questions, multiple choice, true/false
 * and short answer, with 3 difficulties; easy, medium, and hard.
 * 
 * Use run Schema.initialize() to populate before DatabaseManager opens connection
 * 
 * Source (for questions): https://www.today.com/life/inspiration/trivia-questions-rcna39101#anchor-efab46
 * 
 * @author Inderdeep Grewal
 * @version 1.1
 */
public class Schema {

    /**
     * Schema constructor not instantiate (all methods are static).
     */
    private Schema() {}

    /**
     * Creates table and seeds data when needed.
     */
    public static void initialize() {
        try (Connection con = DriverManager.getConnection(CONNECTION_URL);
            Statement stat = con.createStatement()) {
                createTable(stat);

                // check for table contents
                if (isEmpty(con)) {
                    seed(stat);
                    System.out.println("Schema: database seeded successfully.");
                } else {
                    System.out.println("Schema: table exists and has data.");
                }
            } catch (Exception e) {
                System.err.println("Schema initialization failed: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Private method to create table for database for Trivia Maze questions.
     * @param theStat a statement object to for running SQL commands.
     * @throws Exception when an error occurs during table creation.
     */
    private static void createTable(final Statement theStat) throws Exception {
        theStat.execute("""
                    CREATE TABLE IF NOT EXISTS questions (
                        id              INTEGER     PRIMARY KEY AUTOINCREMENT,
                        type            TEXT        NOT NULL CHECK(type IN (
                                            'MULTIPLE_CHOICE', 'TRUE_FALSE', 'SHORT_ANSWER')),
                        text            TEXT        NOT NULL,
                        answer          TEXT        NOT NULL,
                        option_1        TEXT,
                        option_2        TEXT,
                        option_3        TEXT,
                        option_4        TEXT,
                        category        TEXT        NOT NULL CHECK(category IN (
                                            'SCIENCE','HISTORY','GEOGRAPHY','SPORTS',
                                            'TECHNOLOGY','ENTERTAINMENT','MATH')),
                        difficulty      TEXT        NOT NULL CHECK(difficulty IN (
                                            'EASY','MEDIUM','HARD'))
                    );
                """);
    }

    /**
     * Checks to see if the database has been seeded with trivia questions.
     * @param theCon Connection object to the current database to send SQL commands.
     * @return {@true} if the database is empty, {@false} otherwise.
     * @throws Exception when querying the database.
     */
    private static boolean isEmpty(final Connection theCon) throws Exception {
        try (ResultSet rs = theCon.createStatement().executeQuery("SELECT COUNT(*) FROM questions")) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }
    
    /**
     * Seeds the database with 63 trivia questions, each row includes the question, options, category, and difficulty.
     * @param theStat a statement object to for running SQL commands.
     * @throws Exception when an error occurs during seeding process.
     */
    private static void seed(final Statement theStat) throws Exception {
        theStat.execute("""
                INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
                VALUES (
                    'MULTIPLE_CHOICE',
                    'How many elements are on the periodic table?',
                    '118',
                    '100', '120', '118', '99',
                    'SCIENCE', 'MEDIUM'
                );
            """);
 
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is the only planet in our solar system to rotate clockwise on its axis?',
                'Venus',
                'Mars', 'Venus', 'Jupiter', 'Saturn',
                'SCIENCE', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'Weighing around eight pounds, what is the human body''s largest organ?',
                'Skin',
                'Liver', 'Heart', 'Skin', 'Brain',
                'SCIENCE', 'EASY'
            );
        """);

        // ~~~ Science: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Hot water can freeze faster than cold water.',
                'true',
                'SCIENCE', 'HARD'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The Great Wall of China is visible from space with the naked eye.',
                'false',
                'SCIENCE', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Nearly all fossils are preserved in sedimentary rock.',
                'true',
                'SCIENCE', 'MEDIUM'
            );
        """);

        // ~~~ Science: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What is the chemical symbol for gold?',
                'au',
                'SCIENCE', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many bones are in the adult human body?',
                '206',
                'SCIENCE', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What does DNA stand for?',
                'deoxyribonucleic acid',
                'SCIENCE', 'HARD'
            );
        """);

        // ~~~ History: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'When was Earth Day first celebrated?',
                '1970',
                '1965', '1970', '1975', '1980',
                'HISTORY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'Who is considered the Father of Relativity?',
                'Albert Einstein',
                'Isaac Newton', 'Albert Einstein', 'Nikola Tesla', 'Stephen Hawking',
                'HISTORY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'Which branch of the U.S. armed forces used the slogan "It''s not just a job, it''s an adventure"?',
                'Navy',
                'Army', 'Navy', 'Marines', 'Air Force',
                'HISTORY', 'MEDIUM'
            );
        """);

        // ~~~ History: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'World War I ended in 1918.',
                'true',
                'HISTORY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The Declaration of Independence was signed in 1775.',
                'false',
                'HISTORY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The Berlin Wall fell in 1989.',
                'true',
                'HISTORY', 'MEDIUM'
            );
        """);

        // ~~~ History: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'Who was the first President of the United States?',
                'george washington',
                'HISTORY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'In what year did World War II end?',
                '1945',
                'HISTORY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What year did the Berlin Wall fall?',
                '1989',
                'HISTORY', 'MEDIUM'
            );
        """);

        // ~~~ Geography: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'Which country is the largest in the world by area?',
                'Russia',
                'China', 'Canada', 'Russia', 'United States',
                'GEOGRAPHY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is the approximate distance from Earth to the Sun?',
                '93 million miles',
                '50 million miles', '93 million miles', '150 million miles', '200 million miles',
                'GEOGRAPHY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What U.S. state is home to Acadia National Park?',
                'Maine',
                'Maine', 'Vermont', 'New Hampshire', 'Massachusetts',
                'GEOGRAPHY', 'MEDIUM'
            );
        """);

        // ~~~ Geography: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Australia is both a country and a continent.',
                'true',
                'GEOGRAPHY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The Amazon River is the longest river in the world.',
                'false',
                'GEOGRAPHY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Russia is the largest country in the world by area.',
                'true',
                'GEOGRAPHY', 'EASY'
            );
        """);

        // ~~~ Geography: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What is the capital of France?',
                'paris',
                'GEOGRAPHY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What is the largest ocean by area?',
                'pacific',
                'GEOGRAPHY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many U.S. states does the Appalachian Trail cross?',
                '14',
                'GEOGRAPHY', 'HARD'
            );
        """);

        // ~~~ Sports: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What sport was featured on the first curved U.S. coin in 2014?',
                'Baseball',
                'Football', 'Baseball', 'Basketball', 'Soccer',
                'SPORTS', 'HARD'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many points is a touchdown worth in American football?',
                '6',
                '3', '6', '7', '8',
                'SPORTS', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many players from one team are on a soccer field at a time?',
                '11',
                '9', '10', '11', '12',
                'SPORTS', 'EASY'
            );
        """);

        // ~~~ Sports: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'A standard marathon is 26.2 miles long.',
                'true',
                'SPORTS', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The Summer Olympic Games are held every four years.',
                'true',
                'SPORTS', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Basketball was invented by a Canadian named James Naismith.',
                'true',
                'SPORTS', 'HARD'
            );
        """);
 
        // ~~~ Sports: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many players from one team are on a baseball field at a time?',
                '9',
                'SPORTS', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many rings are on the Olympic flag?',
                '5',
                'SPORTS', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What country won the first FIFA World Cup in 1930?',
                'uruguay',
                'SPORTS', 'HARD'
            );
        """);

        // ~~~ Technology: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What does URL stand for?',
                'Uniform Resource Locator',
                'Universal Remote Link', 'Uniform Resource Locator', 'Unified Record Locator', 'Universal Resource Link',
                'TECHNOLOGY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'In what year was the first iPhone released?',
                '2007',
                '2005', '2006', '2007', '2008',
                'TECHNOLOGY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many bits are in a byte?',
                '8',
                '4', '8', '16', '32',
                'TECHNOLOGY', 'EASY'
            );
        """);

        // ~~~ Technology: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Java is a compiled programming language.',
                'true',
                'TECHNOLOGY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The first computer bug was an actual insect found in a machine.',
                'true',
                'TECHNOLOGY', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'HTML stands for HyperText Markup Language.',
                'true',
                'TECHNOLOGY', 'EASY'
            );
        """);

        // ~~~ Technology: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What does CPU stand for?',
                'central processing unit',
                'TECHNOLOGY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What programming language shares its name with a type of coffee?',
                'java',
                'TECHNOLOGY', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What does HTTP stand for?',
                'hypertext transfer protocol',
                'TECHNOLOGY', 'MEDIUM'
            );
        """);

        // ~~~ Entertainment: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What actor plays Ken in the 2023 blockbuster movie Barbie?',
                'Ryan Gosling',
                'Brad Pitt', 'Ryan Gosling', 'Chris Hemsworth', 'Tom Holland',
                'ENTERTAINMENT', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'Anna, Elsa, Kristoff and Olaf are all characters in what animated movie?',
                'Frozen',
                'Tangled', 'Frozen', 'Moana', 'Brave',
                'ENTERTAINMENT', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What name is singer and actress Stefani Germanotta better known by?',
                'Lady Gaga',
                'Beyonce', 'Adele', 'Lady Gaga', 'Katy Perry',
                'ENTERTAINMENT', 'EASY'
            );
        """);

        // ~~~ Entertainment: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Whitney Houston''s "I Will Always Love You" was originally written by Dolly Parton.',
                'true',
                'ENTERTAINMENT', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The TV series The Sopranos is set in New York City.',
                'false',
                'ENTERTAINMENT', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The 1997 film Men in Black stars Will Smith and Tommy Lee Jones.',
                'true',
                'ENTERTAINMENT', 'EASY'
            );
        """);

        // ~~~ Entertainment: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'Before her solo career, Beyonce was part of what R&B group?',
                'destiny''s child',
                'ENTERTAINMENT', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'In what fictional Indiana town does the Netflix series Stranger Things take place?',
                'hawkins',
                'ENTERTAINMENT', 'MEDIUM'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What was Taylor Swift''s first song to chart on the Billboard Hot 100?',
                'tim mcgraw',
                'ENTERTAINMENT', 'HARD'
            );
        """);

        // ~~~ Math: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is an eight-sided polygon called?',
                'Octagon',
                'Hexagon', 'Heptagon', 'Octagon', 'Nonagon',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many points does the Star of David have?',
                '6',
                '5', '6', '7', '8',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is 15% of 200?',
                '30',
                '20', '25', '30', '35',
                'MATH', 'MEDIUM'
            );
        """);

        // ~~~ Math: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'Pi is approximately equal to 3.14.',
                'true',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'A right angle is exactly 90 degrees.',
                'true',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'TRUE_FALSE',
                'The sum of angles in any triangle is always 180 degrees.',
                'true',
                'MATH', 'MEDIUM'
            );
        """);

        // ~~~ Math: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many sides does a hexagon have?',
                '6',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'How many degrees are in a full circle?',
                '360',
                'MATH', 'EASY'
            );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty)
            VALUES (
                'SHORT_ANSWER',
                'What is the square root of 144?',
                '12',
                'MATH', 'MEDIUM'
            );
        """);

        System.out.print("Seeding complete.");
    }
}
