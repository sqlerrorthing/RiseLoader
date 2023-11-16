package me.oneqxz.riseloader.fxml.controllers.impl;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;

import java.awt.*;
import java.net.URI;

public class MainController extends Controller {

    Button home, settings, scripts, discord, github;
    Text version, riseVersion;
    Rectangle background;

    @Override
    protected void init() {
        this.home = ((Button) root.lookup("#btnHome"));
        this.settings = ((Button) root.lookup("#btnSettings"));
        this.scripts = ((Button) root.lookup("#btnScripts"));

        this.discord = ((Button) root.lookup("#discord"));
        this.github = ((Button) root.lookup("#github"));

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

        this.discord.setOnMouseClicked(event ->
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://discord.com/invite/" + RiseInfo.getInstance().getDiscordInvite()));
            }
            catch (Exception e)
            {
                new ErrorBox().show(e);
            }
        });

        this.github.setOnMouseClicked(event ->
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://github.com/oneqxz/RiseLoader"));
            }
            catch (Exception e)
            {
                new ErrorBox().show(e);
            }
        });

        MainScene.setBackground(background);

        this.version.setText("Loader: " + RiseUI.version.getVersion());
    }

}
