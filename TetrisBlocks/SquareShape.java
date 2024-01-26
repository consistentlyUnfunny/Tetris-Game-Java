package TetrisBlocks;

import Game.Tetrimino;

import java.awt.*;

public class SquareShape extends Tetrimino {
    public SquareShape(){
        super(new int[][] {{1, 1}, {1, 1}}, Color.yellow);
    }
}
