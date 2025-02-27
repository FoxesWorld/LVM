package org.foxesworld.lvm;

import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.event.AnimationCallback;
import org.foxesworld.lvm.view.AnimationWebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LVMCore extends AnimationWebView implements AnimationController {

    private static final Logger logger = LoggerFactory.getLogger(LVMCore.class);
    protected String animPath;

    public LVMCore(LottieAnimationConfig config) {
        super(config);
    }

    @Override
    public void setAnimationCallback(AnimationCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("AnimationCallback must not be null");
        }
        logger.debug("Setting animation callback.");
        super.setAnimationCallback(callback);
    }

    public void loadAnimation(String animationUri) {
        this.animPath = animationUri;
        getConfig().setAnimationJsonResourcePath(animationUri);
        try {
            String htmlContent = getHtmlContentBuilder().buildHtmlContent(getConfig());
            getWebView().getEngine().loadContent(htmlContent);
        } catch (Exception e) {
            logger.error("Failed to load animation", e);
            throw new RuntimeException("Failed to load animation", e);
        }
    }
    @Override
    public void play() {
        logger.debug("Playing animation.");
        super.playAnimation();
    }

    @Override
    public void pause() {
        logger.debug("Pausing animation.");
        super.pauseAnimation();
    }

    @Override
    public void stop() {
        logger.debug("Stopping animation.");
        super.stopAnimation();
    }

    @Override
    public void setSpeed(float speed) {
        if (speed <= 0.0f) {
            throw new IllegalArgumentException("Speed must be greater than 0");
        }
        logger.debug("Setting animation speed to {}.", speed);
        super.setAnimationSpeed(speed);
    }
}
