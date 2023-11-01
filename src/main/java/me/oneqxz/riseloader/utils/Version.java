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
        List<Integer> currentVersionList = getVersionList(version);
        List<Integer> latestVersionList = getVersionList(newVersion);

        int result = compareVersion(currentVersionList, latestVersionList);

        if (result < 0) {
            return false;
        } else return result != 0;
    }

    private List<Integer> getVersionList(String version) {
        return Arrays.stream(version.split("\\."))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private int compareVersion(List<Integer> currentVersion, List<Integer> latestVersion) {
        int size = Math.min(currentVersion.size(), latestVersion.size());
        for (int i = 0; i < size; i++) {
            int cmp = currentVersion.get(i).compareTo(latestVersion.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(currentVersion.size(), latestVersion.size());
    }

    public String getVersion() {
        return version;
    }
}
