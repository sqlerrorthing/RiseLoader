package me.oneqxz.riseloader.fxml;

import animatefx.animation.Bounce;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.function.Consumer;

public class FX {

    private static double xOffset = 0;
    private static double yOffset = 0;

    private static final Logger logger = LogManager.getLogger("FXUtils");

    public static void showScene(String title, String resource, Stage primaryStage, Controller controller) throws IOException {
        showScene(title, resource, primaryStage, controller, null);
    }
    public static void showScene(String title, String resource, Stage primaryStage) throws IOException {
        showScene(title, resource, primaryStage, null, null);
    }

    public static void showScene(String title, String resource, Stage primaryStage, Controller controller, Consumer<Parent> runnable) throws IOException {
        Scene scene = createNewScene(resource, controller, primaryStage);
        showScene(scene, title, primaryStage, runnable);
    }

    public static void showScene(Scene scene, String title, Stage primaryStage, Consumer<Parent> runnable) throws IOException {
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        primaryStage.getIcons().add(new javafx.scene.image.Image("/logo.png"));

        primaryStage.show();

        if(runnable != null)
            runnable.accept(scene.getRoot());
    }

    public static Scene createNewScene(String resource, Controller controller, Stage stage) throws IOException {
        Scene scene = new Scene(createNewParent(resource, controller, stage));
        scene.setFill(Color.TRANSPARENT);
        new FadeIn(scene.getRoot()).play();

        logger.info("JavaFX scene will created with name " + resource);
        return scene;
    }

    public static Parent createNewParent(String resource, Controller controller, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(FX.class.getResource("/fxml/" + resource));
        if(controller != null)
            loader.setController(controller);

        Parent root = loader.load();

        logger.debug("JavaFX parent will created with name " + resource);

        if(controller != null)
            controller.init(root, stage);

        return root;
    }

    public static void setTimeout(Runnable runnable, Duration duration) {
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            runnable.run();
        }));
        timeline.play();
    }

    public static void setDraggable(Scene scene, String paneId)
    {
        Node root = scene.getRoot().lookup("#" + paneId);

        root.setOnMousePressed((event) ->
        {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseReleased((event) ->
        {
            xOffset = 0;
            yOffset = 0;
        });
        root.setOnMouseDragged((event) ->
        {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void setMinimizeAndClose(Stage stage, String minimiseID, String closeID)
    {
        setMinimizeAndClose(stage, minimiseID, closeID, false);
    }

    public static void setMinimizeAndClose(Stage stage, String minimiseID, String closeID, boolean closeProgram)
    {
        setMinimizeAndClose(stage, minimiseID, closeID, null, closeProgram, false);
    }

    public static void setMinimizeAndClose(Stage stage, String minimiseID, String closeID, Runnable onClose, boolean closeProgram, boolean hideScene)
    {
        Node min = stage.getScene().getRoot().lookup("#" + minimiseID);
        Node close = stage.getScene().getRoot().lookup("#" + closeID);

        min.setOnMouseClicked((event) -> stage.setIconified(true));
        close.setOnMouseClicked((event) ->
        {
            if(hideScene)
                stage.hide();
            else stage.close();

            if(onClose != null)
                onClose.run();

            if(closeProgram)
            {
                logger.info("Goodbye!");
                System.exit(0);
            }
        });
    }
}
