package me.oneqxz.riseloader.fxml.components.impl.controllers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.rpc.DiscordRichPresence;
import me.oneqxz.riseloader.fxml.scenes.MainScene;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;


public class LaunchDebugController extends Controller {

    private Process process;
    private TextArea logs;

    private Button closeExitButton, copy;

    public LaunchDebugController(Process process) {
        this.process = process;
    }

    @Override
    protected void init() {
        DiscordRichPresence.getInstance().updateState("In game");
        this.logs = (TextArea) root.lookup("#logs");

        this.closeExitButton = (Button) root.lookup("#closeExitButton");
        this.copy = (Button) root.lookup("#copyButton");

        ((Rectangle) root.lookup("#background")).setFill(new ImagePattern(new Image("/background.jpg")));

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));


        this.copy.setOnMouseClicked((e) ->
        {
            StringSelection stringSelection = new StringSelection(logs.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, stringSelection);
        });

        this.closeExitButton.setOnMouseClicked((e) ->
        {
            if(process.isAlive())
            {
                process.destroy();
            }
            else
            {
                DiscordRichPresence.getInstance().updateState("In Home page");
                MainScene.showSelf();
                stage.close();
            }
        });

        new Thread(() ->
        {
            try
            {
                String line;
                while ((line = stdoutReader.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() ->
                    {
                        logs.appendText((logs.getText().isEmpty() ? "" : "\n") + finalLine);
                    });
                }
                while ((line = stderrReader.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() ->
                    {
                        logs.appendText((logs.getText().isEmpty() ? "" : "\n") + finalLine);
                    });
                }


                int exitCode = process.waitFor();
                Platform.runLater(() ->
                {
                    logs.appendText("\n\nProcess exit with status code: " + exitCode);
                    DiscordRichPresence.getInstance().updateState("In Debug view");
                    this.closeExitButton.setText("Exit");
                    this.closeExitButton.getStyleClass().remove("dangerButton");
                    this.closeExitButton.getStyleClass().add("defaultButton");
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }
}
