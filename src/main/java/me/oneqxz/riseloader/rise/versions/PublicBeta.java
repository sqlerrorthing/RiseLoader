package me.oneqxz.riseloader.rise.versions;

public class PublicBeta implements IVersion {

    private String version;
    private long lastUpdated;

    public PublicBeta(String version, long lastUpdated)
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
