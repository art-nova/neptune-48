package misc;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * Class with static methods for playing sounds in the game.
 *
 * @author Artem Novak
 */
public class AudioManager {
    private static Clip bg;
    private static Map<String, Clip> sfx = new HashMap<>();
    private static float bgVolume = 0.55f, sfxVolume = 0.75f;

    /**
     * Initializes the audio manager.
     */
    public static void init() {
        try {
            if (bg == null) bg = AudioSystem.getClip();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets current background music by NameID. Corresponding file should be located at "resources/audio/bg/<i>NameID</i>.wav")
     * <br>
     * This stops whatever track was playing before!
     *
     * @param nameID NameID of the track
     */
    public static void setBG(String nameID) {
        try {
            File file = new File("resources/audio/bg/" + nameID + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            bg.close();
            bg.open(audioInputStream);
            setClipVolume(bg, bgVolume);
            audioInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts playing background music (with looping).
     */
    public static void playBG() {
        bg.loop(Clip.LOOP_CONTINUOUSLY);
    }


    /**
     * Pauses background music, allowing to later resume from the same point.
     */
    public static void pauseBG() {
        bg.stop();
    }

    /**
     * Closes the background {@link Clip}.
     */
    public static void clearBG() {
        bg.close();
    }

    /**
     * Starts playing a sound effect identified by NameID.
     * Corresponding file should be located at "resources/audio/sfx/<i>NameID</i>.wav")
     * Effect is only played a single time.
     *
     * @param nameID NameID of the effect
     */
    public static void playSFX(String nameID) {
        try {
            if (!sfx.containsKey(nameID)) {
                File file = new File("resources/audio/sfx/" + nameID + ".wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                setClipVolume(clip, sfxVolume);
                audioInputStream.close();
                sfx.put(nameID, clip);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.flush();
                    }
                });
                clip.start();
            }
            else {
                Clip clip = sfx.get(nameID);
                clip.setFramePosition(0);
                clip.start();
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Stops and unloads all currently playing sound effects.
     */
    public static void stopSFX() {
        for (Clip clip : new ArrayList<>(sfx.values())) clip.stop();
    }

    /**
     * Returns the master volume.
     *
     * @return volume of all sounds (from 0, no sound, to 1, full sound)
     */
    public static float getBgVolume() {
        return bgVolume;
    }

    /**
     * Sets volume of the background music on the scale from 0 to 1.
     *
     * @param bgVolume volume, from 0 to 1
     */
    public static void setBgVolume(float bgVolume) {
        if (bgVolume < 0 || bgVolume > 1) throw new IllegalArgumentException("Trying to set illegal background music volume " + bgVolume);
        AudioManager.bgVolume = bgVolume;
        if (bg.isOpen()) setClipVolume(bg, bgVolume);
    }

    /**
     * Sets volume of the sound effects on the scale from 0 to 1.
     *
     * @param sfxVolume volume, from 0 to 1
     */
    public static void setSfxVolume(float sfxVolume) {
        if (sfxVolume < 0 || sfxVolume > 1) throw new IllegalArgumentException("Trying to set illegal sound effect volume " + sfxVolume);
        AudioManager.sfxVolume = sfxVolume;
        for (Clip clip : new ArrayList<>(sfx.values())) setClipVolume(clip, sfxVolume);
    }

    /**
     * Sets clip's volume on a scale from 0 (no volume) to 1 (default volume).
     *
     * @param clip the clip
     * @param volume the volume value
     */
    public static void setClipVolume(Clip clip, float volume) {
        if (!clip.isOpen()) throw new IllegalArgumentException("Cannot set volume for an inactive clip!");
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(control.getMinimum() * (1 - volume));
    }
}
