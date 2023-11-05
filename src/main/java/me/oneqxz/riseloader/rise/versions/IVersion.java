package me.oneqxz.riseloader.rise.versions;

import me.oneqxz.riseloader.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public interface IVersion {

    String getVersion();
    long lastUpdated();

    default String getUpdateAgo()
    {
        return TimeUtils.getUploadedAgoByGTM0(lastUpdated());
    }

}
