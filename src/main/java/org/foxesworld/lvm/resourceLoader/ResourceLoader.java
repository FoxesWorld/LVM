package org.foxesworld.lvm.resourceLoader;

import de.jarnbjo.vorbis.VorbisAudioFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Universal resource loader.
 * <p>
 * Supports loading various types of resources:
 * - Text files → {@link String}
 * - Binary files → {@code byte[]}
 * - Audio files (OGG) → {@link AudioInputStream}
 * </p>
 */
public class ResourceLoader implements IResourceLoader {

    private static final String ASSET_BASE_PATH = "assets/";
    private static final Logger logger = LogManager.getLogger(ResourceLoader.class);

    /**
     * Registers converters for different resource types.
     */
    private static final Map<Class<?>, IResourceConverter<?>> converters = new HashMap<>();

    static {
        // Converter for text resources (UTF-8)
        converters.put(String.class, (IResourceConverter<String>) is -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        });

        // Converter for binary resources (альтернатива readAllBytes в Java 8)
        converters.put(byte[].class, (IResourceConverter<byte[]>) is -> {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        });

        // Converter for audio resources (OGG). The stream is not closed as it is needed for further processing.
        converters.put(AudioInputStream.class, (IResourceConverter<AudioInputStream>) is -> {
            VorbisAudioFileReader reader = new VorbisAudioFileReader();
            return reader.getAudioInputStream(is);
        });

        logLoadedTypes();
    }


    /**
     * Logs the registered converters as a sorted list of type names.
     */
    private static void logLoadedTypes() {
        List<String> typeNames = converters.keySet().stream().map(Class::getSimpleName).sorted().collect(Collectors.toList());
        logger.debug("Registered converters for resource types: {}", typeNames);
    }

    /**
     * Loads a resource and returns it in the requested type.
     *
     * @param resourcePath the path to the resource relative to the base directory
     * @param type         the expected class type (e.g., String.class, byte[].class, AudioInputStream.class)
     * @param <T>          the type of the loaded resource
     * @return the resource as an object of the requested type
     * @throws RuntimeException if the resource is not found or unsupported
     */
    @Override
    public <T> T loadResource(String resourcePath, Class<T> type) {
        logger.debug("Requesting resource load: {} (Type: {})", resourcePath, type.getSimpleName());
        @SuppressWarnings("unchecked")
        IResourceConverter<T> converter = (IResourceConverter<T>) converters.get(type);
        if (converter == null) {
            logger.error("Attempt to load unsupported resource type: {}", type.getName());
            throw new IllegalArgumentException("Resource type not supported: " + type.getName());
        }

        try {
            T resource;
            if (AudioInputStream.class.equals(type)) {
                // For audio resources, do not use try-with-resources to avoid closing the stream
                InputStream is = loadResourceAsStream(resourcePath);
                resource = converter.convert(is);
            } else {
                // For text and binary resources, use try-with-resources for safe stream closure
                try (InputStream is = loadResourceAsStream(resourcePath)) {
                    resource = converter.convert(is);
                }
            }

            logger.info("Successfully loaded resource: {} (Type: {})", resourcePath, type.getSimpleName());
            return resource;
        } catch (Exception e) {
            logger.error("Failed to load resource: {} (Type: {})", resourcePath, type.getSimpleName(), e);
            throw new RuntimeException("Error loading resource: " + resourcePath, e);
        }
    }

    /**
     * Loads a resource as an {@link InputStream}.
     *
     * @param resourcePath the path to the resource
     * @return the resource data stream
     * @throws IllegalStateException if the resource is not found
     */
    private InputStream loadResourceAsStream(String resourcePath) {
        // Если путь начинается с "file:", загружаем ресурс напрямую с диска
        if (resourcePath.startsWith("file:")) {
            try {
                URL url = new URL(resourcePath);
                return url.openStream();
            } catch (IOException e) {
                logger.error("Failed to load resource from disk: {}", resourcePath, e);
                throw new IllegalStateException("Error loading resource: " + resourcePath, e);
            }
        }
        // Если ресурс не с диска, используем базовый путь
        String fullPath = ASSET_BASE_PATH + resourcePath;
        InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(fullPath);
        if (is == null) {
            // Ресурс не найден в classpath, проверяем наличие файла на диске по fullPath
            File file = new File(fullPath);
            if (file.exists() && file.isFile()) {
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    logger.error("File not found on disk: {}", fullPath, e);
                    throw new IllegalStateException("Resource not found on disk: " + fullPath, e);
                }
            }
        }
        if (is == null) {
            logger.error("Resource not found: {}", fullPath);
            throw new IllegalStateException("Resource not found: " + fullPath);
        }
        return is;
    }

}
