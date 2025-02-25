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
 * This class loads an HTML template and processes it using a {@link TemplateProcessor} by replacing
 * placeholders with values extracted from the provided {@link LottieAnimationConfig} and resources loaded
 * via the {@link IResourceLoader}.
 * </p>
 */
public class HtmlContentBuilder implements IResourceLoader {

    private final String contentFile;
    private final TemplateProcessor templateProcessor;

    /**
     * Constructs a new HtmlContentBuilder with the specified resource loader and HTML template file path.
     *
     * @param contentFile    the path to the HTML template file; must not be {@code null}
     * @throws IllegalArgumentException if {@code resourceLoader} or {@code contentFile} is {@code null}
     */
    public HtmlContentBuilder(String contentFile) {
        this.contentFile = Objects.requireNonNull(contentFile, "Content file path must not be null");
        this.templateProcessor = new TemplateProcessor();
    }

    /**
     * Builds and returns the HTML content by processing the HTML template with values derived from the provided Lottie animation configuration.
     *
     * @param config the Lottie animation configuration; must not be {@code null}
     * @return the processed HTML content as a {@link String}
     * @throws IllegalArgumentException if {@code config} is {@code null}
     * @throws RuntimeException         if an error occurs during resource loading or template processing
     */
    public String buildHtmlContent(LottieAnimationConfig config) {
        Objects.requireNonNull(config, "LottieAnimationConfig must not be null");
        try {
            String template = loadResource(contentFile, String.class);
            Map<String, String> values = new HashMap<>();
            values.put("containerId", config.getContainerId());
            values.put("bodymovinJs", loadResource(config.getBodymovinJsResourcePath(), String.class));
            values.put("animationJson", loadResource(config.getAnimationJsonResourcePath(), String.class));
            values.put("renderer", config.getRenderer());
            values.put("loop", String.valueOf(config.isLoop()));
            values.put("autoplay", String.valueOf(config.isAutoplay()));

            return templateProcessor.process(template, values);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build HTML content", e);
        }
    }
    @Override
    public <T> T loadResource(String resourcePath, Class<T> type) {
        return new ResourceLoader().loadResource(resourcePath, type);
    }
}
