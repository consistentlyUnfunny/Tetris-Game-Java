package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class ZShape extends Tetrimino {
    public ZShape(){
        super(new int[][] {{1, 1, 0}, {0, 1, 1}}, Color.red);
    }
}
