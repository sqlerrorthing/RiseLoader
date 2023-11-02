package me.oneqxz.riseloader.rise.versions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public interface IVersion {

    String getVersion();
    long lastUpdated();

    default LocalDateTime convertUtcToLocalTime(LocalDateTime utcTime, ZoneId zoneId) {
        return utcTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime();
    }

    default String calculateTimeDelta(LocalDateTime localTime) {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime nowInUtc = now.atZone(ZoneId.of("UTC"));
        ZonedDateTime localTimeInUtc = localTime.atZone(ZoneId.of("UTC"));

        long deltaInSeconds = nowInUtc.toEpochSecond() - localTimeInUtc.toEpochSecond();

        String timeDelta = "";
        if (deltaInSeconds < 60) {
            timeDelta = deltaInSeconds + " sec";
        } else if (deltaInSeconds < 3600) {
            timeDelta = deltaInSeconds / 60 + " min";
        } else if (deltaInSeconds < 86400) {
            timeDelta = deltaInSeconds / 3600 + " hours";
        } else if (deltaInSeconds < 604800) {
            timeDelta = deltaInSeconds / 86400 + " days";
        } else if (deltaInSeconds < 2592000) {
            timeDelta = deltaInSeconds / 604800 + " weeks ";
        } else if (deltaInSeconds < 31536000) {
            timeDelta = deltaInSeconds / 2592000 + " months ";
        } else {
            timeDelta = deltaInSeconds / 31536000 + " years";
        }

        return timeDelta;
    }

    public static void main(String[] args) {
    }

    default String getUpdateAgo()
    {
        LocalDateTime utcTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastUpdated()), ZoneId.systemDefault());
        LocalDateTime localTime = convertUtcToLocalTime(utcTime, ZoneId.systemDefault());
        return calculateTimeDelta(localTime);
    }

}
