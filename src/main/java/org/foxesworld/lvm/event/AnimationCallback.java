package org.foxesworld.lvm.event;

/**
 * Callback interface for handling Lottie animation events.
 */
@SuppressWarnings("unused")
public interface AnimationCallback {

    /**
     * Called when a Lottie event occurs.
     *
     * @param eventName the name of the event
     * @param eventData the event data
     */
    void onEvent(String eventName, Object eventData);

    /**
     * Called when a hover event (mouseenter or mouseleave) occurs on the animation.
     *
     * @param hoverEventData the hover event data (e.g., {"type": "mouseenter"} or {"type": "mouseleave"})
     */
    void onHover(Object hoverEventData);

    /**
     * Called when a click event occurs on the animation.
     *
     * @param clickEventData the click event data, including coordinates and timestamp
     */
    void onClick(Object clickEventData);
}
