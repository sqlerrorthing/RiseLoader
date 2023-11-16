package me.oneqxz.riseloader.rise;

import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.startup.IStartCommand;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class RiseInfo {

    private static RiseInfo instance;

    private JSONObject natives;
    private JSONObject java;
    private JSONObject rise;
    private ClientInfo clientInfo;
    private String discordInvite;
    private IStartCommand normal, optimized;

    private RiseInfo(@NotNull JSONObject natives, @NotNull JSONObject java, @NotNull JSONObject rise, @NotNull ClientInfo clientInfo, @NotNull String discordInvite, @NotNull IStartCommand normal, @NotNull IStartCommand optimized) {
        this.natives = natives;
        this.java = java;
        this.rise = rise;
        this.clientInfo = clientInfo;
        this.discordInvite = discordInvite;
        this.normal = normal;
        this.optimized = optimized;
    }

    public static RiseInfo getInstance()
    {
        return instance;
    }

    public static void createNew(JSONObject n, JSONObject j, JSONObject r, ClientInfo c, String d, IStartCommand normal, IStartCommand o)
    {
        instance = new RiseInfo(n, j, r, c, d, normal, o);
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

    public String getDiscordInvite() {
        return discordInvite;
    }

    public IStartCommand getNormalStartupCommand() {
        return normal;
    }

    public IStartCommand getOptimizedStartupCommand() {
        return optimized;
    }
}
