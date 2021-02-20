/**
 *
 */
package cn.dofuntech.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.WEEK_OF_MONTH;
import static java.util.Calendar.YEAR;

/**
 * 日期工具类，
 * <p>
 * 提供了对{@link java.util.Date}和{@link java.util.Calendar}的操作。
 *
 * @author lxu
 */
public final class DateUtils {

    /**
     * 日期格式mm:ss字符串常量
     */
    public static final String MINUTE_FORMAT = "HH:mm:ss";

    public static final String DATE_FORMAT2 = "MM-dd HH:mm:ss";

    /**
     * 日期格式yyyy-MM-dd字符串常量
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期格式yyyy-MM-dd HH:mm:ss字符串常量
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式yyyyMMddHHmmss字符串常量
     */
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";

    /**
     * 日期格式 yyyy-MM-dd 转换类
     */
    public static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss 转换类
     */
    public static final DateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT);

    /**
     * 一个月时间大约的long型数字
     */
    public static final long MONTH_LONG = 2651224907l;

    private DateUtils() {
    }

    /**
     * 将Calendar类型的日期格式转换成字符串型
     * <p>
     * 默认格式是 "yyyy-MM-dd"
     *
     * @param canlendar
     * @return
     */
    public static String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }

        return format(calendar.getTime());
    }

    /**
     * 将Calendar类型的日期格式转换成字符串型
     *
     * @param calendar
     * @param format   日期格式字符串
     * @return
     */
    public static String format(Calendar calendar, String format) {
        if (calendar == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            return format(calendar);
        }

        return format(calendar.getTime());
    }

    /**
     * 将Date类型的日期格式转换成字符串型
     * <p>
     * 默认格式是 "yyyy-MM-dd"
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    /**
     * 将Date类型的日期格式转换成字符串型
     *
     * @param date
     * @param format 日期格式字符串
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            return format(date);
        }

        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将Object类转换成制定格式的字符串
     * <p>
     * 默认格式为"yyyy-MM-dd"
     *
     * @param obj
     * @return
     */
    public static Date parse(Object obj) {
        if (obj == null) {
            return null;
        }

        return parse(obj.toString());
    }

    /**
     * 将Object类转换成制定格式的字符串
     *
     * @param obj
     * @param format
     * @return
     */
    public static Date parse(Object obj, String format) {
        if (obj == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            return parse(obj);
        }

        return parse(obj.toString(), format);
    }

    /**
     * 将Object类转换成制定格式的字符串 默认格式为"yyyy-MM-dd"
     *
     * @param strDate
     * @return
     */
    public static Date parse(String strDate) {
        if (strDate == null || strDate.length() == 0) {
            return null;
        }

        try {
            return dateFormat.parse(strDate.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将Object类转换成制定格式的字符串
     *
     * @param obj
     * @param format
     * @return
     */
    public static Date parse(String strDate, String format) {
        if (strDate == null || strDate.length() == 0) {
            return null;
        }

        try {
            return new SimpleDateFormat(format).parse(strDate.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断是否是闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            return true;
        }
        return false;
    }

    /**
     * 获得当月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得当月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDateOfMonth(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.add(MONTH, 1);
        calendar.set(DAY_OF_MONTH, 1);
        calendar.add(DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获得上月最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDateOfProMonth(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(DAY_OF_MONTH, 1);
        calendar.add(DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获得上月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfProMonth(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(DAY_OF_MONTH, 1);
        calendar.add(DAY_OF_MONTH, -1);
        calendar.set(DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得查询日期的前一天
     *
     * @param date
     * @return
     */
    public static Date getPreviousDate(Date date) {
        return addDay(date, -1);
    }

    /**
     * 获得查询日期的后一天
     *
     * @param date
     * @return
     */
    public static Date getNextDate(Date date) {
        return addDay(date, 1);
    }

    /**
     * 将日期加上相应的天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        Calendar calendar = getCalendar(date);
        calendar.add(DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 将日期加上相应的周数
     *
     * @param date
     * @param weeks
     * @return
     */
    public static Date addWeek(Date date, int weeks) {
        Calendar calendar = getCalendar(date);
        calendar.add(WEEK_OF_MONTH, weeks);
        return calendar.getTime();
    }

    /**
     * 将日期加上相应的月数
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonth(Date date, int months) {
        Calendar calendar = getCalendar(date);
        calendar.add(MONTH, months);
        return calendar.getTime();
    }

    /**
     * 将日期加上相应的年数
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addYear(Date date, int years) {
        Calendar calendar = getCalendar(date);
        calendar.add(YEAR, years);
        return calendar.getTime();
    }

    /**
     * 得到两个日期的偏差值
     * <p>
     * 默认到天数，field = {@link java.util.Calendar#DAY_OF_MONTH}
     *
     * @param one
     * @param other
     * @return
     */
    public static int offset(Date one, Date other) {
        return offset(one, other, DAY_OF_MONTH);
    }

    /**
     * 得到两个日期的偏差值
     *
     * @param one
     * @param other
     * @param field
     * @return
     */
    public static int offset(Date one, Date other, int field) {
        switch (field) {
            case SECOND:
            case MINUTE:
            case HOUR:
                return offsetTime(one, other, field);
            case DAY_OF_MONTH:
            case WEEK_OF_MONTH:
            case MONTH:
            case YEAR:
                return offsetDate(one, other, field);
            default:
                break;
        }
        return 0;
    }

    /**
     * 得到两个日期的偏差值
     *
     * @param one
     * @param other
     * @param field
     * @return
     */
    private static int offsetTime(Date one, Date other, int field) {
        long oneTime = one.getTime();
        long otherTime = other.getTime();

        long offset = oneTime - otherTime;
        if (offset == 0) {
            return 0;
        }

        switch (field) {
            case SECOND:
                return (int) (offset / 1000);
            case MINUTE:
                return (int) (offset / (1000 * 60));
            case HOUR:
                return (int) (offset / (1000 * 60 * 60));
            default:
                break;
        }
        return 0;
    }

    /**
     * @param one
     * @param other
     * @param field
     * @return
     */
    private static int offsetDate(Date one, Date other, int field) {
        Calendar calendar = getCalendar(parse(format(one)));
        Calendar otherCalendar = getCalendar(parse(format(other)));

        switch (field) {
            case DAY_OF_MONTH:
                return (int) (calendar.getTimeInMillis() - otherCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
            case WEEK_OF_MONTH:
                return (int) (calendar.getTimeInMillis() - otherCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24 * 7);
            case MONTH:
                return (calendar.get(MONTH) - otherCalendar.get(MONTH)) + (calendar.get(YEAR) - otherCalendar.get(YEAR)) * 12;
            case YEAR:
                return calendar.get(YEAR) - otherCalendar.get(YEAR);
            default:
                break;
        }
        return 0;
    }

    /**
     * 获取指定日期的Calendar
     *
     * @param date
     * @return
     */
    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return calendar;
        }
        calendar.setTime(date);
        return calendar;
    }

    /**
     * @param calender
     */
    private static void setStartTimeOfDay(Calendar calender) {
        calender.set(Calendar.HOUR_OF_DAY, calender.getActualMinimum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, calender.getActualMinimum(Calendar.MINUTE));
        calender.set(Calendar.SECOND, calender.getActualMinimum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND, calender.getActualMinimum(Calendar.MILLISECOND));
    }

    public static Date getEndTimeOfDay(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setEndTimeOfDay(calender);
        return calender.getTime();

    }

    public static Date getStartTimeOfDay(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    private static void setEndTimeOfDay(Calendar calender) {
        calender.set(Calendar.HOUR_OF_DAY, calender.getActualMaximum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, calender.getActualMaximum(Calendar.MINUTE));
        calender.set(Calendar.SECOND, calender.getActualMaximum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND, calender.getActualMaximum(Calendar.MILLISECOND));
    }


    /**
     * 根据传递日期加天数，得到当前日期所在周一的日期
     */
    public static String getMonday(Date date, int i) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  


        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day + i);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        String imptimeBegin = sdf.format(cal.getTime()); //周一时间 

        return imptimeBegin;


    }

    /**
     * 查询周几
     * @param today
     * @return
     */
    public static int getWeek(Date today) {
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK)-1;
        if (weekday == 0) {
            weekday = 7;
        }
        return weekday;
    }



}
