package TetrisBlocks;
import Game.Tetrimino;

import java.awt.*;

public class IShape extends Tetrimino{
    public IShape(){
        super(new int[][] {{1}, {1}, {1}, {1}}, Color.CYAN);
        // call constructor of super class (Tetrimino) and pass in the structure of this block
        // this class can't access private member of superclass but the values are still stored in this class

    }
    @Override
    public void rotate(){ // override rotate method because i shape rotates differently
        super.rotate(); // maintain the functionality of super class
        if (this.getWidth() == 1){ // vertical to horizontal
            this.setX(this.getX() + 1);
            this.setY(this.getY() - 1);
        } else { // horizontal to vertical
            this.setX(this.getX() - 1);
            this.setY(this.getY() + 1);
        }


    }
}
