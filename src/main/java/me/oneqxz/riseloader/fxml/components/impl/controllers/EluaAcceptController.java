package me.oneqxz.riseloader.fxml.components.impl.controllers;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.oneqxz.riseloader.elua.Elua;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;

public class EluaAcceptController extends Controller {

    private Runnable onEluaAccept;

    private Button acceptButton, rejectButton;

    public EluaAcceptController(Runnable onEluaAccept) {
        this.onEluaAccept = onEluaAccept;
    }

    @Override
    protected void init() {
        ((Rectangle) root.lookup("#background")).setFill(new ImagePattern(new Image("/background.jpg")));

        this.acceptButton = (Button) root.lookup("#acceptButton");
        this.rejectButton = (Button) root.lookup("#rejectButton");

        this.rejectButton.setOnMouseClicked((e) -> {System.exit(-2);});

        this.acceptButton.setOnMouseClicked((e) ->
        {
            if(e.getButton() == MouseButton.SECONDARY)
            {
                Elua.getElua().acceptElua();
                onEluaAccept.run();
                this.stage.close();
            }
            else
            {
                this.acceptButton.setMouseTransparent(true);
                this.acceptButton.setText("Read the User Agreement!");
                this.acceptButton.getStyleClass().remove("grayButton");
                this.acceptButton.getStyleClass().add("dangerButton");

                FX.setTimeout(() ->
                {
                    this.acceptButton.setMouseTransparent(false);
                    this.acceptButton.setText("Accept");
                    this.acceptButton.getStyleClass().remove("dangerButton");
                    this.acceptButton.getStyleClass().add("grayButton");
                }, Duration.seconds(1));
            }
        });
    }

}
