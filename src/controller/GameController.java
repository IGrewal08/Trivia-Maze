package controller;

import model.Direction;

public class GameController {
    private GameState myState;
    private GameView myView;

    public gameController(final GameState theState, final GameView theView) {
        if (theState == null || theView == null) throw new NullPointerException("GameState or GameView should not be null.");
        this.myState = theState;
        this.myView = theView;
    }

    public void handleMove(final Direction theDir) {

    }

    public void handleAnswer(final String theAnswer) {

    }

}
