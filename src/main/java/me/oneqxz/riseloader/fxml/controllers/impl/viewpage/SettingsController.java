package me.oneqxz.riseloader.fxml.controllers.impl.viewpage;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.settings.Settings;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SettingsController extends Controller {

    private Slider memorySlider;
    private Label memorySelected;

    private CheckBox fullscreen;
    private TextField screenWidth, screenHeight;

    @Override
    protected void init() {
        memorySlider = (Slider) root.lookup("#memorySlider");
        memorySelected = (Label) root.lookup("#memorySelected");

        fullscreen = (CheckBox) root.lookup("#fullscreen");
        screenWidth = (TextField) root.lookup("#screenWidth");
        screenHeight = (TextField) root.lookup("#screenHeight");

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
