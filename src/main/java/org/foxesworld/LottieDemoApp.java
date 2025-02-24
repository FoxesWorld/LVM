package org.foxesworld;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.foxesworld.lvm.AnimationWebView;
import org.foxesworld.lvm.config.LottieAnimationConfig;

public class LottieDemoApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LottieAnimationConfig config = new LottieAnimationConfig();
        config.setAnimationJsonResourcePath("/anim/ai.json");
        config.setContainerId("lottieContainer");
        config.setRenderer("svg");
        config.setLoop(true);
        config.setAutoplay(true);

        AnimationWebView animationView = new AnimationWebView(config);

        Scene scene = new Scene(animationView, 300, 300);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/fox.png")));
        primaryStage.setTitle("FoxesWorld Exp Ai");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
