package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

/**
 * Utility methods for checking Trivia Maze save files before loading them.
 *
 * This class is separate from GameSaver so save-file checks can be tested and
 * reused without changing the current save/load workflow.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public final class SaveFileValidator {

    /** Standard extension used by Trivia Maze save files. */
    public static final String SAVE_EXTENSION = ".save";

    /**
     * Utility class; do not instantiate.
     */
    private SaveFileValidator() {
    }

    /**
     * Returns whether a save name is safe to use as a local file name.
     *
     * The name must not be blank, must not contain path separators, and must
     * not contain characters that are invalid on Windows. This keeps save
     * names portable across Windows and macOS.
     *
     * @param theSaveName proposed save name without a path
     * @return true if the name is safe for a save file
     */
    public static boolean isSafeSaveName(final String theSaveName) {
        if (theSaveName == null || theSaveName.isBlank()) {
            return false;
        }

        final String trimmed = theSaveName.trim();
        final String invalidCharacters = "\\/:*?\"<>|";

        for (int i = 0; i < trimmed.length(); i++) {
            if (invalidCharacters.indexOf(trimmed.charAt(i)) >= 0) {
                return false;
            }
        }

        return !trimmed.equals(".") && !trimmed.equals("..");
    }

    /**
     * Builds the default save-file path used by GameSaver.
     *
     * @param theSaveName save name without extension
     * @return file in the saves directory with the .save extension
     * @throws IllegalArgumentException if theSaveName is not safe
     */
    public static File getDefaultSaveFile(final String theSaveName) {
        if (!isSafeSaveName(theSaveName)) {
            throw new IllegalArgumentException("Save file name is not safe.");
        }

        final String fileName = theSaveName.trim().endsWith(SAVE_EXTENSION)
                ? theSaveName.trim()
                : theSaveName.trim() + SAVE_EXTENSION;

        return new File("saves", fileName);
    }

    /**
     * Returns whether the given file exists and uses the expected save-file
     * extension.
     *
     * @param theFile file to check
     * @return true if the file looks like a Trivia Maze save file
     */
    public static boolean looksLikeSaveFile(final File theFile) {
        return theFile != null
                && theFile.isFile()
                && theFile.getName().endsWith(SAVE_EXTENSION);
    }

    /**
     * Returns whether the given file can be deserialized as a GameState.
     *
     * Corrupt files, missing files, incompatible classes, and files containing
     * another object type all return false instead of throwing. This makes the
     * method safe to call before showing a load option to the user.
     *
     * @param theFile save file to check
     * @return true if the file contains a GameState
     */
    public static boolean canLoadGameState(final File theFile) {
        return readGameState(theFile).isPresent();
    }

    /**
     * Attempts to read a GameState from the given save file.
     *
     * @param theFile save file to read
     * @return Optional containing the GameState, or empty if invalid
     */
    public static Optional<GameState> readGameState(final File theFile) {
        if (!looksLikeSaveFile(theFile)) {
            return Optional.empty();
        }

        try (FileInputStream fileIn = new FileInputStream(theFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            final Object loadedObject = in.readObject();
            if (loadedObject instanceof GameState) {
                return Optional.of((GameState) loadedObject);
            }

        } catch (final IOException | ClassNotFoundException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
