package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class JShape extends Tetrimino {
    public JShape(){
        super(new int[][] {{0, 1}, {0, 1}, {1, 1}}, Color.BLUE);
    }
}

