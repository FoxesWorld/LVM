package org.foxesworld.lvm;

import org.foxesworld.lvm.event.AnimationCallback;

public interface AnimationController {
    void play();
    void pause();
    void stop();
    void setSpeed(float speed);
    void setAnimationCallback(AnimationCallback callback);
}
