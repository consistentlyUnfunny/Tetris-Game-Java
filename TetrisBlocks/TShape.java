package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class TShape extends Tetrimino {
    public TShape(){
        super(new int[][] {{0, 1, 0}, {1, 1, 1}}, Color.magenta);
    }
}
