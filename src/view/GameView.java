package view;

import controller.GameController;
import model.Question;

/**
 * GuiView implements GameView interface to define the required functions of the view
 * ensures GuiView contains strict functionality.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public interface GameView {

    /**
     * method to initialize all GUI component to build the JFrame
     */
    public void initialize();

    /**
     * Updates view from PropertyChangeListener upon player action
     */
    public void updateView();

    /**
     * Prompts the player with question GUI from SQLite database upon selecting a direction
     * @param theQuestion the question to be displayed to the player through the Swing GUI
     */
    public void showQuestions(final Question theQuestion);

    /**
     * Shows a message to the player for different response to player actions
     * @param theMessage the display message for the player through the Swing GUI
     */
    public void showMessage(final String theMessage);

    /**
     * Function to connect the view to the controller in-order to manipulate the model
     * @param theController the controller connecting this view to it's model
     */
    public void setController(final GameController theController);
}
