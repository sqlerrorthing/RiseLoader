package me.oneqxz.riseloader.fxml.scenes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.controllers.impl.MainController;
import me.oneqxz.riseloader.fxml.controllers.impl.viewpage.HomeController;
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

    public static void setCurrenViewPage(Page page)
    {
        Button home = ((Button) scene.getRoot().lookup("#btnHome"));
        Button settings = ((Button) scene.getRoot().lookup("#btnSettings"));
        Button statistics = ((Button) scene.getRoot().lookup("#btnStatistics"));
        Pane pageContent = ((Pane) scene.getRoot().lookup("#pageContent"));


        home.getStyleClass().remove("navButtonActive");
        settings.getStyleClass().remove("navButtonActive");
        statistics.getStyleClass().remove("navButtonActive");

        Parent pageParent = null;
        try
        {
            switch (page) {
                case HOME ->
                {
                    home.getStyleClass().add("navButtonActive");
                    pageParent = FX.createNewParent("pages/home.fxml", new HomeController(), null);
                }
                case SETTINGS ->
                {
                    settings.getStyleClass().add("navButtonActive");
                    pageParent = FX.createNewParent("pages/settings.fxml", new SettingsController(), null);
                }
                case STATISTICS ->
                {
                    statistics.getStyleClass().add("navButtonActive");
                    pageParent = FX.createNewParent("pages/statistics.fxml", new SettingsController(), null);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        pageContent.getChildren().clear();
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
        HOME, SETTINGS, STATISTICS;
    }
}
