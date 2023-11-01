package me.oneqxz.riseloader.fxml.controllers.impl;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;

public class MainController extends Controller {

    Button home;
    Button settings;
    Button statistics;
    Text version;

    @Override
    protected void init() {
        this.home = ((Button) root.lookup("#btnHome"));
        this.settings = ((Button) root.lookup("#btnSettings"));
        this.statistics = ((Button) root.lookup("#btnStatistics"));
        this.version = ((Text) root.lookup("#version"));

        this.home.setOnAction(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.HOME);
        });

        this.settings.setOnAction(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.SETTINGS);
        });

        this.statistics.setOnAction(event -> {
            MainScene.setCurrenViewPage(MainScene.Page.STATISTICS);
        });

        this.version.setText("v" + RiseUI.version.getVersion());
    }

}
