package org.foxesworld.lvm.sound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoundPlayer {

    private static final Logger logger = LogManager.getLogger(SoundPlayer.class);
    private static final Map<String, Clip> activeClips = new ConcurrentHashMap<>();

    private final SoundResourceLoader resourceLoader = new SoundResourceLoader();

    public SoundPlayer() {
    }

    public void playSound(String soundFile, float volume) {
        String resourcePath = "sounds/" + soundFile;
        Clip clip = null;

        logger.debug("Attempting to play sound: {} with volume: {}", soundFile, volume);

        try {
            AudioInputStream ais = resourceLoader.loadAudioStream(resourcePath);
            clip = AudioSystem.getClip();
            clip.open(ais);

            activeClips.compute(resourcePath, (key, oldClip) -> {
                if (oldClip != null && oldClip.isRunning()) {
                    logger.debug("Stopping previous instance of sound: {}", soundFile);
                    oldClip.stop();
                }
                return null;
            });

            Clip finalClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    logger.debug("Playback completed for sound: {}", soundFile);
                    activeClips.remove(resourcePath, finalClip);
                    finalClip.close();
                }
            });

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float targetdB = calculateTargetDecibels(volume, gainControl);
            gainControl.setValue(targetdB);
            logger.debug("Volume set to: {} dB for sound: {}", targetdB, soundFile);

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

    private float calculateTargetDecibels(float volume, FloatControl gainControl) {
        float minGain = gainControl.getMinimum();
        float maxGain = gainControl.getMaximum();
        if (volume <= 0.0f) {
            return minGain;
        }
        float dB = (float) (20.0 * Math.log10(volume));
        return Math.max(minGain, Math.min(maxGain, dB));
    }
}
