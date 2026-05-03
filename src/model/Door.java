package model;
import java.io.Serializable;

public class Door implements Serializable {
    
    private boolean myLocked;
    private boolean myOpened;
    private boolean myAnswered;
    private Question myQuestion;

    public Door(Question theQuestion) {
        this.myLocked = true;
        this.myOpened = false;
        this.myAnswered = false;
        this.myQuestion = theQuestion;
    }

    public boolean getDoorState() {
        return myAnswered;
    }

    public boolean isLocked() {
        return myLocked;
    }

    public boolean isOpen() {
        return myOpened;
    }

    public void lock() {
        this.myLocked = true;
        this.myOpened = false;
    }

    public void unlock() {
        this.myOpened = true;
        this.myLocked = false;
    }

    public Question getQuestion() {
        return myQuestion;
    }

    public boolean attemptUnlock(String theAnswer) {
        return myQuestion.checkAnswer(theAnswer);
    }
}
