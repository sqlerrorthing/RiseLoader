package me.oneqxz.riseloader.fxml.components.impl;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.controllers.Controller;

import java.io.IOException;

public class Loading extends Component {

    @Override
    public Stage show(Object... args) throws IOException {
        Stage stage = new Stage();
        FX.showScene("RiseLoader loading", "loading.fxml", stage, new Controller() {
            @Override
            protected void init() {
                Image bg = new Image("/background.jpg");
                ((Rectangle)root.lookup("#background")).setFill(new ImagePattern(bg));
            }
        });
        FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", (boolean) args[0]);
        FX.setDraggable(stage.getScene(), "riseLogo");
        return stage;
    }

}
