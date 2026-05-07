package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 testing for Door
 * 
 * All tests are written as methods and grouped my method concerns from Door.
 * Inner TestQuestion class is implemented to keep modularity and promote loose coupling. 
 * 
 * Run in shell for tests:
 *   java -jar lib/junit-platform-console-standalone-1.11.4.jar \ 
 *      --class-path bin \ 
 *      --scan-class-path
 */
class DoorTest {

    // Inner Question class
    private static class TestQuestion extends Question {
        static final String CORRECT = "correct";
        static final String WRONG = "wrong";
        
        public TestQuestion() {
            super();
        }

        @Override
        public boolean checkAnswer(String theAnswer) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'checkAnswer'");
        }

        @Override
        public QuestionType getQuestionType() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getQuestionType'");
        }
    }

    private TestQuestion testQuestion;
    private Door myDoor;
    @BeforeEach
    void setUp() {
        testQuestion = new TestQuestion();
        myDoor = new Door(testQuestion);
    }

    // ~~~ Constructor tests ~~~~

    @Test
    void testNewDoorIsLocked() {
        assertTrue(myDoor.isLocked(),
            "A newly constructed door should be locked.");
    }

    @Test
    void testNewDoorIsNotOpen() {
        assertFalse(myDoor.isOpen(),
            "A newly constructed door should not be open.");
    }

    @Test
    void testNewDoorIsUnanswered() {
        assertFalse(myDoor.getDoorState(),
            "A newly constructed door should return false before any answer is submitted.");
    }

    @Test
    void testNewDoorQuestion() {
        assertSame(testQuestion, myDoor.getQuestion(),
            "getQuestion should return the exact question passed to the constructor.");
    }

    @Test
    void testConstructorNullQuestionThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new Door(null),
            "Passing a null as a Question should throw IllegalArgumentException.");
    }

    // ~~~ unlock tests ~~~
    @Test
    void testAttemptUnlockCorrectAnswer() {
        assertTrue(myDoor.attemptUnlock(TestQuestion.CORRECT),
            "attemptUnlock() should return true for the correct answer.");
    }

    @Test
    void testUnlockOpensDoor() {
        myDoor.attemptUnlock(TestQuestion.CORRECT);
        myDoor.unlock();
        assertTrue(myDoor.isOpen(),
            "Door should be open after being unlocked.");
    }

    @Test
    void testUnlockRemovesLockedState() {
        myDoor.unlock();
        assertFalse(myDoor.isLocked(),
            "Door should not be locked after unlock is called.");
    }

    @Test
    void testUnlockMarkDoorStateAsOpen() {
        myDoor.unlock();
        assertTrue(myDoor.getDoorState(),
            "getDoorState() should return true after calling unlock().");
    }

    // ~~~ lock test ~~~

    @Test
    void testAttemptUnlockWrongAnswer() {
        assertFalse(myDoor.attemptUnlock(TestQuestion.WRONG),
            "attemptUnlock() should return false for the wrong answer.");
    }

    @Test
    void testAttemptUnlockWithEmptyAnswer() {
        assertFalse(myDoor.attemptUnlock(""),
            "A empty string answer should not be accepted as a correct answer.");
    }

    @Test
    void testAttemptUnlockWithNullAnswer() {
        assertFalse(myDoor.attemptUnlock(null),
            "A null answer should not be accepted as a correct answer.");
    }


    // ~~~ getQuestion tests ~~~

    @Test
    void testGetQuestionNotNull() {
        assertNotNull(myDoor.getQuestion(),
            "getQuestion() should never return null.");
    }

    @Test
    void testGetQuestionReturnSame() {
        assertSame(myDoor.getQuestion(), myDoor.getQuestion(),
            "getQuestion() must return the same Question instance on every method call.");
    }

    // ~~~ toString tests ~~~

    @Test
    void testToStringNotNull() {
        assertNotNull(myDoor.toString());
    }

    @Test
    void testToStringLockedState() {
        String state = myDoor.toString().toLowerCase();
        assertTrue(state.contains("locked"),
            "toString() should mention locked state.");
    }

    @Test
    void testToStringOpenState() {
        myDoor.unlock();
        String state = myDoor.toString().toLowerCase();
        assertTrue(state.contains("open"),
            "toString() should mention open state.");
    }
}
