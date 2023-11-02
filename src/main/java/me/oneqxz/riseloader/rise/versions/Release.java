package me.oneqxz.riseloader.rise.versions;

public class Release implements IVersion {

    private String version;
    private long lastUpdated;

    public Release(String version, long lastUpdated)
    {
        this.version = version;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public long lastUpdated() {
        return lastUpdated;
    }

}
