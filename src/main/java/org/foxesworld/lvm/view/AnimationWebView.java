package org.foxesworld.lvm.view;

import javafx.concurrent.Worker;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.foxesworld.lvm.AnimationCallback;
import org.foxesworld.lvm.HtmlContentBuilder;
import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.resourceLoader.DefaultResourceLoader;
import org.foxesworld.lvm.resourceLoader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component for displaying Lottie animations within a JavaFX WebView.
 * <p>
 * This class adheres to the Single Responsibility Principle by delegating HTML content creation and
 * resource loading to dedicated classes. It encapsulates all behavior related to animation rendering,
 * ensuring that each method and constructor validates its input and maintains proper state.
 * </p>
 */
public class AnimationWebView extends Region {

    private static final Logger logger = LoggerFactory.getLogger(AnimationWebView.class);
    private static final String DEFAULT_HTML_TEMPLATE_PATH = "html/content.html";

    private final WebView webView;
    private LottieAnimationConfig config;
    private final ResourceLoader resourceLoader;
    private final HtmlContentBuilder htmlContentBuilder;

    /**
     * Constructs an AnimationWebView with the specified Lottie animation configuration using
     * the default resource loader.
     *
     * @param config the Lottie animation configuration; must not be {@code null}
     * @throws IllegalArgumentException if {@code config} is {@code null}
     */
    public AnimationWebView(LottieAnimationConfig config) {
        this(config, new DefaultResourceLoader());
    }

    /**
     * Constructs an AnimationWebView with the specified Lottie animation configuration and a custom resource loader.
     *
     * @param config         the Lottie animation configuration; must not be {@code null}
     * @param resourceLoader a custom resource loader; must not be {@code null}
     * @throws IllegalArgumentException if either {@code config} or {@code resourceLoader} is {@code null}
     */
    public AnimationWebView(LottieAnimationConfig config, ResourceLoader resourceLoader) {
        if (config == null) {
            throw new IllegalArgumentException("LottieAnimationConfig must not be null");
        }
        if (resourceLoader == null) {
            throw new IllegalArgumentException("ResourceLoader must not be null");
        }
        this.config = config;
        this.resourceLoader = resourceLoader;
        this.webView = new WebView();
        this.htmlContentBuilder = new HtmlContentBuilder(resourceLoader, DEFAULT_HTML_TEMPLATE_PATH);
        initialize();
    }

    /**
     * Initializes the component by adding the WebView as a child, binding its size to the container,
     * and loading the initial animation.
     */
    private void initialize() {
        getChildren().add(webView);
        bindWebViewSize();
        loadAnimation();
    }

    /**
     * Binds the WebView's preferred width and height properties to the AnimationWebView's width and height.
     */
    private void bindWebViewSize() {
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());
    }

    /**
     * Loads the Lottie animation into the WebView by generating HTML content based on the current configuration.
     * <p>
     * This method uses the {@link HtmlContentBuilder} to create the HTML content, and then sets it in the WebView.
     * If any error occurs during content generation or loading, an error is logged and a {@link RuntimeException} is thrown.
     * </p>
     *
     * @throws RuntimeException if the animation cannot be loaded
     */
    public void loadAnimation() {
        try {
            String htmlContent = htmlContentBuilder.buildHtmlContent(config);
            webView.getEngine().loadContent(htmlContent);
        } catch (Exception e) {
            logger.error("Failed to load animation", e);
            throw new RuntimeException("Failed to load animation", e);
        }
    }

    /**
     * Updates the Lottie animation configuration and reloads the animation.
     *
     * @param newConfig the new Lottie animation configuration; must not be {@code null}
     * @throws IllegalArgumentException if {@code newConfig} is {@code null}
     */
    public void updateConfig(LottieAnimationConfig newConfig) {
        if (newConfig == null) {
            throw new IllegalArgumentException("New configuration must not be null");
        }
        this.config = newConfig;
        loadAnimation();
    }

    public void setAnimationCallback(AnimationCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("AnimationCallback must not be null");
        }
        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("animationCallback", callback);
            }
        });
    }
}
