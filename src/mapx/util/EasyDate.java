package mapx.util;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.WEEK_OF_MONTH;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import mapx.core.LogicException;

/**
 * 实现常用日期扩展方法的日期工具类(实现Comparable可比较接口、Cloneable可复制接口)
 * @author Ready
 * @date 2012-9-24
 */
public class EasyDate implements Comparable<Object>, Cloneable, Serializable {

	/**
	 * yyyy-MM-dd格式的日期转换器
	 */
	public static final DateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * yyyy-MM-dd HH:mm:ss格式的日期转换器
	 */
	public static final DateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyyyMMdd格式的日期转换器
	 */
	public static final DateFormat SHORT_DATE = new SimpleDateFormat("yyyyMMdd");
	/**
	 * yyyyMM格式的日期转换器
	 */
	public static final DateFormat YM_DATE = new SimpleDateFormat("yyyyMM");
	/**
	 * GMT标准格式的日期转换器[d MMM yyyy HH:mm:ss 'GMT']
	 */
	public static final DateFormat GMT_DATE = new SimpleDateFormat("d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
	/**
	 * Internet GMT标准格式的日期转换器[EEE, d MMM yyyy HH:mm:ss 'GMT']
	 */
	public static final DateFormat GMT_NET_DATE = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar calendar;

	/**
	 * 初始化日历对象相关设置
	 * @param calendar
	 */
	protected void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		this.calendar.setLenient(false);
		this.calendar.setFirstDayOfWeek(MONDAY);
	}

