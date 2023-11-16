package me.oneqxz.riseloader.fxml.components.impl;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;

import java.io.IOException;

public class Loading extends Component {

    private Stage curr;

    @Override
    public Stage show(Object... args) throws IOException {
        Stage stage = new Stage();
        FX.showScene("RiseLoader loading", "loading.fxml", stage, new Controller() {
            @Override
            protected void init() {
                MainScene.setBackground(((Rectangle)root.lookup("#background")));
            }
        });
        FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", (boolean) args[0]);
        FX.setDraggable(stage.getScene(), "riseLogo");
        return curr = stage;
    }

    public void setStageTextLater(String text)
    {
        if(curr == null)
            return;

        Platform.runLater(() -> ((Label) curr.getScene().lookup("#stage")).setText(text));
    }

    public void setStageText(String text)
    {
        if(curr == null)
            return;

        ((Label) curr.getScene().lookup("#stage")).setText(text);
    }
}
