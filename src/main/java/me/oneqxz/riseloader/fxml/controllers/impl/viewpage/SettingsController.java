package me.oneqxz.riseloader.fxml.controllers.impl.viewpage;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeOut;
import animatefx.animation.FadeOutUp;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.FX;
import me.oneqxz.riseloader.fxml.animations.personalization.*;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.controllers.Controller;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.settings.Settings;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.commons.io.FileUtils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SettingsController extends Controller {

    private Slider memorySlider;
    private Label memorySelected;

    private CheckBox fullscreen, javaoptimize;
    private TextField screenWidth, screenHeight;

    private Button openFolder, deleteSaves, deleteClient;

    private SimpleBooleanProperty toggledPersonalization = new SimpleBooleanProperty(false);
    private Pane personalizationToggle, personalizationPane;
    private ImageView personalizationToggleImage;
    private Text togglerText;
    private Pane settingsPane;

    @Override
    protected void init() {
        memorySlider = (Slider) root.lookup("#memorySlider");
        memorySelected = (Label) root.lookup("#memorySelected");

        fullscreen = (CheckBox) root.lookup("#fullscreen");
        screenWidth = (TextField) root.lookup("#screenWidth");
        screenHeight = (TextField) root.lookup("#screenHeight");

        javaoptimize = (CheckBox) root.lookup("#javaoptimize");

        openFolder = (Button) root.lookup("#openFolder");
        deleteSaves = (Button) root.lookup("#deleteSaves");
        deleteClient = (Button) root.lookup("#deleteClient");

        settingsPane = (Pane) root.lookup("#settingsPane");

        personalizationToggle = (Pane) root.lookup("#personalizationToggle");
        personalizationPane = (Pane) root.lookup("#personalizationPane");
        personalizationToggleImage = (ImageView) root.lookup("#personalizationToggleImage");
        togglerText = (Text) root.lookup("#togglerText");

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(((AnchorPane) this.root).widthProperty());
        clip.heightProperty().bind(((AnchorPane) this.root).heightProperty());
        clip.layoutXProperty().bind(((AnchorPane) this.root).layoutXProperty());
        clip.layoutYProperty().bind(((AnchorPane) this.root).layoutYProperty());
        ((AnchorPane) this.root).setClip(clip);

        try {
            Node personalization = FX.createNewParent("pages/child/personalization.fxml", new PersonalizationController(), null);
            personalizationPane.getChildren().add(personalization);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            new ErrorBox().show(e);
        }

        personalizationPane.setScaleX(0);
        personalizationPane.setScaleY(0);

        toggledPersonalization.addListener((observable, oldValue, newValue) ->
        {
            personalizationToggle.setMouseTransparent(true);
            FX.setTimeout(() ->
            {
                personalizationToggle.setMouseTransparent(false);
            }, Duration.millis(900));

            if(newValue)
            {
                new RotateMinus180(personalizationToggleImage).play();
                new TranslateDown(personalizationToggleImage).play();
                new FadeOutUp(settingsPane).play();
                new TextTranslate(togglerText, "PERSONALIZATION", "SETTINGS", -35).play();
                new TranslateY(personalizationToggle, -301 - 5).play();
                personalizationPane.setVisible(true);
                new TranslateY(personalizationPane, -301).play();
                new FadeIn(personalizationPane).play();
                new TranslateScale(settingsPane, 0).play();
                new TranslateScale(personalizationPane, 1).play();
            }
            else
            {
                new TranslateScale(settingsPane, 1).play();
                new TranslateScale(personalizationPane, 0).play();
                new RotatePlus180(personalizationToggleImage).play();
                new TranslateUP(personalizationToggleImage).play();
                new TextTranslate(togglerText, "SETTINGS", "PERSONALIZATION", 35).play();
                new FadeInDown(settingsPane).play();
                new TranslateY(personalizationToggle, 301 + 5).play();
                new TranslateY(personalizationPane, 301).play();
                new FadeOut(personalizationPane).play();
            }
        });

        personalizationToggle.setOnMouseClicked((e) ->
        {
            toggledPersonalization.setValue(!toggledPersonalization.getValue());
        });

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

        javaoptimize.setSelected(Settings.getSettings().getBoolean("others.javaoptimize", true));

        javaoptimize.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getSettings().set("others.javaoptimize", newValue);
        });
    }

    private class PersonalizationController extends Controller
    {

        private Button selectBackground, backgroundDefault;

        @Override
        protected void init() {
            selectBackground = (Button) root.lookup("#selectBackground");
            backgroundDefault = (Button) root.lookup("#backgroundDefault");

            backgroundDefault.setOnMouseClicked((s) ->
            {
                RiseUI.backgroundImage.setValue(new Image("/background.jpg"));
                Settings.getSettings().getString("personalization.background", null);
            });

            selectBackground.setOnMouseClicked((e) ->
            {
                FileChooser fileChooser = new FileChooser();

                // Установка фильтров для расширений файлов
                FileChooser.ExtensionFilter imageFilter =
                        new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif");
                fileChooser.getExtensionFilters().add(imageFilter);

                File selectedFile = fileChooser.showOpenDialog(MainScene.getStage());

                if (selectedFile != null) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                        Image image = new Image(fileInputStream);

                        if(image.isError())
                        {
                            new ErrorBox().show(new IllegalStateException("Image has errors, please choose a different image"));
                            return;
                        }

                        Path sourcePath = Paths.get(selectedFile.getAbsolutePath());

                        Path destinationFolder = Paths.get(OSUtils.getRiseFolder().toAbsolutePath().toString() + "\\.config");
                        if (!Files.exists(destinationFolder)) {
                            Files.createDirectories(destinationFolder);
                        }

                        Path destinationPath = destinationFolder.resolve("background" + OSUtils.getFileExtension(sourcePath));

                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        RiseUI.backgroundImage.setValue(new Image("file:///" + destinationPath.toFile().getAbsolutePath()));
                        Settings.getSettings().set("personalization.background", "background" + OSUtils.getFileExtension(sourcePath));

                    }
                    catch (Exception ex)
                    {
                        new ErrorBox().show(ex);
                        ex.printStackTrace();
                    }
                }
            });
        }
    }
}
