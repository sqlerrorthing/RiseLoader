package me.oneqxz.riseloader.fxml.controllers.impl;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;

import java.net.URI;

public class MainController extends Controller {

    Button home, settings, scripts;
    Text version, riseVersion;
    Rectangle background;

    @Override
    protected void init() {
        this.home = ((Button) root.lookup("#btnHome"));
        this.settings = ((Button) root.lookup("#btnSettings"));
        this.scripts = ((Button) root.lookup("#btnScripts"));

        this.background = ((Rectangle) root.lookup("#background"));

        this.version = ((Text) root.lookup("#version"));
        this.riseVersion = ((Text) root.lookup("#riseReleaseVersion"));

        this.riseVersion.setText(RiseInfo.getInstance().getClientInfo().getRelease().getVersion());

        this.home.setOnMouseClicked(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.HOME);
        });

        this.settings.setOnMouseClicked(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.SETTINGS);
        });

        this.scripts.setOnMouseClicked(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.SCRIPTS);
        });

        Image bg = new Image("/background.jpg");
        background.setFill(new ImagePattern(bg));

        this.version.setText("Loader: " + RiseUI.version.getVersion());
    }

}
