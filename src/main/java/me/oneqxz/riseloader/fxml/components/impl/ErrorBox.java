package me.oneqxz.riseloader.fxml.components.impl;

import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.ErrorBoxController;

import java.io.IOException;

public class ErrorBox extends Component {
    @Override
    public Stage show(Object... args) {
        try {
            Stage stage = new Stage();
            FX.showScene("RiseLoader error", "error.fxml", stage, new ErrorBoxController((Throwable) args[0]));
            FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", args.length == 1 || (boolean) args[1]);
            FX.setDraggable(stage.getScene(), "riseLogo");
            return stage;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
