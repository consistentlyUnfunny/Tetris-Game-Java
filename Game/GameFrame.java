package Game;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel gameOverLabel;
    private Font font;
    private JButton pauseButton;
    private GameArea gameArea;
    public GameFrame(){
        //setup window
        setTitle("Tetris");
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.gray);

        //set layout
        setLayout(null);

        font = new Font("DialogInput", Font.BOLD, 18);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(font);
        scoreLabel.setBounds(150, 0, 100, 50);

        levelLabel = new JLabel("Level: 1");
        levelLabel.setFont(font);
        levelLabel.setBounds(450, 0, 100, 50);

        gameOverLabel = new JLabel("");
        gameOverLabel.setFont(new Font("DialogInput", Font.BOLD, 40));
        gameOverLabel.setForeground(Color.BLACK);
        gameOverLabel.setBounds(275, 275, 250, 50);

        gameArea = new GameArea();

        add(scoreLabel);
        add(levelLabel);
        add(gameOverLabel);
        add(gameArea);
        controls();

        setVisible(true);

    }

    private void controls(){
        InputMap inputMap = this.getRootPane().getInputMap(); // to add keystroke, map keystorke to input events
        ActionMap actionMap = this.getRootPane().getActionMap(); // add action for keystroke, represent an action to be performed
        // input map and action map need to work together to achieve the output

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right"); // parameters: keybind, action map key name
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");



        actionMap.put("right", new AbstractAction() {
            // anonymous instantiation, works for abstract class (which cannot be instantiated), because this method
            // actually create a subclass (anonymously) and override the method in superclass
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.moveBlockRight();
            }
        });
        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.moveBlockLeft();
            }
        });
        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.rotateBlock();
            }
        });
        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.dropBlock();
            }
        });

    }

    public void startGame(){
        GameThread gameThread = new GameThread(gameArea, this);
        Thread thread = new Thread(gameThread); // main game thread
        thread.start();
    }

    public void updateScore(int score){
        scoreLabel.setText("Score: " + score);
    }

    public void updateLevel(int level){
        levelLabel.setText("Level: " + level);
    }

    public void updateGameStatus(){gameOverLabel.setText("Game Over");}



    public static void main(String[] args){
        new GameFrame().startGame();
    }
}
