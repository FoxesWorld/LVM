package org.example.lottie.loader;

/**
 * Интерфейс для загрузки ресурсов.
 * Позволяет легко подменять реализацию загрузки (например, для тестирования или загрузки из альтернативных источников).
 */
public interface ResourceLoader {

    /**
     * Загружает ресурс по указанному пути.
     *
     * @param resourcePath Путь к ресурсу относительно базовой директории.
     * @return Содержимое ресурса в виде строки.
     */
    String loadResource(String resourcePath);
}