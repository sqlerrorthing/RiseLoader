package me.oneqxz.riseloader.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Version {

    private final String version;

    public Version(String version) {
        this.version = version;
    }

    public boolean needToUpdate(String newVersion)
    {
        String[] currentComponents = version.split("\\.");
        String[] newComponents = newVersion.split("\\.");

        // Проверяем, совпадают ли первые три компоненты
        if (currentComponents.length < 3 || newComponents.length < 3) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            int current = Integer.parseInt(currentComponents[i]);
            int newVer = Integer.parseInt(newComponents[i]);

            if (newVer > current) {
                return true;
            } else if (newVer < current) {
                return false;
            }
        }

        return false;
    }

    public String getVersion() {
        return version;
    }
}
