package org.foxesworld.lvm;

import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.event.AnimationCallback;
import org.foxesworld.lvm.view.AnimationWebView;

public class LVMCore extends AnimationWebView {

    public LVMCore(LottieAnimationConfig config) {
        super(config);
    }

    public void setAnimationCallback(AnimationCallback callback) {
        super.setAnimationCallback(callback);
    }

    public void play() {
        super.playAnimation();
    }

    public void pause() {
        super.pauseAnimation();
    }

    public void stop() {
        super.stopAnimation();
    }

    public void setSpeed(float speed) {
        super.setAnimationSpeed(speed);
    }
}
