package me.oneqxz.riseloader.fxml.scenes;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.animations.CFadeInLeft;
import me.oneqxz.riseloader.fxml.controllers.impl.MainController;
import me.oneqxz.riseloader.fxml.controllers.impl.viewpage.HomeController;
import me.oneqxz.riseloader.fxml.controllers.impl.viewpage.ScriptsController;
import me.oneqxz.riseloader.fxml.controllers.impl.viewpage.SettingsController;

import java.io.IOException;

public class MainScene {

    private static Scene scene;
    private static Stage stage;

    public static void createScene(Stage primaryStage) throws IOException {
        FX.showScene("Rise Loader v" + RiseUI.version.getVersion(), "main.fxml", primaryStage, new MainController());

        stage = primaryStage;
        scene = primaryStage.getScene();

        FX.setDraggable(scene, "topBar");
        FX.setMinimizeAndClose(stage, "hideButton", "closeButton", true);
        setCurrenViewPage(Page.HOME);
    }

    private static Node homeNode, settingsNode, scriptsNode;
    private static Page currentViewPage;

    public static void setCurrenViewPage(Page page)
    {
        if(currentViewPage == page)
            return;

        currentViewPage = page;

        Button home = ((Button) scene.getRoot().lookup("#btnHome"));
        Button settings = ((Button) scene.getRoot().lookup("#btnSettings"));
        Button scrips = ((Button) scene.getRoot().lookup("#btnScripts"));

        Pane pageContent = ((Pane) scene.getRoot().lookup("#pageContent"));

        home.getStyleClass().remove("navButtonActive");
        settings.getStyleClass().remove("navButtonActive");
        scrips.getStyleClass().remove("navButtonActive");

        Node pageParent = null;
        try
        {
            switch (page) {
                case HOME ->
                {
                    home.getStyleClass().add("navButtonActive");
                    pageParent = homeNode == null ? homeNode = FX.createNewParent("pages/home.fxml", new HomeController(), null) : homeNode;
                }
                case SETTINGS ->
                {
                    settings.getStyleClass().add("navButtonActive");
                    pageParent = settingsNode == null ? settingsNode = FX.createNewParent("pages/settings.fxml", new SettingsController(), null) : settingsNode;
                }
                case SCRIPTS ->
                {
                    scrips.getStyleClass().add("navButtonActive");
                    pageParent = scriptsNode == null ? scriptsNode = FX.createNewParent("pages/scripts.fxml", new ScriptsController(), null) : scriptsNode;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        pageContent.getChildren().clear();

        new CFadeInLeft(pageParent).setSpeed(2).play();
        pageContent.getChildren().add(pageParent);
        System.gc();
    }

    public static void addChildren(Node n)
    {
        Platform.runLater(() -> ((Pane) scene.getRoot()).getChildren().add(n));
    }

    public static void removeChildren(Node n)
    {
        Platform.runLater(() -> ((Pane) scene.getRoot()).getChildren().remove(n));
    }

    public static void hideSelf()
    {
        Platform.runLater(() -> stage.hide());
    }

    public static void showSelf()
    {
        Platform.runLater(() -> stage.show());
    }

    public static void closeSelf()
    {
        Platform.runLater(() -> stage.close());
    }

    public static enum Page {
        HOME, SETTINGS, SCRIPTS;
    }
}
