package org.foxesworld.lvm;

import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.loader.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builds HTML content based on a Lottie animation configuration.
 * <p>
 * This class loads an HTML template and processes it using a {@link TemplateProcessor} by replacing
 * placeholders with values extracted from the provided {@link LottieAnimationConfig} and resources loaded
 * via the {@link ResourceLoader}.
 * </p>
 */
public class HtmlContentBuilder {

    private final ResourceLoader resourceLoader;
    private final String contentFile;
    private final TemplateProcessor templateProcessor;

    /**
     * Constructs a new HtmlContentBuilder with the specified resource loader and HTML template file path.
     *
     * @param resourceLoader the resource loader used to load the HTML template and additional resources; must not be {@code null}
     * @param contentFile    the path to the HTML template file; must not be {@code null}
     * @throws IllegalArgumentException if {@code resourceLoader} or {@code contentFile} is {@code null}
     */
    public HtmlContentBuilder(ResourceLoader resourceLoader, String contentFile) {
        this.resourceLoader = Objects.requireNonNull(resourceLoader, "ResourceLoader must not be null");
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
            String template = resourceLoader.loadResource(contentFile);
            Map<String, String> values = new HashMap<>();
            values.put("containerId", config.getContainerId());
            values.put("bodymovinJs", resourceLoader.loadResource(config.getBodymovinJsResourcePath()));
            values.put("animationJson", resourceLoader.loadResource(config.getAnimationJsonResourcePath()));
            values.put("renderer", config.getRenderer());
            values.put("loop", String.valueOf(config.isLoop()));
            values.put("autoplay", String.valueOf(config.isAutoplay()));

            return templateProcessor.process(template, values);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build HTML content", e);
        }
    }
}
