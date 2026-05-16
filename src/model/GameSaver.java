package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import controller.GameState;

public class GameSaver {

    public static void saveGame(final GameState theState, final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Save file name must not be empty.");
        }
        try (FileOutputStream fileOut = new FileOutputStream(theFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(theState);
                System.out.println("Serialized to file: " + theFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static GameState getSave(final String theFileName) {
        if (theFileName.equals("")) {
            throw new IllegalArgumentException("Load file name must not be empty.");
        }
        GameState fetchState = null;
        try (FileInputStream fileIn = new FileInputStream(theFileName);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
                fetchState = (GameState) in.readObject();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
        return fetchState;
    }
}
