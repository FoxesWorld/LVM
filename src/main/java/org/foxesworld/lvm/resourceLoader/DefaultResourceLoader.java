package org.foxesworld.lvm.resourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link ResourceLoader} interface.
 * <p>
 * Loads resources from the {@code assets} directory using the {@link ClassLoader}.
 * </p>
 */
public class DefaultResourceLoader implements ResourceLoader {

    private static final String ASSET_BASE_PATH = "assets/";

    /**
     * Loads a resource from the {@code assets} directory based on the given relative path.
     * <p>
     * The resource is read as a UTF-8 encoded string. If the resource cannot be found or an error occurs during
     * loading, a runtime exception is thrown.
     * </p>
     *
     * @param resourcePath the relative path to the resource within the {@code assets} directory; must not be {@code null}
     * @return the content of the resource as a {@link String}
     * @throws IllegalStateException if the resource cannot be found
     * @throws RuntimeException      if an error occurs while reading the resource
     */
    @Override
    public String loadResource(String resourcePath) {
        String fullPath = ASSET_BASE_PATH + resourcePath;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + fullPath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource: " + fullPath, e);
        }
    }
}
