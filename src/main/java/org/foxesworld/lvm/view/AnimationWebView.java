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
    private final WebView webView;
    private LottieAnimationConfig config;
    private final HtmlContentBuilder htmlContentBuilder;

    public AnimationWebView(LottieAnimationConfig config) {
        if (config == null) throw new IllegalArgumentException("LottieAnimationConfig must not be null");
        this.config = config;
        this.soundPlayer = new SoundPlayer();
        this.webView = new WebView();
        this.htmlContentBuilder = new HtmlContentBuilder(DEFAULT_HTML_TEMPLATE_PATH);
        getChildren().add(webView);
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());
        loadAnimation();
    }

    protected void loadAnimation() {
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
        loadAnimation();
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
}
