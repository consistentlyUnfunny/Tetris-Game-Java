package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicThread implements Runnable{
    private Clip soundClip;
    private Clip gameOverSound;
    @Override
    public void run() {
        try{
            // load audio file
            File audioFile = new File("src/TetrisTheme.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);


            // create a Clip and open the audio stream
            soundClip = AudioSystem.getClip(); //.getclip() creates and return a clip object
            soundClip.open(audioInputStream);

            // play clip in loop
            soundClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {// use | (bitwise operator) in exception handling instead of || (logical operator)
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        if (soundClip != null && soundClip.isRunning()){
            soundClip.stop();
            soundClip.close();
            try{
                File audioFile = new File("src/tiktok-snore.wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

                gameOverSound = AudioSystem.getClip();
                gameOverSound.open(audioInputStream);

                gameOverSound.start();
            } catch (UnsupportedAudioFileException| IOException | LineUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
