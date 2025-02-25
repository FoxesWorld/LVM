package org.foxesworld.lvm.config;
public class LottieAnimationConfig {
    private final String bodymovinJsResourcePath;
    private String animationJsonResourcePath;
    private String containerId;
    private String renderer;
    private boolean loop;
    private boolean autoplay;

    public LottieAnimationConfig() {
        this.bodymovinJsResourcePath = "/js/lottie.min.js";
        this.animationJsonResourcePath = "/test.json";
        this.containerId = "lottie";
        this.renderer = "svg";
        this.loop = false;
        this.autoplay = true;
    }
    public String getBodymovinJsResourcePath() {
        return bodymovinJsResourcePath;
    }

    public String getAnimationJsonResourcePath() {
        return animationJsonResourcePath;
    }

    public void setAnimationJsonResourcePath(String animationJsonResourcePath) {
        this.animationJsonResourcePath = animationJsonResourcePath;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }
}
