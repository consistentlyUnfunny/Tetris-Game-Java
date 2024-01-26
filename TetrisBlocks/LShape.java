package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class LShape extends Tetrimino {
    public LShape(){
        super(new int[][] {{1, 0}, {1, 0}, {1, 1}}, Color.orange);
    }
}
