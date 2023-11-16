package me.oneqxz.riseloader.fxml.components.impl;

import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.LaunchDebugController;
import me.oneqxz.riseloader.fxml.scenes.MainScene;

import java.io.IOException;

public class LaunchDebug extends Component {

    private Process process;
    private String command;
    public LaunchDebug(Process process, String command) {
        this.process = process;
        this.command = command;
    }

    @Override
    public Stage show(Object... args) throws IOException {
        Stage stage = new Stage();
        FX.showScene("RiseLoader debug", "started.fxml", stage, new LaunchDebugController(process, command));
        FX.setMinimizeAndClose(stage, "hideButton", "closeButton", () ->
        {
            process.destroy();
            MainScene.showSelf();
            stage.close();
        }, false, false);
        FX.setDraggable(stage.getScene(), "riseLogo");
        return stage;
    }

    public Process getProcess() {
        return process;
    }
}
