package Game;

import Game.GameArea;
import Game.GameFrame;

public class GameThread implements Runnable{
    private GameArea gameArea;
    private GameFrame gameFrame;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;
    private boolean gameRunning = true;

    private int pauseInterval = 1000;
    private int speedUpPerLevel = 100;

    public GameThread(GameArea gameArea, GameFrame gameFrame){
        this.gameFrame = gameFrame;
        this.gameArea = gameArea;
    }

    @Override
    public void run() {
        while (true){
            MusicThread musicThread = new MusicThread();
            Thread thread1 = new Thread(musicThread); // bg music thread
            thread1.start();

            while(gameRunning){
                gameArea.spawnBlock();
                while(gameArea.moveBlockDown()){
                    try {
                        Thread.sleep(pauseInterval);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
                if (gameArea.isBlockOutOfBounds()){
                    gameFrame.updateGameStatus();
                    break;
                }

                gameArea.moveBlockToBackground();
                score += gameArea.clearLines(); // score is the total line cleared

                gameFrame.updateScore(score);

                int lvl = score / scorePerLevel + 1; // 3 clear = +1 lvl
                if (lvl > level){
                    level = lvl;
                    gameFrame.updateLevel(level);
                    pauseInterval -= speedUpPerLevel;
                }
            }

            musicThread.stopMusic(); // stop music after game over
            break;
        }


    }
}
