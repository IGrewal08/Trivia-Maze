package view;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import controller.GameController;
import model.Direction;

/**
 * ControlPanel to render the four directional buttons and wire each button
 * to the GameController handleMove function.
 * 
 * Action listeners are registered here as ControlPanel owns the actual buttons.
 * 
 * @author Inderdeep Grewal
 * @version 1.0
 */
public class ControlPanel extends JPanel {

    /** Controller to delegate moves. */
    private GameController myController;

    /** Map to each direction to it's corresponding JButton. */
    private Map<Direction, JButton> myDirectionButtons;

    /**
     * Constructs the ControlPanel, and creates all four directional buttons,
     * registers each button to their action listeners and add them to the JPanel with layout.
     * 
     * @param theController
     * @throws IllegalArgumentException if theController is null
     */
    public ControlPanel(final GameController theController) {
        super();
        if (theController == null) {
            throw new IllegalArgumentException("The controller must not be null.");
        }
        this.myController = theController;
        myDirectionButtons = new EnumMap<>(Direction.class);
        myDirectionButtons.put(Direction.NORTH, new JButton("North"));
        myDirectionButtons.put(Direction.SOUTH, new JButton("South"));
        myDirectionButtons.put(Direction.WEST, new JButton("West"));
        myDirectionButtons.put(Direction.EAST, new JButton("East"));

        buildButtons();
        setButtonsLayout();
    }

    /**
     * Attaches JButton for each Direction, set all buttons to enabled,
     * ensures button click sends direction ot handleMove function in myController.
     */
    private void buildButtons() {
        for (Direction dir: myDirectionButtons.keySet()) {
            JButton button = myDirectionButtons.get(dir);
            button.addActionListener(e -> myController.handleMove(dir));
        }
    }

    /**
     * Arranges the four directional button in a cross layout.
     */
    private void setButtonsLayout() {
        setLayout(new GridLayout(3, 3));

        add(new JPanel());
        add(myDirectionButtons.get(Direction.NORTH));
        add(new JPanel());
        add(myDirectionButtons.get(Direction.WEST));
        add(new JPanel());
        add(myDirectionButtons.get(Direction.EAST));
        add(new JPanel());
        add(myDirectionButtons.get(Direction.SOUTH));
        add(new JPanel());

    }

    /**
     * Enables or disables the directional button for the given direction.
     * 
     * @param theDir the direction whose button should be updated
     * @param theEnable true to enable, false to disable the button
     * @throws IllegalArgumentException if theDir is null
     */
    public void enableDirection(final Direction theDir, final boolean theEnable) {
        if (theDir == null) {
            throw new IllegalArgumentException("Must pass in a valid direction.");
        }
        myDirectionButtons.get(theDir).setEnabled(theEnable);
    }

    /**
     * Register WASD and arrow-key bindings on the root pane of the given frame.
     * Only work when in focused on the game window for key firing.
     * 
     * @param theFrame the top-level JFrame
     * @throws IllegalArgumentException if theFrame is null
     */
    public void registerKeyBindings(final JFrame theFrame) {
        if (theFrame == null) {
            throw new IllegalArgumentException("Frame must not be null.");
        }

        final int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;

        // WASD and Arrow keys
        final Object[][] bindings = {
            { KeyEvent.VK_W,     "moveNorth", Direction.NORTH },
            { KeyEvent.VK_S,     "moveSouth", Direction.SOUTH },
            { KeyEvent.VK_A,     "moveWest",  Direction.WEST  },
            { KeyEvent.VK_D,     "moveEast",  Direction.EAST  },
            { KeyEvent.VK_UP,    "moveNorth", Direction.NORTH },
            { KeyEvent.VK_DOWN,  "moveSouth", Direction.SOUTH },
            { KeyEvent.VK_LEFT,  "moveWest",  Direction.WEST  },
            { KeyEvent.VK_RIGHT, "moveEast",  Direction.EAST  },
        };

        for (final Object[] binding : bindings) {
            final int keyCode = (int) binding[0];
            final String actionKey = (String) binding[1];
            final Direction dir = (Direction) binding[2];

            theFrame.getRootPane().getInputMap(condition)
                .put(KeyStroke.getKeyStroke(keyCode, 0), actionKey);

            theFrame.getRootPane().getActionMap()
                .put(actionKey, new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent theEvent) {
                        if (myDirectionButtons.get(dir).isEnabled()) {
                            myController.handleMove(dir);
                        }
                    }
                });
        }
    }
}
