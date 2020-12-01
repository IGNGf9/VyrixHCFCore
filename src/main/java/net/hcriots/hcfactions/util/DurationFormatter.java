/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DurationFormatter {

    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1L);
    private static final long HOUR = TimeUnit.HOURS.toMillis(1L);

    public static String getRemaining(long millis, boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < MINUTE) {
            return (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001) + 's';
        } else {
            return DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss");
        }
    }

    public static String translateToDate(long date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");

        Date date1 = new Date(date);

        return dateFormat.format(date1);
    }
}
