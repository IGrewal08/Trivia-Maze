package controller;

import model.Direction;
import model.Door;
import model.GameState;
import model.GameStatus;
import model.Position;
import view.GameView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controller to connect the Trivia Maze's view to it's model and database by validating
 * user information such as inputs for trivia questions or movement input into the model for
 * storage and game logic
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class GameController {
    /* Model entry point to store player information */
    private GameState myState;
    /* View entry point to store events and swing GUI */
    private GameView myView;

    /**
     * Constructor to initialize a GameState and a GameView to initialize and build the
     * trivia maze required objects. 
     * @param theState the model for the trivia maze holds all data
     * @param theView the view for the trivia maze holds java swing
     */
    public GameController(final GameState theState, final GameView theView) {
        if (theState == null || theView == null) {
            throw new IllegalArgumentException("GameState or GameView must not be null.");
        }
        this.myState = theState;
        this.myView = theView;
    }

    /**
     * Handles any player movement to update player position and room status.
     * @param theDir the current direction the player is looking at
     */
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

    /**
     * Handles any of the players answers, true/false, multiple-choice, and short answer.
     * @param theAnswer the answer given from the player.
     */
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

    /**
     * Creates a new game by re-initializing the GameModel and GameView and building a
     * custom matrix for the Trivia Maze.
     * @param theWidth the new width of the matrix for the Maze.
     * @param theHeight the new height of the matrix for the Maze.
     */
    public void newGame(final int theWidth, final int theHeight) {
        if (theWidth < 0 || theHeight < 0) {
            throw new IllegalArgumentException("Maze width/height must be non-negative.");
        }
        myState = new GameState(theWidth, theHeight);
    }

    /**
     * Save serialized data (classes with serialization) into a file to create a save record.
     * @param theFileName name for the save file for this current iteration of the game.
     */
    public void saveGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Save file name must not be empty.");
        }
        // Call gameSaver myState.setSave(theFileName);
        try (FileOutputStream fileOut = new FileOutputStream(theFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(myState);
                System.out.println("Serialized to file: " + theFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Load and deserialize data for a saved game file to rebuild any progress the player made.
     * @param theFileName name to search for the saved file to be loaded from.
     */
    public void loadGame(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Load file name must not be empty.");
        }
        try (FileInputStream fileIn = new FileInputStream(theFileName);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
                myState = (GameState) in.readObject();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    /**
     * Exit the game and close the program, creates a game save to serialized any player data.
     */
    public void exitGame() {
        saveGame("GameSave"); // will overwrite any other game save with same name
        //myView.closeWindow();
    }
}
