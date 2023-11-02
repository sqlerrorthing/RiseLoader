package me.oneqxz.riseloader.fxml.controllers.impl.viewpage;

import animatefx.animation.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.controllers.impl.ClientLaunchController;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;
import me.oneqxz.riseloader.settings.Settings;

import java.io.IOException;


public class HomeController extends Controller {

    TextArea changelog;
    Button launchButton, versionsButton, versionBeta, versionRelease;

    Pane launchPane, versionsPane;

    @Override
    protected void init() {
        changelog = (TextArea) root.lookup("#changelog");

        launchButton = (Button) root.lookup("#launchButton");
        versionsButton = (Button) root.lookup("#versions");

        versionBeta = (Button) root.lookup("#versionBeta");
        versionRelease = (Button) root.lookup("#versionRelease");

        launchPane = (Pane) root.lookup("#launchPane");
        versionsPane = (Pane) root.lookup("#versionsPane");

        changelog.setText(RiseInfo.getInstance().getClientInfo().getChangelog());
        launchButton.setText("Launch " + Settings.getSettings().getString("version"));

        versionsButton.setOnMouseClicked((event) ->
        {
            showVersionsSelect();
        });

        versionBeta.setOnMouseClicked((event) ->
        {
            Settings.getSettings().set("version", "beta");
            launchButton.setText("Launch beta");
            showLaunch();
        });

        versionRelease.setOnMouseClicked((event) ->
        {
            Settings.getSettings().set("version", "release");
            launchButton.setText("Launch release");
            showLaunch();
        });

        launchButton.setOnMouseClicked((event) ->
        {
            try {
                Parent loading = FX.createNewParent("runState.fxml", new ClientLaunchController(), null);
                MainScene.addChildren(loading);
            } catch (IOException e) {
                new ErrorBox().show(e);
            }
        });
    }

    private void showLaunch()
    {
        versionsPane.setMouseTransparent(true);
        launchPane.setMouseTransparent(true);
        FadeIn launchPaneIn = new FadeIn(launchPane);
        launchPaneIn.setOnFinished((e) -> {
            launchPane.setVisible(true);
            launchPane.setMouseTransparent(false);
        });
        launchPaneIn.setSpeed(1.5);
        launchPaneIn.play();
        launchPane.setVisible(true);

        FadeOut versionsPaneOut = new FadeOut(versionsPane);
        versionsPaneOut.setOnFinished((e) -> {
            versionsPane.setVisible(false);
            versionsPane.setMouseTransparent(false);});
        versionsPaneOut.setSpeed(2);
        versionsPaneOut.play();
    }

    private void showVersionsSelect()
    {
        versionsPane.setMouseTransparent(true);
        launchPane.setMouseTransparent(true);
        FadeOut launchPaneOut = new FadeOut(launchPane);
        launchPaneOut.setOnFinished((e) -> {
            launchPane.setVisible(false);
            launchPane.setMouseTransparent(false);
        });
        launchPaneOut.setSpeed(2);
        launchPaneOut.play();
        versionsPane.setVisible(true);

        FadeIn versionsPaneIn = new FadeIn(versionsPane);
        versionsPaneIn.setOnFinished((e) -> {
            versionsPane.setVisible(true);
            versionsPane.setMouseTransparent(false);
        });
        versionsPaneIn.setSpeed(1.5);
        versionsPaneIn.play();
    }
}
