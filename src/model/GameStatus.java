package model;

import java.io.Serializable;

/**
 * Represents the current status of the Trivia Maze game.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public enum GameStatus implements Serializable {
    ACTIVE,
    WON,
    LOST
}