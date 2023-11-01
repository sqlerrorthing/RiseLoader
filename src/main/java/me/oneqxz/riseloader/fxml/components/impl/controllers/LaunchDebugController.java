package me.oneqxz.riseloader.fxml.components.impl.controllers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class LaunchDebugController extends Controller {

    private Process process;
    private TextArea logs;

    private Button closeExitButton, copy;

    public LaunchDebugController(Process process) {
        this.process = process;
    }

    @Override
    protected void init() {
        this.logs = (TextArea) root.lookup("#logs");

        this.closeExitButton = (Button) root.lookup("#closeExitButton");
        this.copy = (Button) root.lookup("#copyButton");

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
                        logs.appendText("\n" + finalLine);
                    });
                }
                while ((line = stderrReader.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() ->
                    {
                        logs.appendText("\n" + finalLine);
                    });
                }


                int exitCode = process.waitFor();
                Platform.runLater(() ->
                {
                    logs.appendText("\n\nProcess exit with status code: " + exitCode);
                    this.closeExitButton.setText("Exit");
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    class StreamGobbler extends Thread {
        InputStream is;
        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                    System.out.println("!1!! "+line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
