package com.rsrit.rcrm.util;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private static final long TICKS_AT_EPOCH = 621355968000000000L;
    private static final long TICKS_PER_MILLISECOND = 10000;

    public static long getUTCTicks(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (calendar.getTimeInMillis() * TICKS_PER_MILLISECOND) + TICKS_AT_EPOCH;

    }

    public static Date getDate(long UTCTicks) {

        return new Date((UTCTicks - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);

    }

}