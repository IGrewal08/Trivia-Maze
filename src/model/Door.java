package model;

import java.io.Serializable;

/**
 * Represents a door connecting two adjacent rooms in the Trivia Maze
 * to allow player traversal.
 *
 * Each door is assigned a single trivia question upon maze generation.
 * Initially the door begins in a locked state. Once a correct question
 * has been answered, it is permanently unlocked. An incorrect answer
 * permanently blocks the door for the remainder of the game.
 *
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class Door implements Serializable {

    /** Locked state of this door, requires correct answer to pass. */
    private boolean myLocked;

    /** Open state of this door, allowing traversal. */
    private boolean myOpened;

    /** Permanently blocked state after incorrect answer. */
    private boolean myBlocked;

    /** Whether this door's question has been answered. */
    private boolean myAnswered;

    /** Trivia question assigned to this door. */
    private Question myQuestion;

    /**
     * Constructs a Door with the given trivia question.
     *
     * @param theQuestion the trivia question assigned to this door
     * @throws IllegalArgumentException if theQuestion is null
     */
    public Door(final Question theQuestion) {

        if (theQuestion == null) {
            throw new IllegalArgumentException(
                    "Door question must not be null.");
        }

        myLocked = true;
        myOpened = false;
        myBlocked = false;
        myAnswered = false;

        myQuestion = theQuestion;
    }

    /**
     * Returns whether this door has been answered.
     *
     * @return true if answered
     */
    public boolean getDoorState() {
        return myAnswered;
    }

    /**
     * Returns whether this door is currently locked.
     *
     * @return true if locked
     */
    public boolean isLocked() {
        return myLocked;
    }

    /**
     * Returns whether this door is currently open.
     *
     * @return true if open
     */
    public boolean isOpen() {
        return myOpened;
    }

    /**
     * Returns whether this door is permanently blocked.
     *
     * @return true if blocked
     */
    public boolean isBlocked() {
        return myBlocked;
    }

    /**
     * Locks this door.
     */
    public void lock() {
        myLocked = true;
        myOpened = false;
        myBlocked = false;
    }

    /**
     * Unlocks this door permanently.
     */
    public void unlock() {
        myOpened = true;
        myLocked = false;
        myBlocked = false;
        myAnswered = true;
    }

    /**
     * Permanently blocks this door.
     */
    public void block() {
        myBlocked = true;
        myLocked = false;
        myOpened = false;
        myAnswered = true;
    }

    /**
     * Returns the trivia question assigned to this door.
     *
     * @return assigned trivia question
     */
    public Question getQuestion() {
        return myQuestion;
    }

    /**
     * Attempts to unlock this door using the provided answer.
     *
     * @param theAnswer player's answer
     * @return true if answer is correct
     */
    public boolean attemptUnlock(final String theAnswer) {
        return myQuestion.checkAnswer(theAnswer);
    }

    /**
     * Returns string representation of this door.
     *
     * @return formatted door state string
     */
    @Override
    public String toString() {

        return String.format(
                "Door [locked=%b, open=%b, blocked=%b, answered=%b]",
                myLocked,
                myOpened,
                myBlocked,
                myAnswered);
    }
}