	/**
	 * 将常用日期对象封装为当前日期类实例对象
	 * @param date
	 */
	public EasyDate(java.util.Date date) {
		setCalendar(new GregorianCalendar());
		calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * 构造一个当前时间的日期实例对象
	 */
	public EasyDate() {
		setCalendar(new GregorianCalendar());
	}

	/**
	 * 根据指定的毫秒数构造对应的实例对象
	 * @param date
	 */
	public EasyDate(long date) {
		setCalendar(new GregorianCalendar());
		calendar.setTimeInMillis(date);
	}

	/**
	 * 根据年、月、日构造对应的实例对象
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 当前月的指定日
	 */
	public EasyDate(int year, int month, int day) {
		this(year, month, day, 0, 0, 0);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10
	 * 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0, -14, 0)
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * 该对象支持java.util.Date、mapx.util.EasyDate、java.util.Calendar等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param doffsetDay 相对于当前时间的日期偏移量
	 */
	public EasyDate(Object date, int offsetYear, int offsetMonth, int offsetDay) {
		this(getTimeOfDate(date));
		if (offsetYear != 0) {
			calendar.add(YEAR, offsetYear);
		}
		if (offsetMonth != 0) {
			calendar.add(MONDAY, offsetMonth);
		}
		if (offsetDay != 0) {
			calendar.add(DAY_OF_MONTH, offsetDay);
		}
	}

	/**
	 * 获取指定日期对象的毫秒值
	 * @param date
	 * @return
	 */
	private static final long getTimeOfDate(Object date) {
		long theTime;
		if (date == null) {
			theTime = System.currentTimeMillis();
		} else if (date instanceof Date) {
			theTime = ((Date) date).getTime();
		} else if (date instanceof EasyDate) {
			theTime = ((EasyDate) date).getTime();
		} else if (date instanceof Calendar) {
			theTime = ((Calendar) date).getTimeInMillis();
		} else {
			throw new LogicException("指定的对象不是日期类型的对象：" + date);
		}
		return theTime;
	}

	/**
	 * 根据年、月、日、时、分构造对应的实例对象
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 日
	 * @param hh 小时
	 * @param mm 分钟
	 */
	public EasyDate(int year, int month, int day, int hh, int mm) {
		this(year, month, day, hh, mm, 0);
	}

	/**
	 * 根据年、月、日、时、分、秒构造对应的实例对象
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 日
	 * @param hh 小时
	 * @param mm 分钟
	 * @param ss 秒
	 */
	public EasyDate(int year, int month, int day, int hh, int mm, int ss) {
		setCalendar(new GregorianCalendar(year, month - 1, day, hh, mm, ss));
	}

	/**
	 * 获取日期的年，例如：2012
	 * @return
	 */
	public int getYear() {
		return calendar.get(YEAR);
	}

	/**
	 * 设置日期的年，例如：2012
	 * @param year
	 */
	public void setYear(int year) {
		calendar.set(YEAR, year);
	}

	/**
	 * 追加指定的年数，例如：当前年是2012，调用addYear(2)，则年份为2014
	 * @param year 指定的年数，可以为负数
	 */
	public void addYear(int year) {
		calendar.add(YEAR, year);
	}

	/**
	 * 获取日期的月；返回值为1(一月)~12(十二月)
	 * @return
	 */
	public int getMonth() {
		return calendar.get(MONTH) + 1;
	}

	/**
	 * 设置日期的月；值为1(一月)~12(十二月)
	 * @return
	 */
	public void setMonth(int month) {
		calendar.set(MONTH, month - 1);
	}

	/**
	 * 追加指定的月数，例如：当前是2012-05-12，调用addMonth(2)，则为2012-07-12
	 * @param month 指定的月数，可以为负数
	 */
	public void addMonth(int month) {
		calendar.add(MONDAY, month);
	}

	/**
	 * 获取日期的日；月份的第一天返回1
	 * @return
	 */
	public int getDay() {
		return calendar.get(DAY_OF_MONTH);
	}

	/**
	 * 设置日期的日；月份的第一天为1
	 * @return
	 */
	public void setDay(int day) {
		calendar.set(DAY_OF_MONTH, day);
	}

	/**
	 * 追加指定的天数，例如：当前是2012-05-12，调用addDay(2)，则为2012-05-14
	 * @param month 指定的天数，可以为负数
	 */
	public void addDay(int day) {
		calendar.set(DAY_OF_MONTH, day);
	}

	/**
	 * 获取日期的星期；返回值为1(星期一)~7(星期天)
	 * @return
	 */
	public int getWeekDay() {
		int weekday = calendar.get(DAY_OF_WEEK);
		return weekday == SUNDAY ? 7 : --weekday;
	}

	/**
	 * 获取日期的时，返回值0~23
	 * @return
	 */
	public int getHour() {
		return calendar.get(HOUR_OF_DAY);
	}

	/**
	 * 设置日期的时，值为0~23
	 * @return
	 */
	public void setHour(int hour) {
		calendar.set(HOUR_OF_DAY, hour);
	}

	/**
	 * 追加指定的小时数，例如：当前是2012-05-12 12:12:56，调用addHour(3)，则为2012-05-12 15:12:56
	 * @param hour 指定的小时数，可以为负数
	 */
	public void addHour(int hour) {
		calendar.add(HOUR_OF_DAY, hour);
	}

	/**
	 * 获取日期的分，返回值0~59
	 * @return
	 */
	public int getMinute() {
		return calendar.get(MINUTE);
	}

	/**
	 * 设置日期的分，值为0~59
	 * @return
	 */
	public void setMinute(int minute) {
		calendar.set(MINUTE, minute);
	}

	/**
	 * 追加指定的分钟数，例如：当前是2012-05-12 09:12:56，调用addMinute(3)，则为2012-05-12 09:15:56
	 * @param hour 指定的分钟数，可以为负数
	 */
	public void addMinute(int minute) {}

	/**
	 * 获取日期的秒，返回值0~59
	 * @return
	 */
	public int getSecond() {
		return calendar.get(SECOND);
	}

	/**
	 * 设置日期的秒，值为0~59
	 * @param second
	 */
	public void setSecond(int second) {
		calendar.set(SECOND, second);
	}

	/**
	 * 追加指定的秒数，例如：当前是2012-05-12 09:12:56，调用addSecond(3)，则为2012-05-12 09:12:59
	 * @param hour 指定的秒数，可以为负数
	 * @param second
	 */
	public void addSecond(int second) {
		calendar.add(SECOND, second);
	}

	/**
	 * 获取日期的时间值，以毫秒为单位
	 * @return
	 */
	public long getTime() {
		return calendar.getTimeInMillis();
	}

	/**
	 * 设置日期的时间值，以毫秒为单位
	 * @param date
	 */
	public void setTime(long date) {
		calendar.setTimeInMillis(date);
	}

	/**
	 * 追加指定的毫秒数
	 * @param time 指定的毫秒数，可以为负数
	 */
	public void addTime(int time) {
		calendar.add(Calendar.MILLISECOND, time);
	}

	/**
	 * 以指定日期对象来重新设置日期
	 * @param date
	 */
	public void setDate(java.util.Date date) {
		calendar.setTime(date);
	}

	/**
	 * 获取日期当前月份的星期数
	 * @return
	 */
	public int getWeeksOfMonth() {
		return calendar.get(WEEK_OF_MONTH);
	}

	/**
	 * 获取日期当前年份的星期数
	 * @return
	 */
	public int getWeeksOfYear() {
		return calendar.get(WEEK_OF_YEAR);
	}

	/**
	 * 获取内置的日历对象
	 * @return
	 */
	public Calendar getCalendar() {
		return this.calendar;
	}

	/**
	 * 根据日期字符串的长度智能转换为对应的日期实例对象<br />
	 * 长度和格式对应如下(找不到对应格式将报错)：<br />
	 * 6=201206(年月)<br />
	 * 8=20120126(年月日)<br />
	 * 10=2012-01-02(年-月-日)<br />
	 * 19=2012-01-02 13:22:56(年-月-日 时:分:秒)
	 * @param date
	 * @return
	 */
	public static EasyDate smartParse(String date) {
		if (date == null) {
			throw new NullPointerException("指定的日期字符串不能为null");
		}
		int length = date.length(); // 字符串长度
		DateFormat format = null;
		switch (length) {
		case 10:// 2012-01-02
			format = DATE;
			break;
		case 19:// 2012-01-02 13:22:56
			format = DATETIME;
			break;
		case 8:// 20120126
			format = SHORT_DATE;
			break;
		case 6:// 201206
			format = YM_DATE;
			break;
		default:
			throw new IllegalArgumentException("智能转换日期字符串时无法找到对应的DateFormat.");
		}
		return parse(format, date);
	}

	/**
	 * 将指定的对象转为EasyDate日期对象<br>
	 * 支持java.util.Date、java.sql.Date、EasyDate、Calendar、字符串等类型转为EasyDate<br>
	 * 如果指定对象对null，则返回null
	 * @param date
	 * @return
	 */
	public static final EasyDate valueOf(Object date) {
		if (date == null) {
			return null;
		} else if (date instanceof String) {
			return smartParse((String) date);
		}
		return new EasyDate(getTimeOfDate(date));
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 * @param format 一般情况无需自己创建，可直接调用EasyDate.DATE、EasyDate.DATETIME、EasyDate.SHORT_DATE等内置的日期转换对象
	 * @param date 日期字符串
	 * @return
	 */
	public static EasyDate parse(DateFormat format, String date) {
		try {
			return new EasyDate(format.parse(date).getTime());
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 * @param format 指定的格式字符串，例如“yyyy-MM-dd”，内部将使用SimpleDateFormat进行转换
	 * @param date 日期字符串
	 * @return
	 */
	public static EasyDate parse(String format, String date) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return new EasyDate(dateFormat.parse(date));
		} catch (ParseException e) {
			throw new LogicException("无法将指定的日期字符串[" + date + "]使用SimpleDateFormat按照指定的格式[" + format + "]转为日期对象");
		}
	}

	/**
	 * 转为java.util.Date
	 * @return
	 */
	public java.util.Date toDate() {
		return new java.util.Date(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Date
	 * @return
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	/**
	 * 与指定日期进行比较，如果大于指定的日期返回正数；等于返回0；小于返回负数
	 * @param date 支持java.util.Date、java.sql.Date、java.util.Calendar、mapx.util.EasyDate等对象及其子类的比较
	 * @return
	 */
	public int compareTo(Object date) {
		if (date == null) {
			throw new NullPointerException("用于比较的指定日期对象不能为null。");
		} else if (this == date) {
			return 0;
		}
		return (int) (calendar.getTimeInMillis() - getTimeOfDate(date));
	}

	/**
	 * 判断指定年份是否为闰年
	 * @param year 例如2012
	 * @return
	 */
	public boolean isLeapYear(int year) {
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 判断当前日期年份是否为闰年
	 * @return
	 */
	public boolean isLeapYear() {
		return ((GregorianCalendar) calendar).isLeapYear(getYear());
	}

	/**
	 * 判断是否在指定日期的时间之后
	 * @param date
	 * @return
	 */
	public boolean after(Object date) {
		return compareTo(date) > 0;
	}

	/**
	 * 判断是否在指定日期的时间之前
	 * @param date
	 * @return
	 */
	public boolean before(Object date) {
		return compareTo(date) < 0;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = result * 31 + ((calendar == null) ? 0 : calendar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EasyDate other = (EasyDate) obj;
		if (calendar == null) {
			if (other.calendar != null)
				return false;
		} else if (!calendar.equals(other.calendar))
			return false;
		return true;
	}

	/**
	 * 返回yyyy-MM-dd格式的字符串
	 */
	@Override
	public String toString() {
		return DATE.format(toDate());
		// return new
		// StringBuilder(Integer.toString(getYear())).append("-").append(getMonth()).append("-").append(getDay()).toString();
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式的字符串
	 * @return
	 */
	public String toLocaleString() {
		return DATETIME.format(toDate());
	}

	/**
	 * 返回yyyyMMdd格式的字符串
	 * @return
	 */
	public String toShortString() {
		return SHORT_DATE.format(toDate());
	}

	/**
	 * 返回GMT标准格式的字符串，例如：1 Dec 2012 15:05:00 GMT
	 * @return
	 */
	public String toGMTString() {
		return GMT_DATE.format(toDate());
	}

	/**
	 * 返回Internet GMT标准格式的字符串,例如：Sat, 1 Dec 2012 23:05:00 GMT
	 * @return
	 */
	public String toGMTNetString() {
		return GMT_NET_DATE.format(toDate());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		EasyDate date = null;
		try {
			date = (EasyDate) super.clone();
			if (calendar != null) {
				date.calendar = (Calendar) calendar.clone();
			}
		} catch (CloneNotSupportedException e) {
		}
		return date;
	}
}
