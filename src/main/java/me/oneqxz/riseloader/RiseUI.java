package me.oneqxz.riseloader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.components.impl.Loading;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.ClientInfo;
import me.oneqxz.riseloader.rise.RiseInfo;
import me.oneqxz.riseloader.settings.Settings;
import me.oneqxz.riseloader.utils.Version;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class RiseUI extends Application {

    private static final Logger log = LogManager.getLogger("RiseLoader");
    public static final Version version = new Version("1.0.0");
    public static final String serverIp = "http://localhost:55503";

    @Override
    public void start(Stage stage) throws IOException {
        Stage loadingStage = new Loading().show(true);

        Thread thread = new Thread(() ->
        {
            Settings.getSettings();
            log.info("Getting Rise information...");
            Response resp = null;
            try {
                resp = Requests.get(serverIp + "/loader");
            } catch (IOException e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    try {
                        loadingStage.close();
                        new ErrorBox().show(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                return;
            }
            if(resp.getStatusCode() == 200)
            {
                log.info(resp.getStatusCode() + ", writing info");
                JSONObject json = resp.getJSON();

                JSONObject files = json.getJSONObject("files");
                JSONObject client = json.getJSONObject("client");

                RiseInfo.createNew(
                        files.getJSONObject("natives"),
                        files.getJSONObject("java"),
                        files.getJSONObject("rise"),
                        new ClientInfo(client.getString("client_version"), client.getString("client_changelog"), client.getString("loader_version"))
                );

                Platform.runLater(() ->
                {
                    try {
                        Loading.close(loadingStage);
                        MainScene.createScene(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else
            {
                throw new RuntimeException("/loader returned invalid status code, " + resp.getStatusCode());
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}