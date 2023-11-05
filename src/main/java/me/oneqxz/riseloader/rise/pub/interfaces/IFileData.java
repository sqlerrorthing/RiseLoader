package me.oneqxz.riseloader.rise.pub.interfaces;

import me.oneqxz.riseloader.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

public interface IFileData {

    @NotNull String getDownloadURL();
    @NotNull String getMD5();
    @NotNull long created();

    default String lastUploaded()
    {
        return TimeUtils.getUploadedAgoByGTM0(created());
    }

}
