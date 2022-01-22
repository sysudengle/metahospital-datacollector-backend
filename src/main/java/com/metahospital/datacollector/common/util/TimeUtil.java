package com.metahospital.datacollector.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class TimeUtil {
    public static final long ONE_SECOND_MILLS = 1000L;

    private TimeUtil() {
    }

    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    public static long toDefaultEpochMilli(LocalDateTime time) {
        if (time == null) {
            return 0;
        } else {
            return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    public static int toDefaultEpochSecond(LocalDateTime time) {
        if (time == null) {
            return 0;
        } else {
            return (int) (time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);
        }
    }

    public static int dateToSecond(Date time) {
        if (time == null) {
            return 0;
        }
        return (int) (time.getTime() / ONE_SECOND_MILLS);
    }

    public static Date toDefaultDate(LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            return Date.from(toDefaultInstant(time));
        }
    }

    private static Instant toDefaultInstant(LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            return time.atZone(ZoneId.systemDefault()).toInstant();
        }
    }

    public static LocalDateTime toDefaultLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public static LocalDateTime toDefaultLocalDateTimeOfSeconds(int timeSeconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeSeconds * ONE_SECOND_MILLS), ZoneId.systemDefault());
    }

    public static LocalDateTime toDefaultLocalDateTime(Date time) {
        if (time == null) {
            return null;
        } else {
            return toDefaultLocalDateTime(time.getTime());
        }
    }
    
    public static LocalDate toDefaultLocalDate(Date date) {
        Objects.requireNonNull(date);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static Date toDefaultDate(LocalDate localDate) {
        Objects.requireNonNull(localDate);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
}
