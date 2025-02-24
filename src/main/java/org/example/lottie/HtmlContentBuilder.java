package org.example.lottie;

import org.example.lottie.config.LottieAnimationConfig;
import org.example.lottie.loader.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public class HtmlContentBuilder {

    private final ResourceLoader resourceLoader;
    private final  String contentFile;
    private final TemplateProcessor templateProcessor;

    /**
     * Конструктор с внедрением загрузчика ресурсов.
     *
     * @param resourceLoader Объект для загрузки ресурсов.
     */
    public HtmlContentBuilder(ResourceLoader resourceLoader, String contentFile) {
        this.contentFile = contentFile;
        this.resourceLoader = resourceLoader;
        this.templateProcessor = new TemplateProcessor();
    }


    public String buildHtmlContent(LottieAnimationConfig config) {
        String template = resourceLoader.loadResource(contentFile);
        Map<String, String> values = new HashMap<>();
        values.put("containerId", config.getContainerId());
        values.put("bodymovinJs", resourceLoader.loadResource(config.getBodymovinJsResourcePath()));
        values.put("animationJson", resourceLoader.loadResource(config.getAnimationJsonResourcePath()));
        values.put("renderer", config.getRenderer());
        values.put("loop", String.valueOf(config.isLoop()));
        values.put("autoplay", String.valueOf(config.isAutoplay()));

        return templateProcessor.process(template, values);
    }
}
