package me.oneqxz.riseloader.fxml.controllers.impl;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;

import java.awt.*;
import java.net.URI;

public class MainController extends Controller {

    Button home, settings;
    Text version;

    @Override
    protected void init() {
        this.home = ((Button) root.lookup("#btnHome"));
        this.settings = ((Button) root.lookup("#btnSettings"));

        this.version = ((Text) root.lookup("#version"));

        this.home.setOnMouseClicked(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.HOME);
        });

        this.settings.setOnMouseClicked(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.SETTINGS);
        });

        this.version.setText("v" + RiseUI.version.getVersion());
    }

}
