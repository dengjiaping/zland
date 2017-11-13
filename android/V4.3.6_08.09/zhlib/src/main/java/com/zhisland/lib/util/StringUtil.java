package com.zhisland.lib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;

import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.text.ZHLink.LinkStyleClickableSpan;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;

/**
 * Various string manipulation methods.
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    // datetime utilize
    // ----------------------------------------
    private static final String TIME_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSSSSS";
    private static final String TIME_FORMAT_TEXT = "yyyy'-'MM'-'dd' 'HH':'mm':'ss";
    private static final String SAVE_FORMAT_TEXT = "yyyy'/'MM'/'dd";
    private static final String SAVESEC_FORMAT_TEXT = "HH:mm";

    private static final String LONGDATE_FORMAT_TEXT = "yyyy'-'MM'-'dd";
    private static final String DATE_FORMAT_TEXT = "yy'-'MM'-'dd";
    private static final String DATE_FORMAT_HH_MM = "h:mm"; // 12

    public static final String SAFE_HOST = "www.zhisland.com";
    public static final String SAFE_SCHEME = "https";

    public static String getVideoTimeString(long time) {
        if (time <= 0)
            return "00:00";
        long second = time % 60;
        long minute = time / 60;
        minute = minute > 59 ? 59 : minute;
        return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
    }

    /**
     * convert local time to UTC string
     *
     * @param l
     * @return
     */
    public static String getUTCDateTimeFromLong(final Long l) {
        try {
            final TimeZone timeZone = TimeZone.getDefault();
            final long offset = timeZone.getOffset(l);
            Date d = new Date(l - offset);
            final SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT,
                    Locale.getDefault());
            final String sDate = df.format(d) + "Z";
            return sDate;
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getTimeString(final Long l) {
        try {
            Date d = new Date(l);
            final SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_TEXT,
                    Locale.getDefault());
            final String sDate = df.format(d);
            return sDate;
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getSaveDateTime(final Long l) {
        try {
            Date d = new Date(l);
            final SimpleDateFormat df = new SimpleDateFormat(SAVE_FORMAT_TEXT,
                    Locale.getDefault());
            final String sDate = df.format(d);
            return sDate;
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getSaveSecTime(final Long l) {
        try {
            Date d = new Date(l);
            final SimpleDateFormat df = new SimpleDateFormat(
                    SAVESEC_FORMAT_TEXT, Locale.getDefault());
            final String sDate = df.format(d);
            return sDate;
        } catch (final Exception e) {
            return null;
        }
    }

    private static final SimpleDateFormat dfSameDay = new SimpleDateFormat(
            "HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dfSameYear = new SimpleDateFormat(
            "MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dfLongAgo = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dfSameYearWithoutTime = new SimpleDateFormat(
            "MM-dd", Locale.getDefault());
    private static final SimpleDateFormat dfLongAgoWithoutTime = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());

    /**
     * 1, 当t<60s时显示为: 刚刚 2, 当t>=60s且t<60m时显示为: x分钟前 (x为整数,不足1分钟的以一分钟计,
     * 如1分20秒计为2分钟) 3, 当t>=60m且t<=2h时显示为: y小时前 (y为整数,超过1小时时显示为2) 4,
     * 当t>2h且发布时间为当天时显示为: hh:mm 5, 当发布时间为当年时显示为: xx月xx日 hh:mm 6, 其他时间显示为:
     * xxxx年xx月xx日
     */

    public static long convertFrom(String timeString) {
        if (StringUtil.isNullOrEmpty(timeString))
            return 0;
        try {
            final SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_TEXT,
                    Locale.getDefault());
            return df.parse(timeString).getTime();
        } catch (final Exception e) {
            return 0;
        }
    }

    public static String convertFrom(long time) {
        return convertFrom(new Date(time));
    }

    public static String convertFrom(final Date dDate) {

        if (dDate == null) {
            return null;
        }

        long lCur = System.currentTimeMillis();
        long lDiff = (lCur - dDate.getTime()) / 1000;
        if (lDiff < 60) {
            return "刚刚";
        } else if (lDiff < 3600) {
            return (lDiff + 59) / 60 + "分钟前";
        } else if (lDiff <= 7200) {
            return lDiff / 3600 + "小时前";
        } else {
            Date dCur = new Date(lCur);
            long dayCur = lCur / 1000 - dCur.getHours() * 3600
                    - dCur.getMinutes() * 60 - dCur.getSeconds();
            long dayDate = dDate.getTime() / 1000 - dDate.getHours() * 3600
                    - dDate.getMinutes() * 60 - dDate.getSeconds();
            if (dayCur == dayDate) {
                return "今天 " + dfSameDay.format(dDate);
            } else if (dayCur - dayDate == 60 * 60 * 24) {
                return "昨天 " + dfSameDay.format(dDate);
            } else if (dCur.getYear() == dDate.getYear()) {
                return dfSameYear.format(dDate);
            } else {
                return dfLongAgo.format(dDate);
            }
        }
    }

    public static String BuildDay(final Date dDate) {
        if (dDate == null) {
            return null;
        }
        long lCur = System.currentTimeMillis();
        Date dCur = new Date(lCur);
        long dayCur = lCur / 1000 - dCur.getHours() * 3600 - dCur.getMinutes()
                * 60 - dCur.getSeconds();
        long dayDate = dDate.getTime() / 1000 - dDate.getHours() * 3600
                - dDate.getMinutes() * 60 - dDate.getSeconds();
        if (dayCur == dayDate) {
            return "今天 ";
        } else if (dayCur - dayDate == 60 * 60 * 24) {
            return "昨天 ";
        } else if (dCur.getYear() == dDate.getYear()) {
            return dfSameYearWithoutTime.format(dDate);
        } else {
            return dfLongAgoWithoutTime.format(dDate);
        }
    }

    /**
     * 1.一天之内 显示为：上午(或下午) 小时：分钟 2.昨天显示为：昨天 3.一周内显示为：星期几 4.一周以前显示为：年月日 XX-XX-XX
     */
    public static String dateConvertFrom(long time) {
        return dateConvertFrom(new Date(time));
    }

    public static String longFormatdateConvertFrom(long time) {
        // return dateConvertFrom(new Date(time));

        SimpleDateFormat df = new SimpleDateFormat(LONGDATE_FORMAT_TEXT,
                Locale.getDefault());
        String timeResult = df.format(new Date(time));
        return timeResult;
    }

    public static String dateConvertFrom(final Date dDate) {

        if (dDate == null) {
            return null;
        }

        String timeResult = "";
        long lCur = System.currentTimeMillis();
        long lDiff = (lCur - dDate.getTime()) / 1000;

        // get current calendar
        Calendar calendar = GregorianCalendar.getInstance();
        int hoursOfDayNow = calendar.get(Calendar.HOUR_OF_DAY); // 24
        int minutesNow = calendar.get(Calendar.MINUTE);

        // set old date to calendar object
        calendar.setTime(dDate);
        int dayOfWeekOld = calendar.get(Calendar.DAY_OF_WEEK);
        int hoursOfDayOld = calendar.get(Calendar.HOUR_OF_DAY);

        if (lDiff < hoursOfDayNow * 3600 + minutesNow) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_HH_MM,
                    Locale.getDefault());
            String sDate = df.format(dDate);
            if (hoursOfDayOld < 12) {
                timeResult = "上午 " + sDate;
            } else {
                timeResult = "下午 " + sDate;
            }
        } else if (lDiff < hoursOfDayNow * 3600 + minutesNow + 24 * 3600) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_HH_MM,
                    Locale.getDefault());
            String sDate = df.format(dDate);
            timeResult = "昨天 " + sDate;
        } else if (lDiff < 7 * 24 * 3600) {
            switch (dayOfWeekOld) {
                case Calendar.SUNDAY: {
                    timeResult = "星期日";
                    break;
                }
                case Calendar.MONDAY: {
                    timeResult = "星期一 ";
                    break;
                }
                case Calendar.TUESDAY: {
                    timeResult = "星期二 ";
                    break;
                }
                case Calendar.WEDNESDAY: {
                    timeResult = "星期三 ";
                    break;
                }
                case Calendar.THURSDAY: {
                    timeResult = "星期四 ";
                    break;
                }
                case Calendar.FRIDAY: {
                    timeResult = "星期五 ";
                    break;
                }
                case Calendar.SATURDAY: {
                    timeResult = "星期六 ";
                    break;
                }
            }
        } else {
            try {
                SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_TEXT,
                        Locale.getDefault());
                timeResult = df.format(dDate);
            } catch (IllegalArgumentException e) {

            }

        }
        return timeResult;
    }

    public static String getSimpleDataTimeText(long lData) {
        return dfLongAgo.format(lData);
    }

    public static long localDateTimeToUTC(long currentDate) {
        final TimeZone timeZone = TimeZone.getDefault();
        final long offset = timeZone.getOffset(currentDate);
        return currentDate - offset;
    }

    public static String UrlByAppendQuery(String url, String query) {
        if (query == null || query.length() == 0) {
            return url;
        }

        String urlQuery = String.format("%s%s%s", url,
                url.indexOf('?') > 0 ? "&" : "?", query);

        return urlQuery;
    }

    /**
     * add HTTP schema
     */
    public static String UrlAppendSchema(String url) {
        if (StringUtil.isNullOrEmpty(url) || url.startsWith("http"))
            return url;

        return "http://" + url;
    }

    /**
     * 获取字符数，英文只占半个
     *
     * @param str
     * @return
     */
    public static int getLength(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return 0;
        }
        try {
            int utfLength = str.getBytes("UTF-8").length;
            String s = new String(str.getBytes(), "UTF-8");
            int length = s.length();
            int chNum = (utfLength - length) / 2;
            int lastLength = (length - chNum + 1) / 2 + chNum;
            return lastLength;
        } catch (UnsupportedEncodingException e1) {
        }
        return 0;
    }

    /**
     * 判断单个字符是否为中文
     */
    public static boolean isChiness(String c) {
        String chinese = "[\u4e00-\u9fa5]";
        return c.matches(chinese);
    }

    /**
     * 判断单个字符是否为a-z,A-Z,0-9
     */
    public static boolean isAssicChar(String c) {
        String assic = "[a-zA-Z0-9]";
        return c.matches(assic);
    }

    /**
     * 判断单个字符是否为a-z,A-Z,0-9和中国字
     */
    public static boolean isNormalChar(String c) {
        return isChiness(c) || isAssicChar(c);
    }

    /**
     * 获取字符数，英文占1个中文占2个
     *
     * @param str
     * @return
     */
    public static int getLength2(String str) {
        int valueLength = 0;
        try {
            String chinese = "[\u4e00-\u9fa5]";
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
            return valueLength;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从头截取str的count个字符，英文占1个中文占2个
     *
     * @param str
     * @return
     */
    public static String subString(String str, int count) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            int valueLength = 0;
            try {
                String chinese = "[\u4e00-\u9fa5]";
                for (int i = 0; i < str.length(); i++) {
                    String temp = str.substring(i, i + 1);
                    if (temp.matches(chinese)) {
                        valueLength += 2;
                    } else {
                        valueLength += 1;
                    }
                    if (valueLength > count) {
                        break;
                    } else {
                        sb.append(temp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 获取指定长度的字符，英文只占半个。超出的部分截掉
     *
     * @param str
     * @param limit
     * @return
     */
    public static String getStrDigitsLimit(String str, int limit) {
        while (StringUtil.getLength(str) > limit) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 判断字符是否多于几行或者大于置顶长度
     *
     * @param rowCount
     * @param length
     * @return
     */
    public static boolean largeThan(String text, int rowCount, int length) {
        if (StringUtil.isNullOrEmpty(text))
            return false;
        int len = getLength(text);

        int count = 0;
        int index = 0;
        while ((index = text.indexOf("\n", index)) != -1) {
            count++;
            index += text.length();
        }
        int row = count + 1;
        return len > length || row > rowCount;
    }

    public static String validUrl(String url) {
        if (StringUtil.isNullOrEmpty(url))
            return null;

        if (!url.startsWith("http"))
            return url;

        try {
            URI myUri = new URI(url);
            if (myUri.getHost() == null) {
                return null;
            }
            if (myUri.getHost().equals(SAFE_HOST)
                    && myUri.getScheme().equals(SAFE_SCHEME)) {
                String desUrl = "http://www.zhisland.com";
                int startIndex = SAFE_HOST.length() + SAFE_SCHEME.length() + 3; // 3
                // is
                // for
                // "://"
                if (startIndex < url.length()) {
                    desUrl = desUrl + url.substring(startIndex);
                    desUrl = desUrl.replaceFirst(":443", "");
                }

                return desUrl;
            }
            ;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return url;

    }

    private static String insertSubStrBeforeExt(String url, String subStr) {
        if (url != null && url.length() > 4) {
            int lastDotIdx = url.lastIndexOf('.');
            if (lastDotIdx > 0) {
                StringBuilder strBuilder = new StringBuilder(url);
                strBuilder.insert(lastDotIdx, subStr);
                return strBuilder.toString();
            }
        }

        return null;
    }

    public static String getAvatar_M_Url(String avatarUrl) {
        return insertSubStrBeforeExt(avatarUrl, "_m");
    }

    public static String getAvatar_L_Url(String avatarUrl) {
        return insertSubStrBeforeExt(avatarUrl, "_l");
    }

    public static boolean isEquals(String s1, String s2) {
        if (StringUtil.isNullOrEmpty(s1)) {
            if (StringUtil.isNullOrEmpty(s2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return s1.equals(s2);
        }
    }

    /**
     * 将一个数组中的元素根据制定分隔符拼接成一个字符串
     *
     * @param arr
     * @param split
     * @return
     */
    public static String convertFromArr(Collection<String> arr, String split) {
        if (arr == null || StringUtil.isNullOrEmpty(split)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            if (!StringUtil.isNullOrEmpty(str)) {
                sb.append(str + split);
            }
        }
        return sb.toString();
    }

    public static String convertFromList(List<?> arr, String split) {
        if (arr == null || arr.size() < 1 || StringUtil.isNullOrEmpty(split)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Object str : arr) {
            sb.append(str + split);
        }
        return sb.subSequence(0, sb.length() - split.length()).toString();
    }

    public static String convertFromArr(String[] arr, String split) {
        if (arr == null || StringUtil.isNullOrEmpty(split)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            if (!StringUtil.isNullOrEmpty(str)) {
                sb.append(str + split);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串分解成字符数组
     *
     * @param str
     * @return
     */
    public static ArrayList<String> split(String str) {
        if (StringUtil.isNullOrEmpty(str))
            return null;
        String[] ss = str.split(",");

        ArrayList<String> arr = new ArrayList<String>();
        for (String s : ss) {
            arr.add(s);
        }
        return arr;
    }

    public static SpannableString getSeparatorSpannableString(Context context,
                                                              String text, int linkColor, String separator,
                                                              OnLinkClickListener lisener) {
        return getSeparatorSpannableString(context, true, false, text,
                linkColor, separator, lisener);
    }

    /**
     * @param isSpanFirst   第一个是否需要spanable,
     * @param isSpanSmaller SpannableString是否需要变小字体
     * @param linkColor     SpannableString 字体颜色
     * @param separator     ：分隔符
     * @param lisener       SpannableString的点击回调
     */
    public static SpannableString getSeparatorSpannableString(Context context,
                                                              boolean isSpanFirst, boolean isSpanSmaller, String text,
                                                              int linkColor, String separator, OnLinkClickListener lisener) {
        if (StringUtil.isNullOrEmpty(text)) {
            return null;
        }
        SpannableString textStyle = new SpannableString(text);

        int sIndex = 0;
        int eIndex = 0;
        while (sIndex <= text.length()
                && (eIndex = text.indexOf(separator, sIndex)) > 0) {
            String childStr = text.substring(sIndex, eIndex);
            // 用于name+phone, 不对name进行span
            if (sIndex == 0 && !isSpanFirst) {
                sIndex = eIndex + separator.length();
                continue;
            }
            LinkStyleClickableSpan span = new LinkStyleClickableSpan(context,
                    null, childStr, linkColor, lisener);
            textStyle.setSpan(span, sIndex, eIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isSpanSmaller) {
                textStyle.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(13)),
                        sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            sIndex = eIndex + separator.length();
        }

        // 只有一个时
        if (sIndex == 0) {
            if (isSpanFirst) {
                LinkStyleClickableSpan span = new LinkStyleClickableSpan(
                        context, null, text, linkColor, lisener);
                textStyle.setSpan(span, 0, text.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (eIndex == -1) {
            String childStr = text.substring(sIndex, text.length());
            LinkStyleClickableSpan span = new LinkStyleClickableSpan(context,
                    null, childStr, linkColor, lisener);
            textStyle.setSpan(span, sIndex, text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isSpanSmaller) {
                textStyle
                        .setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(13)),
                                sIndex, text.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return textStyle;
    }

    /**
     * 获取asset中的文件字符串
     *
     * @param asset
     * @param fileName
     * @return
     */
    public static String loadAssetFile(String fileName) {

        AssetManager asset = ZHApplication.APP_CONTEXT.getAssets();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(asset.open(fileName)));
            String word;
            while ((word = br.readLine()) != null)
                sb.append(word);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // stop reading
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();

    }

    /**
     * 判断是否为合法手机号
     *
     * @param phone
     * @return
     */
    public static boolean phoneFormatCheck(String phone) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 判断电话是否为手机号码。（前提是传入的phone为电话）
     * 以下情况为座机，其余为手机。
     * 长度为12位并且以0开头。
     * 长度为11位并且以0开头
     * 长度小于11位
     */
    public static boolean isMobileNumber(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        } else {
            if (phone.length() < 11) {
                return false;
            } else if (phone.length() <= 12 && phone.startsWith("0")) {
                return false;
            }
            return true;
        }
    }

    /**
     * 判断邮箱是否合法
     *
     * @param mail
     * @return
     */
    public static boolean isEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    /**
     * 复制文本到剪贴板
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text);
    }

    public static void logLongString(String text, String logTag, int logType) {
        int start = 0;
        int size = 500;
        int end = 0;
        while (end < text.length()) {
            end = start + size;
            if (end > text.length()) {
                end = text.length();
            }
            String tmp = text.substring(start, end);
            switch (logType) {
                case Log.DEBUG:
                    Log.d(logTag, tmp);
                    break;
                case Log.ERROR:
                    Log.e(logTag, tmp);
                    break;
                case Log.INFO:
                    Log.i(logTag, tmp);
                    break;
                case Log.WARN:
                    Log.w(logTag, tmp);
                    break;
                default:
                    Log.i(logTag, tmp);
                    break;
            }
            start += size;
            logTag = "";
        }
    }

    /**
     * 获取剪切板文字
     * */
    public static String getClipText(Context context) {
        android.content.ClipboardManager myClipboard = (android.content.ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        if (myClipboard.hasPrimaryClip()) {
            if (myClipboard.getPrimaryClipDescription().hasMimeType(
                    ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                return myClipboard.getPrimaryClip().getItemAt(0).getText().toString();
            }
        }
        return null;
    }

}
