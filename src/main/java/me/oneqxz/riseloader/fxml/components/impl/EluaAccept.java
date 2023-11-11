package me.oneqxz.riseloader.fxml.components.impl;

import javafx.scene.Scene;
import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.EluaAcceptController;
import me.oneqxz.riseloader.fxml.controllers.Controller;

public class EluaAccept extends Component {

    private Runnable onAccept;

    public EluaAccept(Runnable run)
    {
        this.onAccept = run;
    }

    @Override
    public Stage show(Object... args) {
        try {
            Stage stage = new Stage();
            FX.showScene("RiseLoader elua accept", "elua.fxml", stage, new EluaAcceptController(onAccept));
            FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", true);
            FX.setDraggable(stage.getScene(), "riseLogo");
            stage.setIconified(false);
            return stage;
        }
        catch (Exception e)
        {
            new ErrorBox().show(e);
        }

        return null;
    }

}
