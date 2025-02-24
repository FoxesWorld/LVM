package org.example.lottie.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Стандартная реализация интерфейса ResourceLoader.
 * Загружает ресурсы из папки assets, используя ClassLoader.
 */
public class DefaultResourceLoader implements ResourceLoader {

    private static final String ASSET_BASE_PATH = "assets/";

    @Override
    public String loadResource(String resourcePath) {
        String fullPath = ASSET_BASE_PATH + resourcePath;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (is == null) {
                throw new IllegalStateException("Ресурс не найден: " + fullPath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить ресурс: " + fullPath, e);
        }
    }
}
