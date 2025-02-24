package org.foxesworld.lvm.sound;

import org.foxesworld.lvm.resourceLoader.IResourceLoader;
import org.foxesworld.lvm.resourceLoader.ResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoundPlayer implements IResourceLoader {

    private static final Logger logger = LogManager.getLogger(SoundPlayer.class);
    private static final Map<String, Clip> activeClips = new ConcurrentHashMap<>();

    public SoundPlayer() {
    }

    /**
     * Plays a sound file with the specified volume.
     *
     * @param soundFile the name of the sound file located in the "assets/sounds/" directory
     * @param volume    volume level (linear value, where 1.0 is the original volume)
     */
    public void playSound(String soundFile, float volume) {
        // Constructing the path relative to the base directory (assets/)
        String resourcePath = "sounds/" + soundFile;
        Clip clip = null;

        logger.debug("Attempting to play sound: {} with volume: {}", soundFile, volume);

        try {
            // Load the audio stream using the universal resource loader
            AudioInputStream ais = this.loadResource(resourcePath, AudioInputStream.class);
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Atomically remove any existing clip for this resource
            activeClips.compute(resourcePath, (key, oldClip) -> {
                if (oldClip != null && oldClip.isRunning()) {
                    logger.debug("Stopping previous instance of sound: {}", soundFile);
                    oldClip.stop();
                }
                return null;
            });

            // Set a listener to remove the clip from the map after playback finishes
            Clip finalClip = clip;
            String finalResourcePath = resourcePath;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    logger.debug("Playback completed for sound: {}", soundFile);
                    activeClips.remove(finalResourcePath, finalClip);
                    finalClip.close();
                }
            });

            // Adjust volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float targetdB = calculateTargetDecibels(volume, gainControl);
            gainControl.setValue(targetdB);
            logger.debug("Volume set to: {} dB for sound: {}", targetdB, soundFile);

            // Insert the new clip and start playback
            activeClips.put(resourcePath, clip);
            clip.start();
            logger.info("Playing sound: {}", soundFile);
        } catch (Exception e) {
            if (clip != null) {
                clip.close();
            }
            logger.error("Failed to play sound: {}", soundFile, e);
        }
    }

    /**
     * Converts a linear volume value to decibels, considering the control limits.
     *
     * @param volume      linear volume value
     * @param gainControl the gain control handler
     * @return the calculated volume level in decibels
     */
    private float calculateTargetDecibels(float volume, FloatControl gainControl) {
        float minGain = gainControl.getMinimum();
        float maxGain = gainControl.getMaximum();
        if (volume <= 0.0f) {
            return minGain;
        }
        float dB = (float) (20.0 * Math.log10(volume));
        return Math.max(minGain, Math.min(maxGain, dB));
    }

    @Override
    public <T> T loadResource(String resourcePath, Class<T> type) {
        logger.debug("Loading resource: {} as {}", resourcePath, type.getSimpleName());
        return new ResourceLoader().loadResource(resourcePath, (Class<T>) AudioInputStream.class);
    }
}
