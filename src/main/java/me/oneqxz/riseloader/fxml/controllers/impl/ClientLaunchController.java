package me.oneqxz.riseloader.fxml.controllers.impl;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;
import me.oneqxz.riseloader.rise.run.RunClient;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientLaunchController extends Controller {

    Logger log = LogManager.getLogger("ClientLaunch");

    Button cancel;
    Label status;
    ProgressBar mainProgress, subProgress;
    boolean aborted = false;


    private void stop()
    {
        aborted = true;
        MainScene.removeChildren(root);
    }

    @Override
    protected void init() {
        status = (Label) root.lookup("#status");

        cancel = (Button) root.lookup("#cancelRun");

        mainProgress = (ProgressBar) root.lookup("#mainProgress");
        subProgress = (ProgressBar) root.lookup("#subProgress");

        cancel.setOnMouseClicked((event) -> stop());

        subProgress.setVisible(false);

        Path rootPath = OSUtils.getRiseFolder();
        File rootDir = rootPath.toFile();
        RiseInfo info = RiseInfo.getInstance();

        if(info == null)
        {
            MainScene.closeSelf();
            new ErrorBox().show(new IllegalStateException("RiseInfo is null!"));
        }

        if(!rootDir.exists())
            rootDir.mkdirs();

        status.setText("Starting checking files hash...");
        int filesToCheck = info.getJava().keySet().size() + info.getNatives().keySet().size() + info.getRise().keySet().size();
        AtomicInteger checkedFiles = new AtomicInteger();
        new Thread(() ->
        {
            checkHashesOrDownload(info.getJava(), rootDir.getAbsolutePath(), "java", checkedFiles, filesToCheck);
            checkHashesOrDownload(info.getNatives(), rootDir.getAbsolutePath(), "natives", checkedFiles, filesToCheck);
            checkHashesOrDownload(info.getRise(), rootDir.getAbsolutePath(), "rise", checkedFiles, filesToCheck);
            checkAssets();

            if(!aborted)
            {
                Platform.runLater(() ->
                {
                    cancel.setDisable(true);
                    status.setText("Starting...");
                    mainProgress.setVisible(false);
                    subProgress.setVisible(false);
                });

                RunClient.run(root);
            }
        }).start();
    }

    private void checkHashesOrDownload(JSONObject object, String root, String sub, AtomicInteger checkedFiles, int filesToCheck)
    {

        for (String filePath : object.keySet())
        {
            if(aborted)
                return;

            if(sub.equals("java"))
            {
                if(OSUtils.getOS() == OSUtils.OS.WINDOWS)
                {
                    if(!filePath.startsWith("windows"))
                    {
                        checkedFiles.getAndIncrement();
                        continue;
                    }
                }
                else if(OSUtils.getOS() == OSUtils.OS.LINUX)
                {
                    if(!filePath.startsWith("linux"))
                    {
                        checkedFiles.getAndIncrement();
                        continue;
                    }
                }
                else if(OSUtils.getOS() == OSUtils.OS.MACOS)
                {
                    if(!filePath.startsWith("macos"))
                    {
                        checkedFiles.getAndIncrement();
                        continue;
                    }
                }
            }

            File file = new File(root, sub + "\\" + filePath);
            Platform.runLater(() -> {status.setText("Checking hash for " + file.getName());});
            String hash = object.getString(filePath);
            if(!checkFileHash(file, hash))
            {
                Platform.runLater(() ->
                {
                    status.setText("Downloading file " + file.getName());
                    subProgress.setVisible(true);
                    subProgress.setProgress(0);
                });
                downloadFile(RiseUI.serverIp + "/file/"+sub+"/" + filePath.replace("\\", "/"), file.getAbsolutePath());
                Platform.runLater(() ->
                {
                    subProgress.setVisible(false);
                });
            }

            checkedFiles.getAndIncrement();
            Platform.runLater(() ->
            {
                mainProgress.setProgress((double) checkedFiles.get() / filesToCheck);
            });
        }
    }

    private void checkAssets()
    {
        if(aborted)
            return;

        File assetsFolder = new File(OSUtils.getRiseFolder().toFile(), "run\\assets");
        if(!assetsFolder.exists())
        {
            File tempFile;
            try
            {
                tempFile = File.createTempFile("asts", ".zip");
                tempFile.deleteOnExit();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                stop();
                new ErrorBox().show(e);
                return;
            }

            Platform.runLater(() ->
            {
                status.setText("Downloading assets...");
                subProgress.setVisible(true);
                subProgress.setProgress(0);
            });
            downloadFile(RiseUI.serverIp + "/file/assets.zip", tempFile.getAbsolutePath());
            Platform.runLater(() ->
            {
                status.setText("Unzipping assets...");
                subProgress.setVisible(false);
                subProgress.setProgress(0);
            });
            unzip(tempFile, new File(OSUtils.getRiseFolder().toFile(), "run").toPath());
            tempFile.delete();
        }
    }

    private void unzip(File zipFile, Path to)
    {
        try (ZipArchiveInputStream zipInput = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
            ArchiveEntry entry;
            byte[] buffer = new byte[8192];

            long totalBytes = zipFile.length();
            long bytesRead = 0;

            AtomicInteger lastPercent = new AtomicInteger();
            while ((entry = zipInput.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                File outputFile = to.resolve(entry.getName()).toFile();
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }

                try (OutputStream output = new FileOutputStream(outputFile)) {
                    int bytesReadInEntry;
                    while ((bytesReadInEntry = zipInput.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesReadInEntry);
                        bytesRead += bytesReadInEntry;
                        int percent = (int) ((bytesRead * 100) / totalBytes);
                        if(percent > (lastPercent.get() +1))
                        {
                            Platform.runLater(() ->
                            {
                                mainProgress.setProgress(percent / 100.0);
                                lastPercent.set(percent);
                            });
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            stop();
            new ErrorBox().show(e);
            return;
        }
    }

    private boolean checkFileHash(File file, String md5Hash) {
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            fis.close();
            byte[] digest = md.digest();

            StringBuilder md5HashBuilder = new StringBuilder();
            for (byte b : digest) {
                md5HashBuilder.append(String.format("%02x", b));
            }
            String calculatedMd5Hash = md5HashBuilder.toString();

            return calculatedMd5Hash.equals(md5Hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void downloadFile(String fileUrl, String savePath) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                int fileSize = connection.getContentLength();
                InputStream inputStream = connection.getInputStream();
                File outputFile = new File(savePath);

                File parentDir = outputFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                int lastProgress = 0;
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        int progress = (int) ((totalBytesRead * 100) / fileSize);
                        if(progress > (lastProgress+1))
                        {
                            Platform.runLater(() ->
                            {
                                subProgress.setProgress((double) progress / 100);
                            });
                            lastProgress++;
                        }
                    }
                }
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
