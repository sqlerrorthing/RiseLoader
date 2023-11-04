package me.oneqxz.riseloader.fxml.components.impl.controllers;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import me.oneqxz.riseloader.RiseLoaderMain;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.utils.OSUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class UpdatingController extends Controller {

    ProgressBar mainProgress;

    @Override
    protected void init() {
        this.mainProgress = (ProgressBar) root.lookup("#mainProgress");


        new Thread(() ->
        {
            try {
                String currentFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

                String tempFilePath = "update" + new Random().nextInt(10000) + ".temp_jar";

                downloadUpdate(RiseUI.serverIp + "/file/loader.jar", tempFilePath);
                replaceCurrentFile(currentFilePath, tempFilePath);
                restartApplication();
            } catch (Exception e) {
                Platform.runLater(() ->
                {
                    new ErrorBox().show(e);
                    stage.close();
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void downloadUpdate(String updateURL, String tempFilePath) throws IOException {
        URL url = new URL(updateURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFilePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;
            long contentLength = connection.getContentLength();
            int percentCompleted = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                int newPercentCompleted = (int) (totalBytesRead * 100 / contentLength);

                if (newPercentCompleted > percentCompleted) {
                    percentCompleted = newPercentCompleted;
                    int finalPercentCompleted = percentCompleted;
                    Platform.runLater(() ->
                    {
                        mainProgress.setProgress(finalPercentCompleted / 100.0);
                    });
                }
            }
        }
    }

    private void replaceCurrentFile(String currentFilePath, String tempFilePath) throws IOException {
        File tempFile = new File(tempFilePath);
        if (tempFile.exists() && tempFile.isFile()) {

            URL url = tempFile.toPath().toUri().toURL();
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream(currentFilePath);

            byte[] buffer = new byte[2048];
            int count;
            while((count = bis.read(buffer,0,2048)) != -1)
            {
                fis.write(buffer, 0, count);
            }
            fis.close();
            bis.close();
            tempFile.delete();
        }
    }

    private void restartApplication() throws IOException, URISyntaxException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        if(OSUtils.getOS() == OSUtils.OS.WINDOWS)
            if(new File("C:\\Program Files\\Zulu\\zulu-17-jre\\bin\\java.exe").exists())
                java = "C:\\Program Files\\Zulu\\zulu-17-jre\\bin\\java.exe";

        String[] cmd = {java, "-jar", new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath()};

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.start();
        System.exit(0);
    }

}
