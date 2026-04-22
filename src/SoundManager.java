import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Manages audio playback for the application (e.g., Pomodoro alarm).
 */
public class SoundManager {

    private static final String SOUND_FILE = "alrm.wav";

    /**
     * Plays the alarm sound on loop.
     *
     * @return the active {@link Clip}, or {@code null} if playback failed.
     */
    public Clip playAlarm() {
        try {
            File soundFile = new File(SOUND_FILE);

            if (!soundFile.exists()) {
                System.out.println("File suara tidak ditemukan: " + SOUND_FILE);
                return null;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(100);
            return clip;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Stops and closes the given clip if it is currently running.
     *
     * @param clip the clip to stop.
     */
    public void stopAlarm(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}