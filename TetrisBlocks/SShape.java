package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class SShape extends Tetrimino {
    public SShape(){
        super(new int[][] {{0, 1, 1}, {1, 1, 0}}, Color.green);
    }
}
