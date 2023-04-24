package com.merkle.wechat.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A time util class to handle time.
 * 
 * @author tyao
 */
public class TimeUtil {
    public static Date currentTime() {
        return new Date();
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Date getTodayStart() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getTodayEnd() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static String getYesterdayYYYYMMDD() {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        result = simpleDateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));

        return result;
    }

    public static String getTodayYYYYMMDD() {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        result = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        return result;
    }

    /**
     * Format the time.
     * 
     * @param ticks
     *            Time.
     * @return Formatted time as string.
     */
    public static String formatYYYYMMDD(long ticks) {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        result = simpleDateFormat.format(new Date(ticks));

        return result;
    }

    public static String formatYYYYMMDDHHMMSS(long ticks) {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        result = simpleDateFormat.format(new Date(ticks));

        return result;
    }

    public static String formatYYYYMMDDHH(long ticks) {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");

        result = simpleDateFormat.format(new Date(ticks));

        return result;
    }

    public static Date getStartOfDay(Date enableStartDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(enableStartDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getEndOfDay(Date enableStartDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(enableStartDate);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
}
