package controller;

import java.util.List;
import java.util.Random;
import java.util.Set;

import model.*;
import view.GameView;

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
        this.myState.addPropertyChangeListener(theView);
    }

    /**
     * Handles any move preformed by the player before calling the model to update state or the view to repaint GUI.
     * 
     * @param theDir the direction being moved to by the player
     * @throws IllegalArgumentException if the Direction moved is null
     */
    public void handleMove(final Direction theDir) {
        if (theDir == null) {
            throw new IllegalArgumentException("Direction within handleMove must not be null.");
        }

        Position currPos = myState.getCurrentPosition();
        Position newPos = myState.getCurrentPosition().translate(theDir);

        System.out.println("Trying to move: " + theDir);
        System.out.println("Current position: " + currPos);
        System.out.println("New position would be: " + newPos);

        if (!myState.getMaze().isInBounds(newPos)) {
            myView.showMessage("You cannot move " + theDir + ". That is outside the maze.");
            myState.firePropertyChange("INVALID_MOVE", null, newPos);
            return;
        }

        Door door = myState.getMaze().getRoom(currPos).getDoor(theDir);

        Room currentRoom = myState.getMaze().getRoom(currPos);
        System.out.println("Doors in current room " + currPos + ": " + currentRoom.toString());

        if (door == null) {
            myView.showMessage("No door exists to the " + theDir + " yet.");
            myState.firePropertyChange("INVALID_MOVE", null, newPos);
            return;
        }

        if (door.isOpen()) {
            myState.setCurrentPosition(newPos);
            myState.addVisitedRoom(newPos);
            myView.showMessage("Moved " + theDir + " to " + newPos);
            myState.firePropertyChange("PLAYER_MOVED", null, newPos);
            if (newPos.equals(myState.getMaze().getExit())) {
                myState.firePropertyChange("GAME_OVER", null, GameStatus.WON);
            }
        } else if (door.isLocked()) {
            myState.setCurrentDirection(theDir);
            myView.showMessage("Door is locked. Answer the question to move " + theDir + ".");
            myView.showQuestions(door.getQuestion());
            myState.firePropertyChange("QUESTION_ASKED", null, door.getQuestion());
        } else if (door.isBlocked()) {
            myView.showMessage("That door is blocked.");
            myState.firePropertyChange("INVALID_MOVE", null, newPos);
        }
    }

    /**
     * Handles any of the players answers, true/false, multiple-choice, and short answer.
     * @param theAnswer the answer given from the player.
     */
    public void handleAnswer(final String theAnswer) {
        if (theAnswer == null || theAnswer.isBlank()) {
            throw new IllegalArgumentException("Player answer must not be blank or null.");
        }
        Direction dir = myState.getCurrentDirection();
        Position currPos = myState.getCurrentPosition();
        Position newPos = myState.getCurrentPosition().translate(dir);

        Door door = myState.getMaze().getRoom(currPos).getDoor(dir);

        if (door.attemptUnlock(theAnswer)) {
            door.unlock();
            myState.setCurrentPosition(newPos);
            if (newPos.equals(getMaze().getExit())) {
                myState.firePropertyChange("GAME_OVER", null, GameStatus.WON);
                return;
            }
            myState.addVisitedRoom(newPos);
            myState.firePropertyChange("PLAYER_MOVED", null, newPos);
            myState.firePropertyChange("ANSWER_RESULT", null, true);
        } else {
            door.block();
            myState.firePropertyChange("ANSWER_RESULT", null, false);
            myState.firePropertyChange("MAZE_UPDATED", null, null);
        }

        if (!myState.getMaze().isPathPossible(myState.getCurrentPosition())) {
            myState.firePropertyChange("GAME_OVER", null, GameStatus.LOST); // TODO build Stats ENUM for game update per event
        }
        
    }

    /**
     * Returns the room the player is currently in.
     * Called by RoomPanel to get the room to render after a move.
     * @return the player's current Room
     */
    public Room getCurrentRoom() {
        return myState.getMaze().getRoom(myState.getCurrentPosition());
    }

    /**
     * Returns the maze the current game is in.
     * Called by MapPanel to get the maze to render after each move.
     * @return the maze for the game
     */
    public Maze getMaze() {
        return myState.getMaze();
    }

    /**
     * Returns the current position of the player.
     * Called by MapPanel to render player on the maze map.
     * @return the player's current position
     */
    public Position getCurrentPosition() {
        return myState.getCurrentPosition();
    }

    /**
     * Returns the current visited rooms by the player.
     * Called by MapPanel to render and differentiate visited rooms.
     * @return the player's visited rooms
     */
    public Set<Position> getVisitedRooms() {
        return myState.getVisitedRooms();
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
        myState.addPropertyChangeListener(myView);

        int needed = (theWidth - 1) * theHeight + theWidth * (theHeight -1);
        List<Question> questions = QuestionFactory.getRandomQuestions(needed);

        System.out.println("Questions loaded: " + questions.size());
        myState.getMaze().fillRoomsWithQuestions(questions, new Random());

        // Verify doors were actually wired
        Room startRoom = myState.getMaze().getRoom(new Position(0, 0));
        System.out.println("Start room doors after fill: " + startRoom.toString());

        myState.setCurrentPosition(myState.getMaze().getEntrance());
        myState.firePropertyChange("MAZE_UPDATED", null, myState.getMaze());
        System.out.println("New game started: " + theWidth + "x" + theHeight);
    }

    /**
     * Save serialized data (classes with serialization) into a file to create a save record.
     * @param theFileName name for the save file for this current iteration of the game.
     */
    public void saveGame(final String theFileName) {
        if (theFileName.isBlank()) {
            throw new IllegalArgumentException("Save file name must not be empty.");
        }
        GameSaver.saveGame(myState, theFileName);
    }

    /**
     * Load and deserialize data for a saved game file to rebuild any progress the player made.
     * @param theFileName name to search for the saved file to be loaded from.
     */
    public void loadGame(final String theFileName) {
        if (theFileName == null || theFileName.isBlank()) {
            throw new IllegalArgumentException("Load file name must not be empty.");
        }
        GameState loaded = GameSaver.getSave(theFileName);
        if (loaded != null) {
            myState = loaded;
            myState.addPropertyChangeListener(myView);
            myState.firePropertyChange("MAZE_UPDATED", null, myState.getMaze());
        }
    }

    /**
     * Exit the game and close the program, creates a game save to serialized any player data.
     */
    public void exitGame() {
        saveGame("GameSave"); // will overwrite any other game save with same name
        myView.closeGame();
    }

	/**
     * Returns whether the given direction is a valid move from
     * the player's current position — door exists and is not blocked.
     * Called by GuiView to update the control panel buttons.
     *
     * @param theDir the direction to check
     * @return true if the player can attempt to move that direction
     */
    public boolean isDirectionAvailable(final Direction theDir) {
        Room currentRoom = myState.getMaze().getRoom(myState.getCurrentPosition());
        Door door = currentRoom.getDoor(theDir);
        return door != null && !door.isBlocked();
    }
}
