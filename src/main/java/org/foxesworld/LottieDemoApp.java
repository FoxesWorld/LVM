package org.foxesworld;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.foxesworld.lvm.LVMCore;
import org.foxesworld.lvm.config.LottieAnimationConfig;
import org.foxesworld.lvm.event.AnimationCallback;
import org.foxesworld.lvm.event.LottieEventData;
import org.foxesworld.lvm.event.LottieHoverType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class LottieDemoApp extends Application implements AnimationCallback {

    private static final Logger logger = LoggerFactory.getLogger(LottieDemoApp.class);
    private LVMCore lvmCore;

    @Override
    public void start(Stage primaryStage) {
        logger.info("Initializing LottieDemoApp...");

        try {
            // Настройка анимации
            LottieAnimationConfig config = new LottieAnimationConfig();
            config.setAnimationJsonResourcePath("/anim/ok.json");
            config.setContainerId("lottieContainer");
            config.setRenderer("svg");
            config.setLoop(true);
            config.setAutoplay(true);

            lvmCore = new LVMCore(config);
            lvmCore.setAnimationCallback(this);

            // Создание UI-компонентов
            HBox buttonBox = createControlButtons(primaryStage);
            Slider animationSlider = createAnimationSlider();

            VBox controlPanel = new VBox(10, new Label("Animation Controls"), buttonBox, animationSlider);
            controlPanel.setAlignment(Pos.CENTER);
            controlPanel.setPadding(new Insets(10));
            controlPanel.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-background-color: #f9f9f9;");

            // Корневой контейнер
            BorderPane root = new BorderPane();
            root.setCenter(lvmCore);
            root.setBottom(controlPanel);

            Scene scene = new Scene(root, 400, 500);
            applyStyles(scene);

            // Настройки окна
            primaryStage.setTitle("FoxesWorld Exp Ai");
            primaryStage.getIcons().add(loadAppIcon());
            primaryStage.setScene(scene);
            primaryStage.show();

            lvmCore.play();
            lvmCore.setSpeed(1.0f);

            logger.info("LottieDemoApp started successfully.");
        } catch (Exception e) {
            logger.error("Application failed to start", e);
        }
    }

    /**
     * Создаёт блок кнопок управления анимацией.
     */
    private HBox createControlButtons(Stage primaryStage) {
        Button btnPlay = createIconButton(FontAwesome.Glyph.PLAY, "Play", e -> lvmCore.play());
        Button btnPause = createIconButton(FontAwesome.Glyph.PAUSE, "Pause", e -> lvmCore.pause());
        Button btnLoad = createIconButton(FontAwesome.Glyph.FOLDER_OPEN, "Load", e -> loadNewAnimation(primaryStage));

        HBox buttonBox = new HBox(10, btnPlay, btnPause, btnLoad);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    /**
     * Создаёт кнопку с иконкой из FontAwesomeFX.
     */
    private Button createIconButton(FontAwesome.Glyph icon, String tooltip, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button("", new Glyph("FontAwesome", icon));
        button.setOnAction(action);
        button.setPrefSize(50, 50);
        button.setStyle("-fx-background-radius: 5; -fx-background-color: #eee;");
        button.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        return button;
    }


    private Slider createAnimationSlider() {
        Slider slider = new Slider(0, 100, 0);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
        slider.setPrefWidth(350);
        slider.getStyleClass().add("video-slider");

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            float progress = newVal.floatValue() / 100;
            lvmCore.setAnimationProgress(progress);
        });

        return slider;
    }

    /**
     * Загружает новую анимацию из файла.
     */
    private void loadNewAnimation(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите JSON-файл анимации");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            logger.info("Loading new animation: {}", file.toURI());
            lvmCore.loadAnimation(file.toURI().toString());
        }
    }

    /**
     * Загружает иконку приложения.
     */
    private Image loadAppIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png")));
    }

    /**
     * Применяет CSS-стили к сцене.
     */
    private void applyStyles(Scene scene) {
        String cssPath = "/assets/css/video-slider.css";
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssPath)).toExternalForm());
        } catch (NullPointerException e) {
            logger.error("Failed to load CSS: {}", cssPath, e);
        }
    }

    /**
     * Обрабатывает события анимации.
     */
    @Override
    public void onEvent(String eventName, Object eventData) {
        LottieEventData data = new Gson().fromJson(eventData.toString(), LottieEventData.class);

        switch (eventName) {
            case "DOMLoaded" -> lvmCore.getSoundPlayer().playSound("whoosh1.ogg", 1.0f);
            case "loopComplete" -> lvmCore.getSoundPlayer().playSound("sound1.ogg", 1.0f);
            default -> logger.debug("Unhandled animation event: {}", eventName);
        }
    }

    /**
     * Обрабатывает события наведения на анимацию.
     */
    @Override
    public void onHover(Object hoverEventData) {
        LottieHoverType type = new Gson().fromJson(hoverEventData.toString(), LottieHoverType.class);

        switch (type.getType()) {
            case "mouseenter" -> lvmCore.setSpeed(2.0f);
            case "mouseleave" -> lvmCore.setSpeed(1.0f);
            default -> logger.debug("Unhandled hover event: {}", type.getType());
        }
    }

    /**
     * Обрабатывает клик по анимации.
     */
    @Override
    public void onClick(Object clickEventData) {
        logger.info("Animation clicked.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
