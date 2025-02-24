package org.foxesworld.lvm.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.foxesworld.lvm.config.LottieAnimationConfig;

import javax.swing.JPanel;
import java.awt.BorderLayout;

@Deprecated
public class LottiePanelView extends JPanel {
    private final JFXPanel jfxPanel;
    private final LottieAnimationConfig config;

    public LottiePanelView(LottieAnimationConfig config) {
        this.config = config;
        this.jfxPanel = new JFXPanel();
        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            AnimationWebView animationView = new AnimationWebView(config);
            Scene scene = new Scene(animationView, getWidth(), getHeight());
            jfxPanel.setScene(scene);
        });
    }
}
