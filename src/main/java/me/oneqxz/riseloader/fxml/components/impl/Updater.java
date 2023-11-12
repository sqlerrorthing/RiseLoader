package me.oneqxz.riseloader.fxml.components.impl;

import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.ErrorBoxController;
import me.oneqxz.riseloader.fxml.components.impl.controllers.UpdatingController;
import me.oneqxz.riseloader.fxml.rpc.DiscordRichPresence;

import java.io.IOException;

public class Updater extends Component {

    @Override
    public Stage show(Object... args) throws IOException {
        try {
            DiscordRichPresence.getInstance().updateState("In Update...");
            Stage stage = new Stage();
            FX.showScene("RiseLoader updating...", "updating.fxml", stage, new UpdatingController());
            FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", true);
            FX.setDraggable(stage.getScene(), "riseLogo");
            stage.setIconified(false);
            return stage;
        }
        catch (Exception e)
        {
            new ErrorBox().show(e);
            return null;
        }
    }
}
