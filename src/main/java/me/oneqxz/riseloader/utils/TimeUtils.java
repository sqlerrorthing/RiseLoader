package me.oneqxz.riseloader.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static String getUploadedAgoByGTM0(long uploadTime)
    {
        Instant currentTime = Instant.now();
        Instant pastTime = Instant.ofEpochMilli(uploadTime);

        long secondsAgo = ChronoUnit.SECONDS.between(pastTime, currentTime);
        if (secondsAgo < 60) {
            return secondsAgo + (secondsAgo == 1 ? " sec" : " secs");
        }

        long minutesAgo = ChronoUnit.MINUTES.between(pastTime, currentTime);
        if (minutesAgo < 60) {
            return minutesAgo + (minutesAgo == 1 ? " min" : " mins");
        }

        long hoursAgo = ChronoUnit.HOURS.between(pastTime, currentTime);
        if (hoursAgo < 24) {
            return hoursAgo + (hoursAgo == 1 ? " hour" : " hours");
        }

        long daysAgo = ChronoUnit.DAYS.between(pastTime, currentTime);
        if (daysAgo < 7) {
            return daysAgo + (daysAgo == 1 ? " day" : " days");
        }

        long weeksAgo = daysAgo / 7;
        if (weeksAgo < 4) {
            return weeksAgo + (weeksAgo == 1 ? " week" : " weeks");
        }

        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentTime, ZoneId.of("GMT"));
        LocalDateTime pastDateTime = LocalDateTime.ofInstant(pastTime, ZoneId.of("GMT"));
        long monthsAgo = ChronoUnit.MONTHS.between(pastDateTime, currentDateTime);
        if (monthsAgo < 12) {
            return monthsAgo + (monthsAgo == 1 ? " month" : " months");
        }

        long yearsAgo = monthsAgo / 12;
        return yearsAgo + (yearsAgo == 1 ? " year" : " years");
    }

}
