package me.oneqxz.riseloader.rise.pub.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IPublic {

    @NotNull IPublicData[] getData();
    IPublic updateData();

}
