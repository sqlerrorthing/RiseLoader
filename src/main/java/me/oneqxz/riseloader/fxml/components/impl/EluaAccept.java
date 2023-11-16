package me.oneqxz.riseloader.fxml.components.impl;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.Component;
import me.oneqxz.riseloader.fxml.components.impl.controllers.EluaAcceptController;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;

public class EluaAccept extends Component {

    private Runnable onAccept;

    public EluaAccept(Runnable run)
    {
        this.onAccept = run;
    }

    @Override
    public Stage show(Object... args) {
        try {
            Loading loading = new Loading();
            Stage loadingStage = loading.show(true);

            loading.setStageText("Getting user agreement");

            Stage stage = new Stage();
            new Thread(() ->
            {
                try {
                    Response resp = Requests.get(RiseUI.serverIp + "/elua");
                    if(resp.getStatusCode() == 200)
                    {
                        Platform.runLater(() ->
                        {
                            try
                            {
                                FX.showScene("RiseLoader elua accept", "elua.fxml", stage, new EluaAcceptController(onAccept, resp.getString()));
                                FX.setMinimizeAndClose(stage, "minimizeBtn", "closeBtn", true);
                                FX.setDraggable(stage.getScene(), "riseLogo");
                                stage.setIconified(false);
                                Loading.close(loadingStage);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Loading.close(loadingStage); new ErrorBox().show(e);
                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Platform.runLater(() -> {Loading.close(loadingStage); new ErrorBox().show(e);});
                }
            }).start();
            return stage;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            new ErrorBox().show(e);
        }

        return null;
    }

}
