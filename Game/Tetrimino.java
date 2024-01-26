package Game;

import java.awt.*;

public class Tetrimino {
    private int[][] shape;
    private int[][][] shapes; // store variation for rotation
    private int currentRotation; // store current rotation of block
    private Color color;
    private int x, y; // x store top left segment's x coord, y store top left segment's y coord, the coordinates of grids, not the actual pixel

    public Tetrimino(int[][] shape, Color color){
        this.shape = shape;
        this.color = color;

        initShapes();
    }

    private void initShapes(){
        shapes = new int[4][][]; // it is legal as long as the size of the array is specify, don't need to specific subarrays size

        for (int i = 0; i < 4; i++){ // generate all variation of rotation
            int row = shape[0].length; // no. of row of the block
            int col = shape.length; // no. of col or the block

            shapes[i] = new int[row][col];

            for (int y = 0; y < row; y++){
                for (int x = 0; x < col; x++){
                    shapes[i][y][x] = shape[col - x - 1][y];
                }
            }
            shape = shapes[i];
        }
    }

    public void spawn(int gridWidth){ // block start falling from above the game area
        /*
        x++ to right
        x-- to left
        y++ to down
         */
        currentRotation = 0;
        shape = shapes[currentRotation];
        // these must be set before the x and y because those value depends on these

        x = (gridWidth - getWidth()) / 2;
        y = -getHeight(); // basically 0 - height
    }

    public  int[][] getShape(){return shape;} // ** private method can only be accessed within the class

    public Color getColor(){return color;}

    public int getHeight(){return shape.length;}

    public int getWidth(){return shape[0].length;}

    public int getX(){return x;}

    public int getY(){return y;}

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void moveDown(){y++;}

    public void moveLeft(){x--;}

    public void moveRight(){x++;}

    public void rotate(){
        currentRotation++;
        if (currentRotation == 4) currentRotation = 0;
        shape = shapes[currentRotation]; // get rotation variation, max index = 3
    }
    public int getBottomEdge(){return y + getHeight();}

    public int getRightEdge(){return x + getWidth();}
    public int getLeftEdge(){return x;}
}
