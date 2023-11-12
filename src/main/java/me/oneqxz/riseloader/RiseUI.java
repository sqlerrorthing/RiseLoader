package me.oneqxz.riseloader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.oneqxz.riseloader.elua.Elua;
import me.oneqxz.riseloader.fxml.components.impl.EluaAccept;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.components.impl.Loading;
import me.oneqxz.riseloader.fxml.components.impl.Updater;
import me.oneqxz.riseloader.fxml.rpc.DiscordRichPresence;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.ClientInfo;
import me.oneqxz.riseloader.rise.RiseInfo;
import me.oneqxz.riseloader.rise.pub.PublicInstance;
import me.oneqxz.riseloader.rise.versions.PublicBeta;
import me.oneqxz.riseloader.rise.versions.Release;
import me.oneqxz.riseloader.settings.Settings;
import me.oneqxz.riseloader.utils.OSUtils;
import me.oneqxz.riseloader.utils.Version;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class RiseUI extends Application {

    private static final Logger log = LogManager.getLogger("RiseLoader");
    public static final Version version = new Version("1.0.5");
    public static final String serverIp = "http://riseloader.0x22.xyz";

    @Override
    public void start(Stage stage) throws IOException {
        if(!Elua.getElua().isEluaAccepted())
        {
            new EluaAccept(() -> {try{load(stage);}catch (Exception e){e.printStackTrace();System.exit(0);}}).show();
        }
        else
        {
            load(stage);
        }
    }

    private void load(Stage stage) throws IOException
    {
        Loading loading = new Loading();
        Stage loadingStage = loading.show(true);

        loading.setStageText("Detecting os");
        if(OSUtils.getOS() == OSUtils.OS.UNDEFINED)
        {
            log.info("Cannot detect OS!");
            loadingStage.close();
            new ErrorBox().show(new IllegalStateException("Can't detect OS! " + OSUtils.getOS().name()));
        }
        else
        {
            log.info("Detected OS: " + OSUtils.getOS().name());
        }

        loading.setStageText("Initializing DiscordRPC");
        DiscordRichPresence.getInstance();
        loading.setStageText("DiscordRPC ready");

        Thread thread = new Thread(() ->
        {
            loading.setStageTextLater("Fetching scripts");
            PublicInstance.getInstance();

            loading.setStageTextLater("Parsing settings");
            Settings.getSettings();

            loading.setStageTextLater("Fetching rise files");
            log.info("Getting Rise information...");
            Response resp = null;
            try {
                resp = Requests.get(serverIp + "/loader");
            } catch (IOException e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    loadingStage.close();
                    new ErrorBox().show(e);
                });

                return;
            }
            if(resp.getStatusCode() == 200)
            {
                try {
                    loading.setStageTextLater("Parsing rise files");
                    log.info(resp.getStatusCode() + ", writing info");
                    JSONObject json = resp.getJSON();

                    JSONObject files = json.getJSONObject("files");
                    JSONObject client = json.getJSONObject("client");

                    if(version.needToUpdate(client.getString("loader_version")))
                    {
                        loading.setStageTextLater("Updating...");
                        log.info("New version detected! Updating...");
                        try {
                            Platform.runLater(() ->
                            {
                                Loading.close(loadingStage);
                                try {
                                    new Updater().show();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            return;
                        }
                        catch (Exception e)
                        {
                            new ErrorBox().show(e);
                            e.printStackTrace();
                        }
                    }

                    JSONObject versions = client.getJSONObject("versions");

                    PublicBeta publicBeta = new PublicBeta(versions.getJSONObject("beta").getString("version"), versions.getJSONObject("beta").getLong("lastUpdated"));
                    loading.setStageTextLater("Parsed Public beta version");
                    Release release = new Release(versions.getJSONObject("release").getString("version"), versions.getJSONObject("release").getLong("lastUpdated"));
                    loading.setStageTextLater("Parsed Release version");

                    RiseInfo.createNew(
                            files.getJSONObject("natives"),
                            files.getJSONObject("java"),
                            files.getJSONObject("rise"),
                            new ClientInfo(publicBeta, release, client.getString("release_changelog"), client.getString("loader_version"))
                    );

                    loading.setStageTextLater("Finalizing...");
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
                catch (Exception e)
                {
                    Platform.runLater(() -> {
                        loadingStage.close();
                        new ErrorBox().show(e);
                    });
                }
            }
            else
            {
                Platform.runLater(() -> {
                    loadingStage.close();
                    new ErrorBox().show(new RuntimeException("/loader returned invalid status code"));
                });
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            if(DiscordRichPresence.isRegistered())
                DiscordRichPresence.getInstance().shutdown();
        }));
        launch();
    }
}