package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class ensures all serializable classes and states are stored in a binary file export
 * This file can be used to resume from a previously saved state of the game including players positions
 * rooms and door states and overall game state.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class GameSaver {

    /**
     * To save serialized classes and it's data into a binary byte file to capture current games state
     * 
     * @param theState the current state, that contains all data fields needed to be captured
     * @param theFileName the output file name given to this file
     */
    public static void saveGame(final GameState theState, final String theFileName) {

        if (!SaveFileValidator.isSafeSaveName(theFileName)) {
            throw new IllegalArgumentException("Save file name is not valid.");
        }
        if (theState == null) {
            throw new IllegalArgumentException("GameState must not be null.");
        }

        // Check if saves directory exists, create one if it doesn't
        final File dir = new File("saves" + File.separator);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Create saves/ directory.");
            } else {
                System.err.println("Warning: could not create saves/ directory");
            }
        }

        final File saveFile = SaveFileValidator.getDefaultSaveFile(theFileName);

        try (FileOutputStream fileOut = new FileOutputStream(saveFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

                out.writeObject(theState);
                System.out.println("Serialized file to: " + saveFile.getPath());

            } catch (final IOException e) {
                System.err.println("Failed to save game: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * To retrieve the serialized binary and deserialize this file to restore game from a previous state
     *  
     * @param theFileName identity of the file that holds the serialized data
     * @return GameState/Null return the deserialized state if file is found else return null
     */
    public static GameState getSave(final String theFileName) {
        
        if (!SaveFileValidator.isSafeSaveName(theFileName)) {
            throw new IllegalArgumentException("Load file name is not valid.");
        }

        final File saveFile = SaveFileValidator.getDefaultSaveFile(theFileName);

        if (!saveFile.exists()) {
            System.out.println("No save file found at: " + saveFile.getPath());
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {

                final GameState fetchState =  (GameState) in.readObject();
                System.out.println("Game loaded from: " + saveFile.getPath());
                return fetchState;

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
        return null;
    }
}
