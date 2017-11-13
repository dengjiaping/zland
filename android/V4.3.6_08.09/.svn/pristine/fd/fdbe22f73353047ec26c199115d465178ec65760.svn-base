package com.zhisland.android.blog.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    private static final String YEAR = "yyyy";
    private static final String TIME_FORMAT = "yyyy'年'M'月'd'日' HH':'mm";
    private static final String TIME_FORMAT_YYYY_M_D = "yyyy'年'M'月'd'日'";
    private static final String TIME_FORMAT_M_D = "M'月'd'日'";
    private static final String TIME_FORMAT_LACK_YEAR = "M'月'd'日' HH':'mm";
    private static final String TIME_FORMAT_LACK_YEAR_HOURS = "MM'月'dd'日'";
    private static final String TIME_FORMAT_HOURS_MIN = "HH':'mm";

    private static final Locale LOCALE = Locale.CHINA;

    /**
     * 时间规则： 当年： MM月dd HH：mm 其他：YYYY年 MM月dd HH：mm
     */
    public static String getTimeStr(long time) {
        time = time * 1000;
        SimpleDateFormat df = null;
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == getYear(time)) {
            df = new SimpleDateFormat(TIME_FORMAT_LACK_YEAR,
                    Locale.getDefault());
        } else {
            df = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        }
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    private static int getYear(long date) {
        SimpleDateFormat df = new SimpleDateFormat(YEAR, Locale.getDefault());
        String timeResult = df.format(date);
        return Integer.parseInt(timeResult);
    }

    public static String long2String(long time) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT,
                Locale.getDefault());
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    public static long string2Long(String source) {
        SimpleDateFormat f = new SimpleDateFormat(TIME_FORMAT, LOCALE);
        long result = -1;
        try {
            Date d = f.parse(source);
            result = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result / 1000;
    }

    /**
     * 是否和上次时间超过24小时
     */
    public static boolean isBeyond24H(long time) {
        return (System.currentTimeMillis() - time) > 24 * 60 * 60 * 1000;
    }

    public static boolean isSameDay(long day1, long day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getInfoDate(String date) {
        String endStr = " · 精选";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
        try {
            Date infoDate = f.parse(date);
            if (isYesterday(infoDate)) {
                return "昨日" + endStr;
            } else {
                Date nowDate = new Date();
                String format = f.format(nowDate);
                if (format.substring(0, 4).equals(date.substring(0, 4))) {
                    SimpleDateFormat sf = new SimpleDateFormat(
                            TIME_FORMAT_LACK_YEAR_HOURS, LOCALE);
                    String format2 = sf.format(infoDate);
                    return format2 + endStr;
                } else {
                    return date + endStr;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSceneTime(long date) {
        Date longToDate = new Date(date);
        if (longToDate == null) {
            return "";
        }
        boolean today = TimeUtil.isToday(longToDate);
        if (today) {
            SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT_HOURS_MIN,
                    LOCALE);
            return sf.format(longToDate);
        } else {
            SimpleDateFormat sf = new SimpleDateFormat(
                    TIME_FORMAT_LACK_YEAR_HOURS, LOCALE);
            return sf.format(longToDate);
        }
    }

    public static boolean isToday(Date a) {
        if (a == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
        return format.format(today).equals(format.format(a));
    }

    public static boolean isToday(long time) {
        Date a = new Date();
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
        return format.format(today).equals(format.format(a));
    }

    public static boolean isYesterday(Date a) {
        if (a == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
        return format.format(today).equals(format.format(a));
    }

    /**
     * 当天 HH:mm 其它 dd/mm/YY
     */
    public static String getTimeString(long time) {
        SimpleDateFormat format;
        if (isToday(time)) {
            format = new SimpleDateFormat(TIME_FORMAT_HOURS_MIN, LOCALE);
        } else {
            format = new SimpleDateFormat("dd'/'MM'/'yy", LOCALE);
        }
        return format.format(time);
    }

    /**
     * 根据活动详情的活动时间规则，取得时间String
     * 活动时间显示规则:
     * 当天活动显示日期+具体时间 2月28日13：30 - 18：30
     * 跨天显示日期+具体时间 2月28日13：30 - 3月2日 18：30
     * 非当年活动显示年份、日期、具体时间
     * 2015年2月28日13：30 - 18：30（同天）
     * 2015年2月28日13：30 - 2015年3月2日18：30（不同天）
     * 跨年活动显示年份、日期、具体时间 2015年2月28日13：30 - 2016年3月2日18：30
     */
    public static String getTimeContentForEvent(long eventStart, long eventEnd, int timeZoneInt) {
        TimeZone timeZone = getTimeZone(timeZoneInt);
        long currentTime = System.currentTimeMillis() / 1000l;
        String content;
        if (isSameYear(eventStart, eventEnd, timeZone)) {
            // 开始时间和结束时间为同一年
            if (isSameYear(eventStart, currentTime, timeZone)) {
                // 开始时间和结束时间为同一年，并且为今年
                if (isSameDay(eventStart, eventEnd, timeZone)) {
                    // 开始时间和结束时间为同一年，并且为今年,并且为同一天 显示为 MM月dd日 HH:mm - HH:mm
                    content = TimeUtil.formatTimeMDHHMM(eventStart, timeZone) + "—"
                            + TimeUtil.formatTimeHHMM(eventEnd, timeZone);
                } else {
                    // 开始时间和结束时间为同一年，并且为今年,并且不是同一天 显示为 MM月dd日 HH:mm -
                    // MM月dd日HH:mm
                    content = TimeUtil.formatTimeMDHHMM(eventStart, timeZone) + "—"
                            + TimeUtil.formatTimeMDHHMM(eventEnd, timeZone);
                }
            } else {
                // 开始时间和结束时间为同一年，并且不是今年
                if (isSameDay(eventStart, eventEnd, timeZone)) {
                    // 开始时间和结束时间为同一年，并且不是今年,并且为同一天 显示为 yyyy年M月dd日 HH:mm - HH:mm
                    content = TimeUtil.formatTimeYYYYMDHHMM(eventStart, timeZone) + "—"
                            + TimeUtil.formatTimeHHMM(eventEnd, timeZone);
                } else {
                    // 开始时间和结束时间为同一年，并且不是今年,并且不是同一天 显示为yyyy年M月dd日 HH:mm - yyyy年MM月dd日 HH:mm
                    content = TimeUtil.formatTimeYYYYMDHHMM(eventStart, timeZone) + "—"
                            + TimeUtil.formatTimeYYYYMDHHMM(eventEnd, timeZone);
                }
            }
        } else {
            // 开始时间和结束时间不是同一年,显示为yyyy年M月dd日 HH:mm - yyyy年MM月dd日 HH:mm
            content = TimeUtil.formatTimeYYYYMDHHMM(eventStart, timeZone) + "-"
                    + TimeUtil.formatTimeYYYYMDHHMM(eventEnd, timeZone);
        }
        return content;
    }

    /**
     * 根据活动列表的活动时间规则，取得时间String
     * 时间规则：  1.当天活动显示日期+具体时间：2月28日13：30 - 18：30
     * 2.跨天不显示具体时间：2月28日 - 3月2日
     * 3.非当年活动显示年份
     * a.2015年2月28日13：30 - 18：30（同天）
     * b.2015年2月28日 - 2015年3月2日（不同天）
     * 4.跨年活动显示年份，不显示具体时间：2015年2月28 - 2016年1月2日
     */
    public static String getTimeContentForEventList(long eventStart, long eventEnd, int timeZoneInt) {
        TimeZone timeZone = getTimeZone(timeZoneInt);
        long currentTime = System.currentTimeMillis() / 1000l;
        String content;
        if (isSameYear(eventStart, eventEnd, timeZone)) {
            // 开始时间和结束时间为同一年
            if (isSameYear(eventStart, currentTime, timeZone)) {
                // 开始时间和结束时间为同一年，并且为今年
                if (isSameDay(eventStart, eventEnd, timeZone)) {
                    // 开始时间和结束时间为同一年，并且为今年,并且为同一天 显示为 MM月dd日 HH:mm - HH:mm
                    content = TimeUtil.formatTimeMDHHMM(eventStart, timeZone) + " — "
                            + TimeUtil.formatTimeHHMM(eventEnd, timeZone);
                } else {
                    // 开始时间和结束时间为同一年，并且为今年,并且不是同一天 显示为 MM月dd日 - MM月dd日
                    content = TimeUtil.formatTimeMD(eventStart, timeZone) + " — "
                            + TimeUtil.formatTimeMD(eventEnd, timeZone);
                }
            } else {
                // 开始时间和结束时间为同一年，并且不是今年
                if (isSameDay(eventStart, eventEnd, timeZone)) {
                    // 开始时间和结束时间为同一年，并且不是今年,并且为同一天 显示为 yyyy年M月dd日 HH:mm - HH:mm
                    content = TimeUtil.formatTimeYYYYMDHHMM(eventStart, timeZone) + " — "
                            + TimeUtil.formatTimeHHMM(eventEnd, timeZone);
                } else {
                    // 开始时间和结束时间为同一年，并且不是今年,并且不是同一天 显示为yyyy年M月dd日 - yyyy年M月dd日
                    content = TimeUtil.formatTimeYYYYMD(eventStart, timeZone) + " — "
                            + TimeUtil.formatTimeYYYYMD(eventEnd, timeZone);
                }
            }
        } else {
            // 开始时间和结束时间不是同一年 yyyy年M月dd日 - yyyy年M月dd日
            content = TimeUtil.formatTimeYYYYMD(eventStart, timeZone) + " — "
                    + TimeUtil.formatTimeYYYYMD(eventEnd, timeZone);
        }
        return content;
    }

    /**
     * 根据时间和时区，返回M月d日 HH:mm格式的日期
     */
    public static String formatTimeMDHHMM(long time, TimeZone timeZone) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_LACK_YEAR);
        df.setTimeZone(timeZone);
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    /**
     * 根据时间和时区，返回HH:mm格式的日期
     */
    public static String formatTimeHHMM(long time, TimeZone timeZone) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_HOURS_MIN);
        df.setTimeZone(timeZone);
        String timeResult = df.format(new Date(time));
        return timeResult;
    }


    /**
     * 根据时间和时区，返回MD格式的日期
     */
    public static String formatTimeMD(long time, TimeZone timeZone) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_M_D);
        df.setTimeZone(timeZone);
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    /**
     * 根据时间和时区，返回yyyy年M月d日 HH:mm格式的日期
     */
    public static String formatTimeYYYYMDHHMM(long time, TimeZone timeZone) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
        df.setTimeZone(timeZone);
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    /**
     * 根据时间和时区，返回yyyy年M月dd日格式的日期
     */
    public static String formatTimeYYYYMD(long time, TimeZone timeZone) {
        time = time * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_YYYY_M_D);
        df.setTimeZone(timeZone);
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    /**
     * 判断在指定时区下，两个时间是否为同一年。
     */
    public static boolean isSameYear(long time1, long time2, TimeZone timeZone) {
        time1 = time1 * 1000;
        time2 = time2 * 1000;
        SimpleDateFormat df = new SimpleDateFormat(YEAR);
        df.setTimeZone(timeZone);
        return df.format(new Date(time1)).equals(df.format(new Date(time2)));
    }

    /**
     * 判断在指定时区下，两个时间是否为同一天。
     */
    public static boolean isSameDay(long time1, long time2, TimeZone timeZone) {
        time1 = time1 * 1000;
        time2 = time2 * 1000;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_YYYY_M_D);
        df.setTimeZone(timeZone);
        return df.format(new Date(time1)).equals(df.format(new Date(time2)));
    }

    /**
     * 根据timeZoneInt 获取时区。
     */
    public static TimeZone getTimeZone(int timeZoneInt) {
        String signStr = timeZoneInt >= 0 ? "+" : "-";
        return TimeZone.getTimeZone("GMT" + signStr + Math.abs(timeZoneInt));
    }

    /**
     * 是否和上次时间超过半个小时
     */
    public static boolean isBeyondHalfHour(long time) {
        return (System.currentTimeMillis() - time) > 30 * 60 * 1000;
    }

}
