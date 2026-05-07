package controller;

import model.Direction;

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

    }

    public void handleAnswer(final String theAnswer) {
        if (theAnswer.equals("")) {
            throw new IllegalArgumentException("Player answer must not be empty");
        }
    }

    public void newGame(final int theWidth, final int theHeight) {
        if (theWidth < 0 || theHeight < 0) {
            throw new IllegalArgumentException("Maze width/height must be non-negative.");
        }
    }

    public void saveGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Save file name must not be empty.");
        }
    }

    public void loadGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Load file name must not be empty.");
        }
    }

    public void exitGame() {

    }
}
