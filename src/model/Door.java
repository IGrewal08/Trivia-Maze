package model;
import java.io.Serializable;

/**
 * Represents a door connecting two adjacent rooms in the Trivia Maze to allow the player traversal.
 *
 * Each door is assigned a single question upon maze generation, initially the door begins in a lock state
 * Once a correct question has been answered, it is permanently unlocked, while a incorrect answer leads the door
 * being permanently blocked for the rest of the game.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class Door implements Serializable {

    /** Locked state of this door, requires correct answer to pass. */
    private boolean myLocked;
    /** Open state of this door, when unlocked the player can pass this room. */
    private boolean myOpened;
    /** Whether this door's question has been answered (either correct or incorrect). */
    private boolean myAnswered;
    /** The trivia question that must be answered correctly to unlock this door. */
    private Question myQuestion;

    /**
     * Construct a Door with the given trivia question, with its initial state as locked.
     * 
     * The provided question is assigned once and is permanent, and cannot be changed after construction.
     * 
     * @param theQuestion the trivia question assigned to this door
     * @throws IllegalArgumentException if thQuestion is null
     */
    public Door(Question theQuestion) {
        if (theQuestion == null) {
            throw new IllegalArgumentException("Door question must not be null.");
        }
        this.myLocked = true;
        this.myOpened = false;
        this.myAnswered = false;
        this.myQuestion = theQuestion;
    }

    /**
     * Returns whether this door's question has been answered.
     * 
     * @return {@code true} when an answer has been submitted for this door
     */
    public boolean getDoorState() {
        return myAnswered;
    }

    /**
     * Returns whether this door is currently locked.
     * 
     * @return {@code true} when this door is locked
     */
    public boolean isLocked() {
        return myLocked;
    }

    /**
     * Returns whether this door is open and passable.
     * 
     * @return {@code true} when this door has been unlocked successfully
     */
    public boolean isOpen() {
        return myOpened;
    }

    /**
     * Locks this door, preventing further passage.
     */
    public void lock() {
        this.myLocked = true;
        this.myOpened = false;
    }

    /**
     * Permanently unlocks this door, allowing further passage.
     */
    public void unlock() {
        this.myOpened = true;
        this.myLocked = false;
    }

    /**
     * Returns the trivia question assigned to this door.
     * 
     * @return the question assigned to this door
     */
    public Question getQuestion() {
        return myQuestion;
    }

    /**
     * Attempts to unlock this door by checking the player's answer to actual answer.
     * 
     * @param theAnswer the player's answer
     * @return {@code true} if the answer is correct, {@code false} otherwise
     */
    public boolean attemptUnlock(String theAnswer) {
        return myQuestion.checkAnswer(theAnswer);
    }
}
