package me.oneqxz.riseloader.fxml.controllers.impl.viewpage;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.controllers.impl.ClientLaunchController;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;

import java.io.IOException;


public class HomeController extends Controller {

    TextArea changelog;
    Text riseVersion;
    Button launchButton;
    Node launchNode;

    @Override
    protected void init() {
        changelog = (TextArea) root.lookup("#changelog");
        riseVersion = (Text) root.lookup("#riseVersion");
        launchButton = (Button) root.lookup("#launchButton");

        changelog.setText(RiseInfo.getInstance().getClientInfo().getChangelog());
        riseVersion.setText(RiseInfo.getInstance().getClientInfo().getClientVersion());

        launchButton.setOnMouseClicked((event) ->
        {
            try {
                Parent loading = FX.createNewParent("runState.fxml", new ClientLaunchController(), null);
                MainScene.addChildren(loading);
            } catch (IOException e) {
                try {
                    new ErrorBox().show(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
