package cn.dofuntech.gencode.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @info:
 * @Author:dengying
 * @Date:2011-10-7
 * @Version:1.0
 */
public class TimerUtils {
	/**
	 * 定时任务执行器
	 * 
	 * @param ses
	 * @param r
	 * @param d
	 * @return
	 */
	public static final ScheduledFuture<?> scheduled1(
			ScheduledExecutorService ses, Runnable r, Date d) {
		long delay = d.getTime() - System.currentTimeMillis();
		delay = delay <= 0 ? 1 : delay;
		return ses.schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 获得指定时间的凌晨时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMorning(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 加秒数得到新的时间
	 * 
	 * @param begin
	 * @param second
	 * @return
	 */
	public static Date returnDate(Date begin, int second) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(begin);
		calendar.add(Calendar.SECOND, second);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 根据long得到时间
	 * 
	 * @param _long
	 * @return
	 */
	public static Date getDateByMillisecond(long _long) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(_long);
		Date date = calendar.getTime();
		return date;
	}
}
