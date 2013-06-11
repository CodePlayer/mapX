package easymapping.jdbc.adapter;

/**
 * 时间适配器接口<br />
 * 用于获取当前系统时间，可以获取Java虚拟机的系统时间或数据库的系统时间(取决于Spring配置文件中的配置)<br />
 * 一般情况下，此接口的实现类应该使用Spring注入JdbcCommand属性值(如果获取的是Java虚拟机的系统时间时除外)
 * @author Ready
 * @date 2012-6-5
 */
public interface TimeAdapter {
	/**
	 * 获取系统的当前日期、时间<br />
	 * 一般格式为“yyyy-MM-dd HH:mm:ss”<br />
	 * 例如：“2012-03-15 13:55:02”
	 * @return
	 */
	public String currentDatetime();

	/**
	 * 获取系统的当前日期<br />
	 * 一般格式为“yyyy-MM-dd”<br />
	 * 例如：“2012-03-05”
	 * @return
	 */
	public String currentDate();

	/**
	 * 获取系统的当前时间<br />
	 * 一般格式为“HH:mm:ss”<br />
	 * 例如：“14:15:03”
	 * @return
	 */
	public String currentTime();
}
