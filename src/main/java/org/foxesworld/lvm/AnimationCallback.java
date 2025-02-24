package org.foxesworld.lvm;

/**
 * Callback interface for handling Lottie animation events.
 */
public interface AnimationCallback {
    /**
     * Called when a Lottie event occurs.
     *
     * @param eventName the name of the event
     * @param eventData the event data
     */
    void onEvent(String eventName, Object eventData);
}
