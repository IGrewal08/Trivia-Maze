package tests.model;

import model.GameState;
import model.SaveFileValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for SaveFileValidator.
 *
 * These tests verify that save-file validation fails safely for missing,
 * corrupt, and incompatible files while accepting real serialized GameState
 * saves.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class SaveFileValidatorTest {

    @TempDir
    Path myTempDir;

    @Test
    void safeSaveNameAcceptsSimpleNames() {
        assertTrue(SaveFileValidator.isSafeSaveName("slot1"));
        assertTrue(SaveFileValidator.isSafeSaveName("my save"));
        assertTrue(SaveFileValidator.isSafeSaveName("slot1.save"));
    }

    @Test
    void safeSaveNameRejectsUnsafeNames() {
        assertFalse(SaveFileValidator.isSafeSaveName(null));
        assertFalse(SaveFileValidator.isSafeSaveName(""));
        assertFalse(SaveFileValidator.isSafeSaveName("   "));
        assertFalse(SaveFileValidator.isSafeSaveName("../slot1"));
        assertFalse(SaveFileValidator.isSafeSaveName("folder/slot1"));
        assertFalse(SaveFileValidator.isSafeSaveName("bad:name"));
    }

    @Test
    void defaultSaveFileAddsExtensionWhenNeeded() {
        final File saveFile = SaveFileValidator.getDefaultSaveFile("slot1");

        assertEquals("slot1.save", saveFile.getName());
        assertEquals("saves", saveFile.getParent());
    }

    @Test
    void defaultSaveFileDoesNotDuplicateExtension() {
        final File saveFile = SaveFileValidator.getDefaultSaveFile("slot1.save");

        assertEquals("slot1.save", saveFile.getName());
    }

    @Test
    void defaultSaveFileThrowsForUnsafeName() {
        assertThrows(IllegalArgumentException.class,
                () -> SaveFileValidator.getDefaultSaveFile("../slot1"));
    }

    @Test
    void validGameStateSaveCanBeLoaded() throws Exception {
        final File saveFile = myTempDir.resolve("valid.save").toFile();
        writeObject(saveFile, new GameState(2, 2));

        final Optional<GameState> loadedState =
                SaveFileValidator.readGameState(saveFile);

        assertTrue(SaveFileValidator.canLoadGameState(saveFile));
        assertTrue(loadedState.isPresent());
        assertEquals(2, loadedState.get().getMaze().getWidth());
        assertEquals(2, loadedState.get().getMaze().getHeight());
    }

    @Test
    void corruptSaveReturnsEmptyInsteadOfThrowing() throws Exception {
        final File corruptFile = myTempDir.resolve("corrupt.save").toFile();
        Files.writeString(corruptFile.toPath(), "not serialized data",
                StandardCharsets.UTF_8);

        assertFalse(SaveFileValidator.canLoadGameState(corruptFile));
        assertTrue(SaveFileValidator.readGameState(corruptFile).isEmpty());
    }

    @Test
    void wrongObjectTypeReturnsEmptyInsteadOfThrowing() throws Exception {
        final File wrongTypeFile = myTempDir.resolve("wrong.save").toFile();
        writeObject(wrongTypeFile, "not a game state");

        assertFalse(SaveFileValidator.canLoadGameState(wrongTypeFile));
        assertTrue(SaveFileValidator.readGameState(wrongTypeFile).isEmpty());
    }

    @Test
    void wrongExtensionIsNotTreatedAsSaveFile() throws Exception {
        final File textFile = myTempDir.resolve("valid.txt").toFile();
        writeObject(textFile, new GameState(2, 2));

        assertFalse(SaveFileValidator.looksLikeSaveFile(textFile));
        assertFalse(SaveFileValidator.canLoadGameState(textFile));
    }

    @Test
    void missingFileIsNotTreatedAsSaveFile() {
        final File missingFile = myTempDir.resolve("missing.save").toFile();

        assertFalse(SaveFileValidator.looksLikeSaveFile(missingFile));
        assertFalse(SaveFileValidator.canLoadGameState(missingFile));
    }

    private static void writeObject(final File theFile,
                                    final Object theObject) throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(theFile);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(theObject);
        }
    }
}
