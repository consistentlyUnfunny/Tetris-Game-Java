package Game;

import TetrisBlocks.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameArea extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 480;

    private int gridRows; // number of rows
    private static final int gridColumns = 10; // number of columns
    private int gridCellSize; // width or height divided by row or column
    private Color[][] background; // to display the fallen block, foreground is used to display falling block, treat all fallen block as an object
    private Tetrimino block;
    private Tetrimino[] blocks;

    public GameArea() {
        setBounds(150, 50, WIDTH, HEIGHT);
        setBorder(new LineBorder(Color.BLACK, 3));

        gridCellSize = this.getBounds().width / gridColumns; // size per side of cell
        gridRows = this.getBounds().height / gridCellSize;

        background = new Color[gridRows][gridColumns]; // 2d array of colors with size of the total grid, in this case [12][10]

        blocks = new Tetrimino[]{new IShape(),new JShape(), new SquareShape(), new ZShape(), new SShape(), new TShape(), new LShape()};
        // add tetris blocks to array for randomization

    }

    public boolean moveBlockDown(){
        if (!checkBottom()) { // if block touched the bottom
            return false;
        }
        block.moveDown();
        repaint(); // repaint calls the paintComponent again, ultimately redrawing the tetris in new position
        return true; // still falling
    }

    public void moveBlockLeft(){
        if (block == null) return; // game over
        if (!checkLeft()) return; // do nothing if unable to go left
        block.moveLeft();
        playSound("src/mixkit-arcade-game-jump-coin-216.wav");
        repaint(); // without repaint, it lags because it has to wait until the moveBlockDown() calls it
        // change x value coord which will be used to in repaint(),
    }

    public void moveBlockRight(){
        if (block == null) return; // game over
        if (!checkRight()) return; // do nothing if cant move right
        block.moveRight(); // change x value coord which will be used to in repaint()
        playSound("src/mixkit-arcade-game-jump-coin-216.wav");
        repaint();
    }

    public void dropBlock(){
        if (block == null) return; // game over
        while(checkBottom()){ // repeat until the block can't move down anymore
            block.moveDown();
        }
        playSound("src/mixkit-arcade-game-jump-coin-216.wav");
        repaint();
    }

    public void rotateBlock(){
        if (block == null) return; // game over
        if (checkCollision() == false) return;
        block.rotate();
        // prevent from rotating out of bounds
        if (block.getLeftEdge() < 0) block.setX(0);
        if (block.getRightEdge() >= gridColumns) block.setX(gridColumns - block.getWidth());
        if (block.getBottomEdge() >= gridRows) block.setY(gridRows - block.getHeight());
        playSound("src/mixkit-arcade-game-jump-coin-216.wav");
        repaint();
    }


    private boolean checkBottom(){ // check if block can move down
        if (block.getBottomEdge() == gridRows) return false; // can't go down anymore
        // check if any blocks under this one
        int[][] shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();

        for (int col = 0; col < w; col++){
            for (int row = h - 1; row >=0; row--){
                // h - 1 instead of h because we're trying to get the index, not the actual value,
                // index is used to access the element in the array later
                if (shape[row][col] != 0){ // traverse the shape in reverse (clockwise)
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if (y > 0) { // y is negative when it spawns, if so, give up checking the column
                        if (background[y][x] != null) return false; // if there's block under this one, it can't go down anymore
                    }
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkCollision() { // check if rotation causes collision sideways
        int[][] shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();

        for (int col = 0; col < w; col++) {
            for (int row = h - 1; row >= 0; row--) {
                if (shape[row][col] != 0) { // this is one grid, not whole shape
                    int x = col + block.getX(); // get absolute coord of x grid
                    int y = row + block.getY(); // get absolute coord of y grid, +1 checks the cell direcly below to the current one
                    if (y >= 0) { // if block in the play field and not above
                        if (x + 1 < gridColumns && background[y][x + 1] != null) return false; // can't rotate to side
                        if (x - 1 >= 0 && background[y][x - 1] != null) return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean checkRight(){
        if (block.getRightEdge() == gridColumns) return false;// return false if can't move right anymore

        int[][] shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();

        for (int row = 0; row < h; row++){
            for (int col = w - 1; col >=0; col--){
                if (shape[row][col] != 0){
                    int x = col + block.getX() + 1;
                    int y = row + block.getY();
                    if (y >= 0) { // y is negative when it spawns, if so, give up checking the column
                        if (background[y][x] != null) return false; // if there's block under this one, it can't go down anymore
                    }
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkLeft(){
       if (block.getLeftEdge() == 0) return false; // return false if unable to move left

        int[][] shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();

        for (int row = 0; row < h; row++){
            for (int col = 0; col < w; col++){
                if (shape[row][col] != 0){
                    int x = col + block.getX() - 1;
                    int y = row + block.getY();
                    if (y >= 0) { // y is negative when it spawns, if so, give up checking the column
                        if (background[y][x] != null) return false; // if there's block under this one, it can't go down anymore
                    }
                    break;
                }
            }
        }
        return true;
    }

    public int clearLines(){
        boolean lineFilled; // not given a value here because it needs to be set back to true every loop
        int linesCleared = 0;
        for (int row = gridRows - 1; row >= 0; row--){ // traverse background grids bottom up
            lineFilled = true;
            for (int col = 0; col < gridColumns; col++){
                if (background[row][col] == null){ // stop traversing the row if a blank grid is found
                    lineFilled = false;
                    break;
                }
            }
            if (lineFilled){ // if line is filled, line will be cleared
                linesCleared++; // increment everytime a line is cleared
                clearLine(row);
                shiftDown(row); // shift the whole background downwards
                clearLine(0);
                row++; // need to increment back because it is supposed to check the same line again after moving down (since the line is moved down)
                // after shifting down, clear the top most line, it can't be accomplished by shift down due to potential out of bound error during the for loop
                repaint(); // redraw the new background
            }
        }
        return linesCleared;
    }

    private void clearLine(int row){ // clear line
        for (int i = 0; i < gridColumns; i++){
            playSound("src/mixkit-video-game-lock-2851.wav");
            background[row][i] = null; // set all grid value of column to null
        }
    }

    private void shiftDown(int row){ // shift the whole background downwards
        for (; row > 0; row--){
            // traverse from row (a lower point) up to grid 1 (second line),
            // r > 0 doesn't move the top line, if r >= 0 then it might cause out of bound exception, therefore the top line will be handled by clear line
            // since it doesn't have to move the upper row down anymore
            for (int col = 0; col < gridColumns; col++){
                background[row][col] = background[row - 1][col];
                // row - 1 is the row above the current one, so it shift the upper row down, while col remains the same
            }
        }


    }

    public boolean isBlockOutOfBounds(){// return true if out of bounds, false if still in bound (if not in play field, above it)
        int[][] shape = block.getShape();
        int x = block.getX();
        int y = block.getY();

        for (int i = 0; i < shape.length; i++){
            for (int j = 0; j < shape[i].length; j++){
                int row = y + i;
                int col = x + j;
                if (shape[i][j] != 0 && (col < 0 || col >= gridColumns || row >= gridRows || row < 0)){
                    block = null;
                    return true;
                }
            }
        }
        return false;
    }
    public void moveBlockToBackground(){
        if (isBlockOutOfBounds()){
            return;
        }
        int[][] shape = block.getShape();
        int h = block.getHeight();
        int w = block.getWidth();

        int xCoord = block.getX();
        int yCoord = block.getY();

        Color color = block.getColor();

        for (int row = 0; row < h; row++){
            for (int col = 0; col < w; col++){
                int bgrow = row + yCoord;
                int bgcol = col + xCoord;
                if (shape[row][col] == 1 && bgrow >= 0 && bgrow < gridRows && bgcol >= 0 && bgcol < gridColumns){ // if there's color on the block/grid
                    background[row + yCoord][col + xCoord] = color; // set the grid on background to color of the block
                }
            }
        }

    }

    public void spawnBlock(){ // spawn the block, does not paint it yet
        Random random = new Random();
        block = blocks[random.nextInt(blocks.length)]; // random generate number from 0(inclusive) - n (exclusive)
        block.spawn(gridColumns);
    }

    private void drawBlock(Graphics g) { // draw a tetrimino, in the foreground
        if (block == null) return;
        int h = block.getHeight();
        int w = block.getWidth();
        int[][] shape = block.getShape();
        Color color = block.getColor();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                if (shape[row][col] == 1) {
                    // get coords
                    int x = (block.getX() + col) * gridCellSize;
                    int y = (block.getY() + row) * gridCellSize;
                    drawGridSquare(g, color, x, y);
                }

            }
        }
    }

    private void drawBackground(Graphics g){ // called during repaint()/ paintComponent, draw the background to keep track of fallen blocks
        Color color;
        for (int row = 0; row < gridRows; row++){
            for (int col = 0; col < gridColumns; col++){
                color = background[row][col];
                if (color != null){
                    // get coords
                    int x = col * gridCellSize;
                    int y = row * gridCellSize;
                    drawGridSquare(g, color, x, y);
                }
            }
        }
    }

    private void drawGridSquare(Graphics g, Color color, int x, int y){ // draw individual grid for foreground and background
        g.setColor(color); // set color to fill grid
        g.fillRect(x, y, gridCellSize, gridCellSize);
        g.setColor(Color.BLACK); // set color back to black before drawing grid
        g.drawRect(x, y, gridCellSize, gridCellSize);
    }

    private void playSound(String path) {
        try {
            // load audio file
            File audioFile = new File(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audioInputStream);

            soundClip.start();
            audioInputStream.close();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) { // g represent a graphic object, it is needed to be used to draw
        /*
        paintCompoenent is a special method that is automatically called by swing when component needs to be rendered or updated
        this is also the place where all the rendering or drawing code is place, if these code are written outside of this method,
        those operation will need to be call explicitly whenever the component needs to be updated (initial appearance, repaint etc.)

         */
        super.paintComponent(g); // ensures that components' background and other default painting are properly handled before executing this

        /*
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridColumns; j++) { // draw column grid
                g.drawRect(j * gridCellSize, i * gridCellSize, gridCellSize, gridCellSize);
                // using i as y value directly does not work because i is only incremented by 1 each loop
            }
        }

         */
        drawBackground(g);
        drawBlock(g);
    }
}
