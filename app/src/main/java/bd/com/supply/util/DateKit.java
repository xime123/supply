package bd.com.supply.util;

import android.text.TextUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by 徐敏 on 2016/1/18.
 * 邮箱：labixiaoxin@isoffice.cn
 */
public class DateKit {


    /**
     * 日期格式
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 时间格式
     */
    private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    /**
     * 时间格式
     */
    private final static ThreadLocal<SimpleDateFormat> timeMinFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    /**
     * 获取当前时间:Date
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 获取当前时间:Calendar
     */
    public static Calendar getCal() {
        return Calendar.getInstance();
    }

    /**
     * 日期转换为字符串:yyyy-MM-dd
     */
    public static String dateToStr(Date date) {
        if (date != null)
            return dateFormat.get().format(date);
        return null;
    }


    /**
     * 日期转换为字符串:yyyy-MM-dd
     */
    public static String dateToStr(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (date != null) {
                Date dd = sdf.parse(date);
                return dateFormat.get().format(dd);
            }
        } catch (ParseException e) {

            e.printStackTrace();

        }
        return "";
    }

    /**
     * 日期转换为字符串:yyyy年MM月dd
     */
    public static String dateToStr2(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null) {
                Date dd = sdf.parse(date);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年M月d日");
                return sdf2.format(dd);
            }
        } catch (ParseException e) {

            e.printStackTrace();

        }
        return null;
    }

    public static String dateToStr3(String date) {
        if (TextUtils.isEmpty(date)) {
            return "没有日期";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null) {
                Date dd = sdf.parse(date);

                return sdf.format(dd);
            }
        } catch (ParseException e) {

            e.printStackTrace();

        }
        return null;
    }

    /**
     * 时间转换为字符串:yyyy-MM-dd HH:mm:ss
     */
    public static String timeToStr(Date date) {
        if (date != null)
            return timeFormat.get().format(date);
        return null;
    }

    /**
     * 字符串转换为日期:yyyy-MM-dd
     */
    public static Date strToDate(String str) {
        Date date = null;
        try {
            date = dateFormat.get().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换为时间:yyyy-MM-dd HH:mm:ss
     */
    public static Date strToTime(String str) {
        Date date = null;
        try {
            date = timeFormat.get().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String transformDate(String str) {
        Date date = strToDate(str);
        if (date == null)
            return ":)";
        String time = new SimpleDateFormat("MM月dd日").format(date);
        return time;
    }

    /**
     * 友好的方式显示时间:
     * 一分钟内：刚刚；
     * <p>
     * 一分钟后一小时内：f 分钟前；
     * <p>
     * 一小时后一天内：h 小时前；
     * <p>
     * 一天后一个月内：d 天前；
     * <p>
     * 一个月后：12-27 22:33；
     * <p>
     * 不是今年：2014-12-03 22:33；
     */
    public static String friendlyFormat(String str) {
        Date date = strToTime(str);
        if (date == null)
            return ":)";
        Calendar now = getCal();
        String time = new SimpleDateFormat("HH:mm").format(date);

        // 第一种情况，日期在同一天
        String curDate = dateFormat.get().format(now.getTime());//当前时间
        String paramDate = dateFormat.get().format(date);//传进来的时间
        if (curDate.equals(paramDate)) {//同一天的
            int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
            if (hour > 0)
                return hour + "小时前";
            int minute = (int) ((now.getTimeInMillis() - date.getTime()) / 60000);
            if (minute < 1)
                return "刚刚";
            return minute + "分钟前";
        }

        // 第二种情况，不在同一天
        int days = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000);
        int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
        if (hour > 0 && hour < 24)
            return hour + "小时前";
        if (days < 30 && days > 0)
            return days + "天前";
        if (days >= 30 && days < 365)
            return dateToStr(date).substring(5);
        return dateToStr(date);
    }

    /**
     * 返回日期的0点:2012-07-07 20:20:20 --> 2012-07-07 00:00:00
     */
    public static Date getBegin(Date date) {
        return strToTime(dateToStr(date) + " 00:00:00");
    }

    public static void main(String[] args) {
        System.err.println(friendlyFormat("2013-09-16 11:27:19"));
    }

    public static final long DAY = 86400000;


    public static String getHMTime(String date) {
        try {
            String str = date.toString().trim();
            return str.substring(11, 16);
        } catch (Exception e) {
            return date;
        }
    }

    public static boolean isRightDate(String dataStr) {
        try {
            Date nowdate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date d = sdf.parse(dataStr);

            boolean flag = d.before(nowdate);
            return flag;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isRightDate3(String dataStr) {
        if (TextUtils.isEmpty(dataStr)) return false;

        if (dataStr.length() < 12) {
            dataStr = dataStr + " 23:59:59";
        }
        try {
            Date nowdate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date d = sdf.parse(dataStr);

            boolean flag = d.before(nowdate);
            return flag;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isRightDate2(String dataStr) {
        try {
            Date nowdate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
            Date d = sdf.parse(dataStr);

            boolean flag = d.before(nowdate);
            return flag;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 毫秒
     * @return
     */
    public static String timeStampDate(long seconds) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

}
