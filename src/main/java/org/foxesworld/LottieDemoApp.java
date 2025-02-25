package org.foxesworld;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.foxesworld.lvm.LVMCore;
import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.event.AnimationCallback;
import org.foxesworld.lvm.event.LottieEventData;
import org.foxesworld.lvm.event.LottieHoverType;

import java.util.Objects;

public class LottieDemoApp extends Application implements AnimationCallback {

    private LVMCore lvmCore;

    @Override
    public void start(Stage primaryStage) {
        LottieAnimationConfig config = new LottieAnimationConfig();
        config.setAnimationJsonResourcePath("/anim/ok.json");
        config.setContainerId("lottieContainer");
        config.setRenderer("svg");
        config.setLoop(true);
        config.setAutoplay(true);

        lvmCore = new LVMCore(config);
        lvmCore.setAnimationCallback(this);

        Scene scene = new Scene(lvmCore, 300, 300);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        primaryStage.setTitle("FoxesWorld Exp Ai");
        primaryStage.setScene(scene);
        primaryStage.show();

        lvmCore.play();
        lvmCore.setSpeed(1.0f);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onEvent(String eventName, Object eventData) {
        Gson gson = new Gson();
        LottieEventData data = gson.fromJson(eventData.toString(), LottieEventData.class);
        switch (eventName) {
            case "DOMLoaded" -> lvmCore.getSoundPlayer().playSound("whoosh1.ogg", 1.0f);
            case "enterFrame" -> {
                // Process frame event if needed
            }
            case "loopComplete" -> lvmCore.getSoundPlayer().playSound("sound1.ogg", 1.0f);
        }
    }

    @Override
    public void onHover(Object hoverEventData) {
        LottieHoverType type = new Gson().fromJson(hoverEventData.toString(), LottieHoverType.class);
        switch (type.getType()) {
            case "mouseenter" -> lvmCore.setSpeed(2.0f);
            case "mouseleave" -> lvmCore.setSpeed(1.0f);
        }
    }

    @Override
    public void onClick(Object clickEventData) {
        System.out.println("Click");
    }
}
