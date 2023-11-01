package me.oneqxz.riseloader.fxml.components.impl;

import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.FX;

import java.io.IOException;

public class Loading extends Component {

    @Override
    public Stage show(Object... args) throws IOException {
        Stage stage = new Stage();
        FX.showScene("RiseLoader loading", "loading.fxml", stage);
        FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", (boolean) args[0]);
        FX.setDraggable(stage.getScene(), "riseLogo");
        return stage;
    }

}
