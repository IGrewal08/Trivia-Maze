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
        try (Connection con = DriverManager.getConnection(DatabaseManager.getDBPath());
            Statement stat = con.createStatement()) {
                createTable(stat);
                ensureHintColumn(stat);

                // check for table contents
                if (isEmpty(con)) {
                    seed(stat);
                    System.out.println("Schema: database seeded successfully.");
                } else {
                    System.out.println("Schema: table exists and has data.");
                }
                seedHints(stat);
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
                        hint            TEXT,
                        category        TEXT        NOT NULL CHECK(category IN (
                                            'SCIENCE','HISTORY','GEOGRAPHY','SPORTS',
                                            'TECHNOLOGY','ENTERTAINMENT','MATH')),
                        difficulty      TEXT        NOT NULL CHECK(difficulty IN (
                                            'EASY','MEDIUM','HARD'))
                    );
                """);
    }

    /**
     * Adds the hint column for older local databases created before hints existed.
     * @param theStat statement used to inspect and update the table.
     * @throws Exception when database metadata cannot be read or altered.
     */
    private static void ensureHintColumn(final Statement theStat) throws Exception {
        try (ResultSet rs = theStat.executeQuery("PRAGMA table_info(questions)")) {
            while (rs.next()) {
                if ("hint".equalsIgnoreCase(rs.getString("name"))) {
                    return;
                }
            }
        }
        theStat.execute("ALTER TABLE questions ADD COLUMN hint TEXT");
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
                INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
                VALUES (
                    'MULTIPLE_CHOICE',
                    'How many elements are on the periodic table?',
                    '118',
                    '100', '120', '118', '99',
                    'SCIENCE', 'MEDIUM',
                    'This count is a little under 120.'
                );
            """);
 
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is the only planet in our solar system to rotate clockwise on its axis?',
                'Venus',
                'Mars', 'Venus', 'Jupiter', 'Saturn',
                'SCIENCE', 'MEDIUM',
                    'Think about the planet known for its unusual spin and bright evening appearance.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'Weighing around eight pounds, what is the human body''s largest organ?',
                'Skin',
                'Liver', 'Heart', 'Skin', 'Brain',
                'SCIENCE', 'EASY',
                    'The organ covers the outside of the body.'
                );
        """);

        // ~~~ Science: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Hot water can freeze faster than cold water.',
                'true',
                'SCIENCE', 'HARD',
                    'This effect is named after a student who observed it in ice cream making.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The Great Wall of China is visible from space with the naked eye.',
                'false',
                'SCIENCE', 'EASY',
                    'Consider how thin the structure is compared with viewing distance.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Nearly all fossils are preserved in sedimentary rock.',
                'true',
                'SCIENCE', 'MEDIUM',
                    'Fossils usually form where layers of material settle over time.'
                );
        """);

        // ~~~ Science: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What is the chemical symbol for gold?',
                'au',
                'SCIENCE', 'EASY',
                    'The symbol comes from the Latin name aurum.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many bones are in the adult human body?',
                '206',
                'SCIENCE', 'MEDIUM',
                    'The adult count is just over two hundred.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What does DNA stand for?',
                'deoxyribonucleic acid',
                'SCIENCE', 'HARD',
                    'The full name describes a molecule with deoxyribose sugar and nucleic acid.'
                );
        """);

        // ~~~ History: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'When was Earth Day first celebrated?',
                '1970',
                '1965', '1970', '1975', '1980',
                'HISTORY', 'EASY',
                    'It began at the start of the modern environmental movement.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'Who is considered the Father of Relativity?',
                'Albert Einstein',
                'Isaac Newton', 'Albert Einstein', 'Nikola Tesla', 'Stephen Hawking',
                'HISTORY', 'EASY',
                    'This scientist is strongly associated with E equals mc squared.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'Which branch of the U.S. armed forces used the slogan "It''s not just a job, it''s an adventure"?',
                'Navy',
                'Army', 'Navy', 'Marines', 'Air Force',
                'HISTORY', 'MEDIUM',
                    'The slogan points toward travel and service at sea.'
                );
        """);

        // ~~~ History: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'World War I ended in 1918.',
                'true',
                'HISTORY', 'EASY',
                    'Think of the armistice signed in November of that year.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The Declaration of Independence was signed in 1775.',
                'false',
                'HISTORY', 'MEDIUM',
                    'The famous signing is tied to the year after the Revolutionary War began.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The Berlin Wall fell in 1989.',
                'true',
                'HISTORY', 'MEDIUM',
                    'This event happened near the end of the Cold War.'
                );
        """);

        // ~~~ History: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'Who was the first President of the United States?',
                'george washington',
                'HISTORY', 'EASY',
                    'His portrait appears on the one-dollar bill.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'In what year did World War II end?',
                '1945',
                'HISTORY', 'EASY',
                    'The war ended the same year as V-E Day and V-J Day.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What year did the Berlin Wall fall?',
                '1989',
                'HISTORY', 'MEDIUM',
                    'It happened in the final full year of the 1980s.'
                );
        """);

        // ~~~ Geography: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'Which country is the largest in the world by area?',
                'Russia',
                'China', 'Canada', 'Russia', 'United States',
                'GEOGRAPHY', 'EASY',
                    'This country spans eastern Europe and northern Asia.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is the approximate distance from Earth to the Sun?',
                '93 million miles',
                '50 million miles', '93 million miles', '150 million miles', '200 million miles',
                'GEOGRAPHY', 'MEDIUM',
                    'The distance is about one astronomical unit.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What U.S. state is home to Acadia National Park?',
                'Maine',
                'Maine', 'Vermont', 'New Hampshire', 'Massachusetts',
                'GEOGRAPHY', 'MEDIUM',
                    'Look to the northeastern coast of the United States.'
                );
        """);

        // ~~~ Geography: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Australia is both a country and a continent.',
                'true',
                'GEOGRAPHY', 'EASY',
                    'Think about how the landmass is classified geographically and politically.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The Amazon River is the longest river in the world.',
                'false',
                'GEOGRAPHY', 'MEDIUM',
                    'The Nile is often used as the comparison for this record.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Russia is the largest country in the world by area.',
                'true',
                'GEOGRAPHY', 'EASY',
                    'It covers more than one continent.'
                );
        """);

        // ~~~ Geography: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What is the capital of France?',
                'paris',
                'GEOGRAPHY', 'EASY',
                    'This city is known for the Eiffel Tower.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What is the largest ocean by area?',
                'pacific',
                'GEOGRAPHY', 'EASY',
                    'This ocean borders the west coast of the Americas and eastern Asia.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many U.S. states does the Appalachian Trail cross?',
                '14',
                'GEOGRAPHY', 'HARD',
                    'The trail runs from Georgia to Maine through more than a dozen states.'
                );
        """);

        // ~~~ Sports: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What sport was featured on the first curved U.S. coin in 2014?',
                'Baseball',
                'Football', 'Baseball', 'Basketball', 'Soccer',
                'SPORTS', 'HARD',
                    'The coin honored a sport with a round ball and a diamond-shaped field.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many points is a touchdown worth in American football?',
                '6',
                '3', '6', '7', '8',
                'SPORTS', 'EASY',
                    'The extra point attempt comes after this score.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many players from one team are on a soccer field at a time?',
                '11',
                '9', '10', '11', '12',
                'SPORTS', 'EASY',
                    'Include the goalkeeper in the count.'
                );
        """);

        // ~~~ Sports: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'A standard marathon is 26.2 miles long.',
                'true',
                'SPORTS', 'MEDIUM',
                    'The modern race distance is just over twenty-six miles.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The Summer Olympic Games are held every four years.',
                'true',
                'SPORTS', 'EASY',
                    'Think of the regular Olympiad cycle.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Basketball was invented by a Canadian named James Naismith.',
                'true',
                'SPORTS', 'HARD',
                    'The inventor created the game while teaching physical education.'
                );
        """);
 
        // ~~~ Sports: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many players from one team are on a baseball field at a time?',
                '9',
                'SPORTS', 'EASY',
                    'Count the pitcher, catcher, infielders, and outfielders.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many rings are on the Olympic flag?',
                '5',
                'SPORTS', 'EASY',
                    'The symbol represents the inhabited continents in interlocking rings.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What country won the first FIFA World Cup in 1930?',
                'uruguay',
                'SPORTS', 'HARD',
                    'The host nation won the first tournament.'
                );
        """);

        // ~~~ Technology: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What does URL stand for?',
                'Uniform Resource Locator',
                'Universal Remote Link', 'Uniform Resource Locator', 'Unified Record Locator', 'Universal Resource Link',
                'TECHNOLOGY', 'MEDIUM',
                    'The phrase describes a standardized address for a resource.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'In what year was the first iPhone released?',
                '2007',
                '2005', '2006', '2007', '2008',
                'TECHNOLOGY', 'EASY',
                    'It was announced by Steve Jobs in the late 2000s.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many bits are in a byte?',
                '8',
                '4', '8', '16', '32',
                'TECHNOLOGY', 'EASY',
                    'This is the standard binary grouping used for one byte.'
                );
        """);

        // ~~~ Technology: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Java is a compiled programming language.',
                'true',
                'TECHNOLOGY', 'MEDIUM',
                    'Java source is compiled into bytecode before running on the JVM.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The first computer bug was an actual insect found in a machine.',
                'true',
                'TECHNOLOGY', 'MEDIUM',
                    'The story involves a moth found in an early computer relay.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'HTML stands for HyperText Markup Language.',
                'true',
                'TECHNOLOGY', 'EASY',
                    'It is the markup language used to structure web pages.'
                );
        """);

        // ~~~ Technology: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What does CPU stand for?',
                'central processing unit',
                'TECHNOLOGY', 'EASY',
                    'This component is often called the main processor.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What programming language shares its name with a type of coffee?',
                'java',
                'TECHNOLOGY', 'EASY',
                    'The language logo uses a steaming cup.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What does HTTP stand for?',
                'hypertext transfer protocol',
                'TECHNOLOGY', 'MEDIUM',
                    'The phrase describes the web protocol for transferring hypertext.'
                );
        """);

        // ~~~ Entertainment: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What actor plays Ken in the 2023 blockbuster movie Barbie?',
                'Ryan Gosling',
                'Brad Pitt', 'Ryan Gosling', 'Chris Hemsworth', 'Tom Holland',
                'ENTERTAINMENT', 'EASY',
                    'This actor also starred in La La Land.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'Anna, Elsa, Kristoff and Olaf are all characters in what animated movie?',
                'Frozen',
                'Tangled', 'Frozen', 'Moana', 'Brave',
                'ENTERTAINMENT', 'EASY',
                    'The story includes an ice queen and a snowman.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What name is singer and actress Stefani Germanotta better known by?',
                'Lady Gaga',
                'Beyonce', 'Adele', 'Lady Gaga', 'Katy Perry',
                'ENTERTAINMENT', 'EASY',
                    'Her stage name includes a title and a repeated syllable.'
                );
        """);

        // ~~~ Entertainment: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Whitney Houston''s "I Will Always Love You" was originally written by Dolly Parton.',
                'true',
                'ENTERTAINMENT', 'MEDIUM',
                    'The song existed as a country ballad before the famous movie version.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The TV series The Sopranos is set in New York City.',
                'false',
                'ENTERTAINMENT', 'MEDIUM',
                    'The crime family is most closely tied to New Jersey.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The 1997 film Men in Black stars Will Smith and Tommy Lee Jones.',
                'true',
                'ENTERTAINMENT', 'EASY',
                    'Think of the two agents in black suits investigating aliens.'
                );
        """);

        // ~~~ Entertainment: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'Before her solo career, Beyonce was part of what R&B group?',
                'destiny''s child',
                'ENTERTAINMENT', 'EASY',
                    'The group name refers to fate and children.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'In what fictional Indiana town does the Netflix series Stranger Things take place?',
                'hawkins',
                'ENTERTAINMENT', 'MEDIUM',
                    'The town is home to the lab connected to the Upside Down.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What was Taylor Swift''s first song to chart on the Billboard Hot 100?',
                'tim mcgraw',
                'ENTERTAINMENT', 'HARD',
                    'The song title is the name of a country singer.'
                );
        """);

        // ~~~ Math: Multiple Choice
        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is an eight-sided polygon called?',
                'Octagon',
                'Hexagon', 'Heptagon', 'Octagon', 'Nonagon',
                'MATH', 'EASY',
                    'The prefix for eight is octa.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'How many points does the Star of David have?',
                '6',
                '5', '6', '7', '8',
                'MATH', 'EASY',
                    'The shape is formed by two overlapping triangles.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, option_1, option_2, option_3, option_4, category, difficulty, hint)
            VALUES (
                'MULTIPLE_CHOICE',
                'What is 15% of 200?',
                '30',
                '20', '25', '30', '35',
                'MATH', 'MEDIUM',
                    'Ten percent is 20, and five percent is 10.'
                );
        """);

        // ~~~ Math: True/False
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'Pi is approximately equal to 3.14.',
                'true',
                'MATH', 'EASY',
                    'This is the common two-decimal approximation used in math classes.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'A right angle is exactly 90 degrees.',
                'true',
                'MATH', 'EASY',
                    'Picture the corner of a square.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'TRUE_FALSE',
                'The sum of angles in any triangle is always 180 degrees.',
                'true',
                'MATH', 'MEDIUM',
                    'This is a standard fact from plane geometry.'
                );
        """);

        // ~~~ Math: Short Answer
        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many sides does a hexagon have?',
                '6',
                'MATH', 'EASY',
                    'The prefix hex means six.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'How many degrees are in a full circle?',
                '360',
                'MATH', 'EASY',
                    'A full turn has four right angles.'
                );
        """);

        theStat.execute("""
            INSERT INTO questions (type, text, answer, category, difficulty, hint)
            VALUES (
                'SHORT_ANSWER',
                'What is the square root of 144?',
                '12',
                'MATH', 'MEDIUM',
                    'Think of the number that multiplies by itself to make 144.'
                );
        """);

        System.out.print("Seeding complete.");
    }

    /**
     * Populates hints for seeded questions that do not already have one.
     * @param theStat statement used to run updates.
     * @throws Exception when an update fails.
     */
    private static void seedHints(final Statement theStat) throws Exception {
        setHintByText(theStat, "How many elements are on the periodic table?",
                "This count is a little under 120.");
        setHintByText(theStat, "What is the only planet in our solar system to rotate clockwise on its axis?",
                "Think about the planet known for its unusual spin and bright evening appearance.");
        setHintByText(theStat, "Weighing around eight pounds, what is the human body's largest organ?",
                "The organ covers the outside of the body.");
        setHintByText(theStat, "Hot water can freeze faster than cold water.",
                "This effect is named after a student who observed it in ice cream making.");
        setHintByText(theStat, "The Great Wall of China is visible from space with the naked eye.",
                "Consider how thin the structure is compared with viewing distance.");
        setHintByText(theStat, "Nearly all fossils are preserved in sedimentary rock.",
                "Fossils usually form where layers of material settle over time.");
        setHintByText(theStat, "What is the chemical symbol for gold?",
                "The symbol comes from the Latin name aurum.");
        setHintByText(theStat, "How many bones are in the adult human body?",
                "The adult count is just over two hundred.");
        setHintByText(theStat, "What does DNA stand for?",
                "The full name describes a molecule with deoxyribose sugar and nucleic acid.");
        setHintByText(theStat, "When was Earth Day first celebrated?",
                "It began at the start of the modern environmental movement.");
        setHintByText(theStat, "Who is considered the Father of Relativity?",
                "This scientist is strongly associated with E equals mc squared.");
        setHintByText(theStat, "Which branch of the U.S. armed forces used the slogan \"It's not just a job, it's an adventure\"?",
                "The slogan points toward travel and service at sea.");
        setHintByText(theStat, "World War I ended in 1918.",
                "Think of the armistice signed in November of that year.");
        setHintByText(theStat, "The Declaration of Independence was signed in 1775.",
                "The famous signing is tied to the year after the Revolutionary War began.");
        setHintByText(theStat, "The Berlin Wall fell in 1989.",
                "This event happened near the end of the Cold War.");
        setHintByText(theStat, "Who was the first President of the United States?",
                "His portrait appears on the one-dollar bill.");
        setHintByText(theStat, "In what year did World War II end?",
                "The war ended the same year as V-E Day and V-J Day.");
        setHintByText(theStat, "What year did the Berlin Wall fall?",
                "It happened in the final full year of the 1980s.");
        setHintByText(theStat, "Which country is the largest in the world by area?",
                "This country spans eastern Europe and northern Asia.");
        setHintByText(theStat, "What is the approximate distance from Earth to the Sun?",
                "The distance is about one astronomical unit.");
        setHintByText(theStat, "What U.S. state is home to Acadia National Park?",
                "Look to the northeastern coast of the United States.");
        setHintByText(theStat, "Australia is both a country and a continent.",
                "Think about how the landmass is classified geographically and politically.");
        setHintByText(theStat, "The Amazon River is the longest river in the world.",
                "The Nile is often used as the comparison for this record.");
        setHintByText(theStat, "Russia is the largest country in the world by area.",
                "It covers more than one continent.");
        setHintByText(theStat, "What is the capital of France?",
                "This city is known for the Eiffel Tower.");
        setHintByText(theStat, "What is the largest ocean by area?",
                "This ocean borders the west coast of the Americas and eastern Asia.");
        setHintByText(theStat, "How many U.S. states does the Appalachian Trail cross?",
                "The trail runs from Georgia to Maine through more than a dozen states.");
        setHintByText(theStat, "What sport was featured on the first curved U.S. coin in 2014?",
                "The coin honored a sport with a round ball and a diamond-shaped field.");
        setHintByText(theStat, "How many points is a touchdown worth in American football?",
                "The extra point attempt comes after this score.");
        setHintByText(theStat, "How many players from one team are on a soccer field at a time?",
                "Include the goalkeeper in the count.");
        setHintByText(theStat, "A standard marathon is 26.2 miles long.",
                "The modern race distance is just over twenty-six miles.");
        setHintByText(theStat, "The Summer Olympic Games are held every four years.",
                "Think of the regular Olympiad cycle.");
        setHintByText(theStat, "Basketball was invented by a Canadian named James Naismith.",
                "The inventor created the game while teaching physical education.");
        setHintByText(theStat, "How many players from one team are on a baseball field at a time?",
                "Count the pitcher, catcher, infielders, and outfielders.");
        setHintByText(theStat, "How many rings are on the Olympic flag?",
                "The symbol represents the inhabited continents in interlocking rings.");
        setHintByText(theStat, "What country won the first FIFA World Cup in 1930?",
                "The host nation won the first tournament.");
        setHintByText(theStat, "What does URL stand for?",
                "The phrase describes a standardized address for a resource.");
        setHintByText(theStat, "In what year was the first iPhone released?",
                "It was announced by Steve Jobs in the late 2000s.");
        setHintByText(theStat, "How many bits are in a byte?",
                "This is the standard binary grouping used for one byte.");
        setHintByText(theStat, "Java is a compiled programming language.",
                "Java source is compiled into bytecode before running on the JVM.");
        setHintByText(theStat, "The first computer bug was an actual insect found in a machine.",
                "The story involves a moth found in an early computer relay.");
        setHintByText(theStat, "HTML stands for HyperText Markup Language.",
                "It is the markup language used to structure web pages.");
        setHintByText(theStat, "What does CPU stand for?",
                "This component is often called the main processor.");
        setHintByText(theStat, "What programming language shares its name with a type of coffee?",
                "The language logo uses a steaming cup.");
        setHintByText(theStat, "What does HTTP stand for?",
                "The phrase describes the web protocol for transferring hypertext.");
        setHintByText(theStat, "What actor plays Ken in the 2023 blockbuster movie Barbie?",
                "This actor also starred in La La Land.");
        setHintByText(theStat, "Anna, Elsa, Kristoff and Olaf are all characters in what animated movie?",
                "The story includes an ice queen and a snowman.");
        setHintByText(theStat, "What name is singer and actress Stefani Germanotta better known by?",
                "Her stage name includes a title and a repeated syllable.");
        setHintByText(theStat, "Whitney Houston's \"I Will Always Love You\" was originally written by Dolly Parton.",
                "The song existed as a country ballad before the famous movie version.");
        setHintByText(theStat, "The TV series The Sopranos is set in New York City.",
                "The crime family is most closely tied to New Jersey.");
        setHintByText(theStat, "The 1997 film Men in Black stars Will Smith and Tommy Lee Jones.",
                "Think of the two agents in black suits investigating aliens.");
        setHintByText(theStat, "Before her solo career, Beyonce was part of what R&B group?",
                "The group name refers to fate and children.");
        setHintByText(theStat, "In what fictional Indiana town does the Netflix series Stranger Things take place?",
                "The town is home to the lab connected to the Upside Down.");
        setHintByText(theStat, "What was Taylor Swift's first song to chart on the Billboard Hot 100?",
                "The song title is the name of a country singer.");
        setHintByText(theStat, "What is an eight-sided polygon called?",
                "The prefix for eight is octa.");
        setHintByText(theStat, "How many points does the Star of David have?",
                "The shape is formed by two overlapping triangles.");
        setHintByText(theStat, "What is 15% of 200?",
                "Ten percent is 20, and five percent is 10.");
        setHintByText(theStat, "Pi is approximately equal to 3.14.",
                "This is the common two-decimal approximation used in math classes.");
        setHintByText(theStat, "A right angle is exactly 90 degrees.",
                "Picture the corner of a square.");
        setHintByText(theStat, "The sum of angles in any triangle is always 180 degrees.",
                "This is a standard fact from plane geometry.");
        setHintByText(theStat, "How many sides does a hexagon have?",
                "The prefix hex means six.");
        setHintByText(theStat, "How many degrees are in a full circle?",
                "A full turn has four right angles.");
        setHintByText(theStat, "What is the square root of 144?",
                "Think of the number that multiplies by itself to make 144.");
    }

    /**
     * Sets a hint for one seeded question.
     * @param theStat statement used to run the update.
     * @param theQuestion question text to update.
     * @param theHint hint text to store.
     * @throws Exception when an update fails.
     */
    private static void setHintByText(final Statement theStat, final String theQuestion,
                                      final String theHint) throws Exception {
        theStat.execute("UPDATE questions SET hint = '" + sql(theHint) + "' "
                + "WHERE text = '" + sql(theQuestion) + "'");
    }

    /**
     * Escapes a string for simple SQL literals used by seed updates.
     * @param theValue value to escape.
     * @return escaped SQL literal contents.
     */
    private static String sql(final String theValue) {
        return theValue.replace("'", "''");
    }
}
