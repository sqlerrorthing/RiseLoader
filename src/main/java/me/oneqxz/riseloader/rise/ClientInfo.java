package me.oneqxz.riseloader.rise;

import me.oneqxz.riseloader.rise.versions.IVersion;
import me.oneqxz.riseloader.rise.versions.PublicBeta;
import me.oneqxz.riseloader.rise.versions.Release;

public class ClientInfo {

    private String changelog;
    private String loaderVersion;
    private IVersion publicBeta, release;

    public ClientInfo(PublicBeta publicBeta, Release release, String changelog, String loaderVersion)
    {
        this.publicBeta = publicBeta;
        this.release = release;
        this.changelog = changelog;
        this.loaderVersion = loaderVersion;
    }

    public IVersion getPublicBeta() {
        return publicBeta;
    }

    public IVersion getRelease() {
        return release;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }
}
