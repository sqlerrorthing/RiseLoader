package me.oneqxz.riseloader.fxml.components;

import javafx.stage.Stage;

import java.io.IOException;

public abstract class Component {

    public abstract Stage show(Object... args) throws IOException;
    public static void close(Stage stage)
    {
        stage.close();
    }
}
