package org.example.lottie;

import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import org.example.lottie.config.LottieAnimationConfig;
import org.example.lottie.loader.DefaultResourceLoader;
import org.example.lottie.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Компонент для отображения анимаций Lottie в JavaFX WebView.
 * Класс использует принцип единственной ответственности, делегируя формирование HTML контента и загрузку ресурсов соответствующим классам.
 */
public class AnimationWebView extends Region {

    private static final Logger logger = LoggerFactory.getLogger(AnimationWebView.class);

    private final WebView webView;
    private LottieAnimationConfig config;
    private final ResourceLoader resourceLoader;
    private final HtmlContentBuilder htmlContentBuilder;

    /**
     * Конструктор, использующий конфигурацию и стандартный загрузчик ресурсов.
     *
     * @param config Конфигурация анимации Lottie.
     */
    public AnimationWebView(LottieAnimationConfig config) {
        this(config, new DefaultResourceLoader());
    }

    /**
     * Конструктор с возможностью указания собственного загрузчика ресурсов.
     *
     * @param config         Конфигурация анимации Lottie.
     * @param resourceLoader Пользовательская реализация загрузчика ресурсов.
     */
    public AnimationWebView(LottieAnimationConfig config, ResourceLoader resourceLoader) {
        this.config = config;
        this.resourceLoader = resourceLoader;
        this.webView = new WebView();
        this.htmlContentBuilder = new HtmlContentBuilder(resourceLoader, "html/content.html");
        initialize();
    }


    private void initialize() {
        getChildren().add(webView);
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());
        loadAnimation();
    }

    /**
     * Загружает анимацию в WebView, формируя HTML контент на основе конфигурации.
     */
    public void loadAnimation() {
        try {
            String htmlContent = htmlContentBuilder.buildHtmlContent(config);
            webView.getEngine().loadContent(htmlContent);
        } catch (Exception e) {
            logger.error("Не удалось загрузить анимацию", e);
            throw new RuntimeException("Не удалось загрузить анимацию", e);
        }
    }

    /**
     * Обновляет конфигурацию анимации и перезагружает контент.
     *
     * @param newConfig Новая конфигурация анимации Lottie.
     */
    public void updateConfig(LottieAnimationConfig newConfig) {
        this.config = newConfig;
        loadAnimation();
    }
}
