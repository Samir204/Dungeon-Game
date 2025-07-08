package Main_game_file;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
   private static Clip musicClip;
   private static boolean musicEnabled=true;
   private static boolean isPlaying=false;
   private static final String MUSIC_FILE= "Fantasy.wav";


   public static void initializeMusic(){
        try {
            if (musicClip == null) {
                File file = new File(MUSIC_FILE);
                if (!file.exists()) {
                    System.out.println("Music file not found: " + MUSIC_FILE);
                    return;
                }
                
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                musicClip = AudioSystem.getClip();
                musicClip.open(audioStream);
                
                musicClip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP && musicEnabled && isPlaying) {
                            musicClip.setFramePosition(0);
                            musicClip.start();
                        }
                    }
                });
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error initializing music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startMusic(){
        if (musicClip!=null && musicEnabled && !isPlaying) {
            musicClip.setFramePosition(0);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying=true;
        }
    }

    public static void stopMusicOrPause(){
        if (musicClip!=null && isPlaying) {
            musicClip.stop();
            isPlaying=false;
        }
    }

    public static void resumeMusic(){
        if (musicClip!=null && musicEnabled && !isPlaying) {
            musicClip.start();
            isPlaying=true;
        }
    }

    public static void toggleMusic(){
        if (isPlaying) {
            stopMusicOrPause();
        }
        else{
            resumeMusic();
        }
    }

    public static boolean isPlaying(){
        return isPlaying;
    }

    public static void cleanUP(){ // cleaning up, /////
        if (musicClip!=null) {
            musicClip.close();
            musicClip=null;
            isPlaying=false;
        }
    }

    @Deprecated
    public static void musicToggle(){
        toggleMusic();
    }
}
