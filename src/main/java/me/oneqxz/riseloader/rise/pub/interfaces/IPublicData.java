package me.oneqxz.riseloader.rise.pub.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IPublicData {

    @NotNull String getDescription();
    @NotNull IFileData getFileData();
    @NotNull String getName();

    @NotNull String getServers();
    @Nullable String getPreviewURL();
    @Nullable String getVideoURL();

}
