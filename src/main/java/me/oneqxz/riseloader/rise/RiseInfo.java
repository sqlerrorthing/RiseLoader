package me.oneqxz.riseloader.rise;

import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class RiseInfo {

    private static RiseInfo instance;
    private static final Logger log = LogManager.getLogger("Rise info");


    private JSONObject natives;
    private JSONObject java;
    private JSONObject rise;
    private ClientInfo clientInfo;

    private RiseInfo(@NotNull JSONObject natives, @NotNull JSONObject java, @NotNull JSONObject rise, @NotNull ClientInfo clientInfo) {
        this.natives = natives;
        this.java = java;
        this.rise = rise;
        this.clientInfo = clientInfo;
    }

    public static RiseInfo getInstance()
    {
        return instance;
    }

    public static void createNew(JSONObject n, JSONObject j, JSONObject r, ClientInfo c)
    {
        instance = new RiseInfo(n, j, r, c);
    }
    public JSONObject getNatives() {
        return natives;
    }

    public JSONObject getJava() {
        return java;
    }

    public JSONObject getRise() {
        return rise;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
