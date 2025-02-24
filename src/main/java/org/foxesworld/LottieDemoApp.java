package org.foxesworld;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.foxesworld.lvm.AnimationCallback;
import org.foxesworld.lvm.LottieEventData;
import org.foxesworld.lvm.view.AnimationWebView;
import org.foxesworld.lvm.config.LottieAnimationConfig;

import java.util.Objects;

public class LottieDemoApp extends Application implements AnimationCallback {

    AnimationWebView animationView;
    @Override
    public void start(Stage primaryStage) {
        LottieAnimationConfig config = new LottieAnimationConfig();
        config.setAnimationJsonResourcePath("/anim/ai.json");
        config.setContainerId("lottieContainer");
        config.setRenderer("svg");
        config.setLoop(true);
        config.setAutoplay(true);

        animationView = new AnimationWebView(config);
        animationView.setAnimationCallback(this);

        Scene scene = new Scene(animationView, 300, 300);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        primaryStage.setTitle("FoxesWorld Exp Ai");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void onEvent(String eventName, Object eventData) {
        Gson gson = new Gson();
        LottieEventData data = gson.fromJson(eventData.toString(), LottieEventData.class);

        switch (eventName) {
            case "DOMLoaded" -> this.animationView.getSoundPlayer().playSound("whoosh1.ogg", 1.0f);
            case "enterFrame" -> {
                System.out.println(data.getType());
            }
            case "loopComplete" -> this.animationView.getSoundPlayer().playSound("ha.ogg", 1.0f);
        }
    }

    @Override
    public void onHover(Object hoverEventData) {
        System.out.println("Hover");
    }

    @Override
    public void onClick(Object clickEventData) {
        System.out.println("Click");
    }
}
