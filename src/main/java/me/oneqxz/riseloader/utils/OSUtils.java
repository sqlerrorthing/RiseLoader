package me.oneqxz.riseloader.utils;

import java.nio.file.Path;

public class OSUtils {

    public static OS getOS()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
            return OS.WINDOWS;
        else if(os.contains("mac"))
            return OS.MACOS;
        else if(os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0)
            return OS.LINUX;
        else
            return OS.UNDEFINED;
    }

    public static Path getRiseFolder()
    {
        return Path.of(System.getProperty("user.home"), ".rise");
    }

    public static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index);
        }
        return "";
    }

    public enum OS {
        WINDOWS,
        LINUX,
        MACOS,
        UNDEFINED
    }
}
