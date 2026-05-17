package view;

import controller.GameController;
import model.Direction;
import model.Question;

import javax.swing.*;
import java.awt.*;

/**
 * Basic Swing implementation of the GameView interface.
 *
 * This is an initial GUI shell for controller/view integration.
 *
 * @author Nicholas Cortes
 * @version 1.0
 */
public class GuiView extends JFrame implements GameView {

    private GameController myController;
    private JLabel myStatusLabel;
    private JTextArea myQuestionArea;

    public GuiView() {
        initialize();
    }

    @Override
    public void initialize() {
        setTitle("Trivia Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        myStatusLabel = new JLabel("Welcome to Trivia Maze!", SwingConstants.CENTER);
        myStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(myStatusLabel, BorderLayout.NORTH);

        myQuestionArea = new JTextArea("Use the direction buttons to move through the maze.");
        myQuestionArea.setEditable(false);
        myQuestionArea.setLineWrap(true);
        myQuestionArea.setWrapStyleWord(true);
        myQuestionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(new JScrollPane(myQuestionArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());

        JButton northButton = new JButton("North");
        JButton southButton = new JButton("South");
        JButton eastButton = new JButton("East");
        JButton westButton = new JButton("West");

        northButton.addActionListener(e -> move(Direction.NORTH));
        southButton.addActionListener(e -> move(Direction.SOUTH));
        eastButton.addActionListener(e -> move(Direction.EAST));
        westButton.addActionListener(e -> move(Direction.WEST));

        controlPanel.add(northButton, BorderLayout.NORTH);
        controlPanel.add(southButton, BorderLayout.SOUTH);
        controlPanel.add(eastButton, BorderLayout.EAST);
        controlPanel.add(westButton, BorderLayout.WEST);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void move(final Direction theDirection) {
        if (myController == null) {
            showMessage("Controller has not been connected yet.");
            return;
        }

        myController.handleMove(theDirection);
    }

    @Override
    public void updateView() {
        repaint();
        revalidate();
    }

    @Override
    public void showQuestions(final Question theQuestion) {
        if (theQuestion == null) {
            myQuestionArea.setText("No question available.");
        } else {
            myQuestionArea.setText(theQuestion.toString());
        }
    }

    @Override
    public void showMessage(final String theMessage) {
        myStatusLabel.setText(theMessage);
    }

    @Override
    public void setController(final GameController theController) {
        myController = theController;
    }
}