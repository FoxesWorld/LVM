package org.foxesworld.lvm.view;

import javafx.concurrent.Worker;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.event.AnimationCallback;
import org.foxesworld.lvm.html.HtmlContentBuilder;
import org.foxesworld.lvm.sound.SoundPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationWebView extends Region {
    private static final Logger logger = LoggerFactory.getLogger(AnimationWebView.class);
    private static final String DEFAULT_HTML_TEMPLATE_PATH = "html/content.html";

    private final SoundPlayer soundPlayer;
    private WebView webView; // убрали final, чтобы можно было задать внешний экземпляр
    private LottieAnimationConfig config;
    private final HtmlContentBuilder htmlContentBuilder;

    // Конструктор, который принимает внешний WebView (если он передан, используем его)
    public AnimationWebView(LottieAnimationConfig config, WebView externalWebView) {
        if (config == null) throw new IllegalArgumentException("LottieAnimationConfig must not be null");
        this.config = config;
        this.soundPlayer = new SoundPlayer();
        if (externalWebView != null) {
            this.webView = externalWebView;
        } else {
            this.webView = new WebView();
        }
        this.htmlContentBuilder = new HtmlContentBuilder(DEFAULT_HTML_TEMPLATE_PATH);
        getChildren().add(this.webView);
        this.webView.prefWidthProperty().bind(widthProperty());
        this.webView.prefHeightProperty().bind(heightProperty());
        loadAnimation(this.config.getAnimationJsonResourcePath());
    }

    // Сохраняем прежний конструктор для обратной совместимости
    public AnimationWebView(LottieAnimationConfig config) {
        this(config, null);
    }

    protected void loadAnimation(String animationUri) {
        config.setAnimationJsonResourcePath(animationUri);
        try {
            String htmlContent = htmlContentBuilder.buildHtmlContent(config);
            webView.getEngine().loadContent(htmlContent);
        } catch (Exception e) {
            logger.error("Failed to load animation", e);
            throw new RuntimeException("Failed to load animation", e);
        }
    }

    protected void updateConfig(LottieAnimationConfig newConfig) {
        if (newConfig == null) throw new IllegalArgumentException("New configuration must not be null");
        this.config = newConfig;
        loadAnimation(this.config.getAnimationJsonResourcePath());
    }

    protected void setAnimationCallback(AnimationCallback callback) {
        if (callback == null) throw new IllegalArgumentException("AnimationCallback must not be null");
        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("animationCallback", callback);
            }
        });
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    protected void playAnimation() {
        try {
            webView.getEngine().executeScript(
                    "if (typeof lottieAnimation !== 'undefined') { lottieAnimation.play(); }"
            );
            logger.debug("playAnimation executed");
        } catch (Exception e) {
            logger.error("Failed to play animation", e);
        }
    }

    protected void pauseAnimation() {
        try {
            webView.getEngine().executeScript(
                    "if (typeof lottieAnimation !== 'undefined') { lottieAnimation.pause(); }"
            );
            logger.debug("pauseAnimation executed");
        } catch (Exception e) {
            logger.error("Failed to pause animation", e);
        }
    }

    protected void stopAnimation() {
        try {
            webView.getEngine().executeScript(
                    "if (typeof lottieAnimation !== 'undefined') { lottieAnimation.stop(); }"
            );
            logger.debug("stopAnimation executed");
        } catch (Exception e) {
            logger.error("Failed to stop animation", e);
        }
    }

    protected void setAnimationSpeed(float speed) {
        try {
            webView.getEngine().executeScript(
                    "if (typeof lottieAnimation !== 'undefined') { lottieAnimation.setSpeed(" + speed + "); }"
            );
            logger.debug("setAnimationSpeed executed with speed: {}", speed);
        } catch (Exception e) {
            logger.error("Failed to set animation speed", e);
        }
    }

    public void setAnimationProgress(float progress) {
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;
        try {
            webView.getEngine().executeScript(
                    "if (typeof lottieAnimation !== 'undefined' && lottieAnimation.totalFrames) {" +
                            "    var frame = Math.floor(lottieAnimation.totalFrames * " + progress + ");" +
                            "    lottieAnimation.goToAndStop(frame, true);" +
                            "}"
            );
            logger.debug("setAnimationProgress executed with progress: {}", progress);
        } catch (Exception e) {
            logger.error("Failed to set animation progress", e);
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public LottieAnimationConfig getConfig() {
        return config;
    }

    public HtmlContentBuilder getHtmlContentBuilder() {
        return htmlContentBuilder;
    }
}
