package misc;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.*;

public class AudioManager {
    private Clip bg;
    private Map<String, Clip> sfx = new HashMap<>();
    private float bgVolume = 0.55f, sfxVolume = 0.75f;

    public AudioManager() {
        try {
            bg = AudioSystem.getClip();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Sound error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets current background music by NameID. Corresponding file should be located at "resources/audio/bg/<i>NameID</i>.wav")
     * <br>
     * This stops whatever track was playing before!
     *
     * @param nameID NameID of the track
     */
    public void setBG(String nameID) {
        try {
            File file = new File("resources/audio/bg/" + nameID + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            bg.close();
            bg.open(audioInputStream);
            setClipScaleVolume(bg, bgVolume);
            audioInputStream.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Sound error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Starts playing background music (with looping).
     */
    public void playBG() {
        bg.loop(Clip.LOOP_CONTINUOUSLY);
    }


    /**
     * Pauses background music, allowing to later resume from the same point.
     */
    public void pauseBG() {
        bg.stop();
    }

    /**
     * Closes the background {@link Clip}. Use this to unload
     */
    public void clearBG() {
        bg.close();
    }

    /**
     * Starts playing a sound effect identified by NameID.
     * Corresponding file should be located at "resources/audio/sfx/<i>NameID</i>.wav")
     * Effect is only played a single time.
     *
     * @param nameID NameID of the effect
     */
    public void playSFX(String nameID) {
        try {
            if (sfx.containsKey(nameID)) sfx.get(nameID).setFramePosition(0);
            else {
                File file = new File("resources/audio/sfx/" + nameID + ".wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                setClipScaleVolume(clip, sfxVolume);
                audioInputStream.close();
                sfx.put(nameID, clip);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        sfx.remove(nameID);
                        clip.close();
                    }
                });
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
    public void stopSFX() {
        for (Clip clip : new ArrayList<>(sfx.values())) clip.stop();
    }

    /**
     * Returns the master volume.
     *
     * @return volume of all sounds (from 0, no sound, to 1, full sound)
     */
    public float getBgVolume() {
        return bgVolume;
    }

    /**
     * Sets volume of the audio on the scale from 0 to 1.
     *
     * @param bgVolume volume, from 0 to 1
     */
    public void setBgVolume(float bgVolume) {
        if (bgVolume < 0 || bgVolume > 1) throw new IllegalArgumentException("Trying to set illegal background music volume " + bgVolume);
        this.bgVolume = bgVolume;
        if (bg.isOpen()) setClipScaleVolume(bg, bgVolume);
    }

    public void setSfxVolume(float sfxVolume) {
        if (sfxVolume < 0 || sfxVolume > 1) throw new IllegalArgumentException("Trying to set illegal sound effect volume " + sfxVolume);
        this.sfxVolume = sfxVolume;
        for (Clip clip : new ArrayList<>(sfx.values())) setClipScaleVolume(clip, sfxVolume);
    }

    private void setClipScaleVolume(Clip clip, float volume) {
        if (!clip.isOpen()) throw new IllegalArgumentException("Cannot set volume for an inactive clip!");
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(control.getMinimum() * (1 - volume));
    }
}
