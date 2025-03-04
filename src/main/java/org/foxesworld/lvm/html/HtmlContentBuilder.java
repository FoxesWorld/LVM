package org.foxesworld.lvm.html;

import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.resourceLoader.IResourceLoader;
import org.foxesworld.lvm.resourceLoader.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builds HTML content based on a Lottie animation configuration.
 * <p>
 * This class loads an HTML template only once during initialization and processes it using a {@link TemplateProcessor}
 * by replacing placeholders with values extracted from the provided {@link LottieAnimationConfig}. To reduce the number
 * of resource loads, static resources (HTML template, JS, JSON) are cached.
 * </p>
 */
public class HtmlContentBuilder implements IResourceLoader {

    private final String contentFile;
    private final TemplateProcessor templateProcessor;

    // Кэш для статических ресурсов, загружаемых по пути
    private final Map<String, String> resourceCache = new HashMap<>();

    // Загруженный шаблон HTML, который используется для всех построений
    private final String cachedTemplate;

    /**
     * Constructs a new HtmlContentBuilder with the specified HTML template file path.
     *
     * @param contentFile the path to the HTML template file; must not be {@code null}
     * @throws IllegalArgumentException if {@code contentFile} is {@code null}
     */
    public HtmlContentBuilder(String contentFile) {
        this.contentFile = Objects.requireNonNull(contentFile, "Content file path must not be null");
        this.templateProcessor = new TemplateProcessor();
        // Загружаем шаблон HTML один раз и сохраняем в кэше
        this.cachedTemplate = loadResource(contentFile, String.class);
    }

    /**
     * Builds and returns the HTML content by processing the cached HTML template with values derived from the provided
     * Lottie animation configuration.
     *
     * @param config the Lottie animation configuration; must not be {@code null}
     * @return the processed HTML content as a {@link String}
     * @throws IllegalArgumentException if {@code config} is {@code null}
     * @throws RuntimeException         if an error occurs during resource loading or template processing
     */
    public String buildHtmlContent(LottieAnimationConfig config) {
        Objects.requireNonNull(config, "LottieAnimationConfig must not be null");
        try {
            Map<String, Object> values = new HashMap<>();
            values.put("containerId", config.getContainerId());
            values.put("bodymovinJs", getCachedResource(config.getBodymovinJsResourcePath()));
            values.put("animationJson", getCachedResource(config.getAnimationJsonResourcePath()));
            values.put("renderer", config.getRenderer());
            values.put("loop", String.valueOf(config.isLoop()));
            values.put("autoplay", String.valueOf(config.isAutoplay()));

            return templateProcessor.process(cachedTemplate, values);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build HTML content", e);
        }
    }

    /**
     * Возвращает ресурс из кэша или загружает его, если он отсутствует в кэше.
     *
     * @param resourcePath путь к ресурсу
     * @return содержимое ресурса в виде {@link String}
     */
    private String getCachedResource(String resourcePath) {
        return resourceCache.computeIfAbsent(resourcePath, path -> loadResource(path, String.class));
    }

    @Override
    public <T> T loadResource(String resourcePath, Class<T> type) {
        return new ResourceLoader().loadResource(resourcePath, type);
    }

    public String getContentFile() {
        return contentFile;
    }

    public TemplateProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    public Map<String, String> getResourceCache() {
        return resourceCache;
    }

    public String getCachedTemplate() {
        return cachedTemplate;
    }
}
