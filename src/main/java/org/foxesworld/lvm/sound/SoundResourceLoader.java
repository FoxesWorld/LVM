package org.foxesworld.lvm.sound;

import org.foxesworld.lvm.resourceLoader.IResourceLoader;
import org.foxesworld.lvm.resourceLoader.ResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;

public class SoundResourceLoader implements IResourceLoader {

    private static final Logger logger = LogManager.getLogger(SoundResourceLoader.class);

    @Override
    public <T> T loadResource(String resourcePath, Class<T> type) {
        logger.debug("Loading resource: {} as {}", resourcePath, type.getSimpleName());
        return new ResourceLoader().loadResource(resourcePath, type);
    }

    public AudioInputStream loadAudioStream(String resourcePath) {
        return loadResource(resourcePath, AudioInputStream.class);
    }
}
