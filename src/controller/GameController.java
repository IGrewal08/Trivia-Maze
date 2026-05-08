package controller;

import model.Direction;
import model.Door;

public class GameController {
    private GameState myState;
    private GameView myView;

    public gameController(final GameState theState, final GameView theView) {
        if (theState == null || theView == null) {
            throw new IllegalArgumentException("GameState or GameView must not be null.");
        }
        this.myState = theState;
        this.myView = theView;
    }

    public void handleMove(final Direction theDir) {
        if (theDir == null) {
            throw new IllegalArgumentException("Direction within handleMove must not be null.");
        }
        Position newPos = myState.getCurrentPosition().translate(theDir);
        Door door = myState.getMaze().getRoom(newPos).getDoor(theDir);

        if (door.isOpen()) {
            myState.setCurrentPosition(newPos);
            myState.addVisitedRoom(newPos);
            myState.firePropertyChange("PLAYER_MOVED", null, newPos); // TODO on GameState class
        } else if (door.isLocked()) {
            myState.setCurrentDirection(theDir);
            myState.firePropertyChange("QUESTION_ASKED", null, door.getQuestion());
        }

    }

    public void handleAnswer(final String theAnswer) {
        if (theAnswer.equals("") || theAnswer == null) {
            throw new IllegalArgumentException("Player answer must not be blank or null.");
        }

        Direction dir = myState.getCurrentDirection();
        Position newPos = myState.getCurrentPosition().translate(dir);
        Door door = myState.getMaze().getRoom(newPos).getDoor(dir);

        if (door.attemptUnlock(theAnswer)) {
            door.unlock();
            myState.setCurrentPosition(newPos);
            myState.addVisitedRoom(newPos);
            myState.firePropertyChange("PLAYER_MOVED", null, newPos);
            myState.firePropertyChange("ANSWER_RESULT", null, true);
        } else {
            door.block();
            myState.firePropertyChange("ANSWER_RESULT", null, false);
            myState.firePropertyChange("MAZE_UPDATED", null, null);
        }

        if (!myState.getMaze().isPathAvailable(myState.getCurrentPosition())) {
            myState.firePropertyChange("GAME_OVER", null, GameStatus.LOST); // TODO build Stats ENUM for game update per event
        }
        
    }

    public void newGame(final int theWidth, final int theHeight) {
        if (theWidth < 0 || theHeight < 0) {
            throw new IllegalArgumentException("Maze width/height must be non-negative.");
        }
        myState = new GameState(theWidth, theHeight);
    }

    public void saveGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Save file name must not be empty.");
        }
        myState.saveGame(theFileName);
    }

    public void loadGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Load file name must not be empty.");
        }
    }

    public void exitGame() {

    }
}
