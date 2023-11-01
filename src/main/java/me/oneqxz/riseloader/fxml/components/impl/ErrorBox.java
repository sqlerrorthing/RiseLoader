package me.oneqxz.riseloader.fxml.components.impl;

import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.ErrorBoxController;

import java.io.IOException;

public class ErrorBox extends Component {
    @Override
    public Stage show(Object... args) throws IOException {
        Stage stage = new Stage();
        FX.showScene("RiseLoader error", "error.fxml", stage, new ErrorBoxController((Throwable) args[0]));
        FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", true);
        FX.setDraggable(stage.getScene(), "riseLogo");
        return stage;
    }
}
