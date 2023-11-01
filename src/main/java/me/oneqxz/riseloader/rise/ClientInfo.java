package me.oneqxz.riseloader.rise;

public class ClientInfo {

    private String clientVersion;
    private String changelog;
    private String loaderVersion;

    public ClientInfo(String clientVersion, String changelog, String loaderVersion)
    {
        this.clientVersion = clientVersion;
        this.changelog = changelog;
        this.loaderVersion = loaderVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }
}
