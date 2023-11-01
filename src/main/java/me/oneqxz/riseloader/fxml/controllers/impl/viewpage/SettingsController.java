package me.oneqxz.riseloader.fxml.controllers.impl.viewpage;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.settings.Settings;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.commons.io.FileUtils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SettingsController extends Controller {

    private Slider memorySlider;
    private Label memorySelected;

    private CheckBox fullscreen;
    private TextField screenWidth, screenHeight;

    private Button openFolder, deleteSaves, deleteClient;

    @Override
    protected void init() {
        memorySlider = (Slider) root.lookup("#memorySlider");
        memorySelected = (Label) root.lookup("#memorySelected");

        fullscreen = (CheckBox) root.lookup("#fullscreen");
        screenWidth = (TextField) root.lookup("#screenWidth");
        screenHeight = (TextField) root.lookup("#screenHeight");

        openFolder = (Button) root.lookup("#openFolder");
        deleteSaves = (Button) root.lookup("#deleteSaves");
        deleteClient = (Button) root.lookup("#deleteClient");

        openFolder.setOnMouseClicked((event) ->
        {
            try {
                Desktop.getDesktop().open(OSUtils.getRiseFolder().toFile());
            } catch (IOException e) {
                new ErrorBox().show(e);
                e.printStackTrace();
            }
        });

        deleteSaves.setOnMouseClicked((event) ->
        {
            File saves = new File(OSUtils.getRiseFolder().toFile(), "run");
            try {if(saves.exists())FileUtils.deleteDirectory(saves);}
            catch (Exception e)
            {e.printStackTrace();new ErrorBox().show(new IllegalStateException("Can't delete folder " + saves.getAbsolutePath()), false);}
        });

        deleteClient.setOnMouseClicked((event) ->
        {
            File java = new File(OSUtils.getRiseFolder().toFile(), "java");
            try {if(java.exists())FileUtils.deleteDirectory(java);}
            catch (Exception e)
            {e.printStackTrace();new ErrorBox().show(new IllegalStateException("Can't delete folder " + java.getAbsolutePath()), false);}

            File natives = new File(OSUtils.getRiseFolder().toFile(), "natives");
            try {if(natives.exists())FileUtils.deleteDirectory(natives);}
            catch (Exception e)
            {e.printStackTrace();new ErrorBox().show(new IllegalStateException("Can't delete folder " + natives.getAbsolutePath()), false);}

            File rise = new File(OSUtils.getRiseFolder().toFile(), "rise");
            try {if(rise.exists())FileUtils.deleteDirectory(rise);}
            catch (Exception e)
            {e.printStackTrace();new ErrorBox().show(new IllegalStateException("Can't delete folder " + rise.getAbsolutePath()), false);}

            System.gc();
        });

        long totalMemory = 0;
        try
        {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang","type","OperatingSystem"), "TotalPhysicalMemorySize");
            totalMemory = Long.parseLong(attribute.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        memorySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            memorySelected.setText(String.valueOf(Math.round((newValue.intValue() / 1024.0) * 10.0) / 10.0) + "GB");
            Settings.getSettings().set("preferences.memory", ((int)newValue.intValue()));
        });
        memorySlider.setMax((double) totalMemory / (1024 * 1024));
        memorySlider.setValue(Settings.getSettings().getInt("preferences.memory", 2048));

        fullscreen.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getSettings().set("preferences.resolution.fullscreen", newValue);
        });

        fullscreen.setSelected(Settings.getSettings().getBoolean("preferences.resolution.fullscreen", false));

        screenWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.isEmpty()) return;

            Settings.getSettings().set("preferences.resolution.width", Integer.parseInt(newValue));
        });
        screenHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.isEmpty()) return;

            Settings.getSettings().set("preferences.resolution.height", Integer.parseInt(newValue));
        });

        screenWidth.setText(String.valueOf(Settings.getSettings().getInt("preferences.resolution.width", 854)));
        screenHeight.setText(String.valueOf(Settings.getSettings().getInt("preferences.resolution.height", 480)));

        onlyNumbers(screenWidth);
        onlyNumbers(screenHeight);
    }
}
