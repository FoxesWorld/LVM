package org.foxesworld;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.foxesworld.lvm.AnimationCallback;
import org.foxesworld.lvm.view.AnimationWebView;
import org.foxesworld.lvm.config.LottieAnimationConfig;

public class LottieDemoApp extends Application implements AnimationCallback {

    @Override
    public void start(Stage primaryStage) {
        LottieAnimationConfig config = new LottieAnimationConfig();
        config.setAnimationJsonResourcePath("/anim/ai.json");
        config.setContainerId("lottieContainer");
        config.setRenderer("svg");
        config.setLoop(true);
        config.setAutoplay(true);

        AnimationWebView animationView = new AnimationWebView(config);
        animationView.setAnimationCallback(this);

        Scene scene = new Scene(animationView, 300, 300);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.setTitle("FoxesWorld Exp Ai");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void onEvent(String eventName, Object eventData) {
        System.out.println(eventName + eventData);
    }
}
