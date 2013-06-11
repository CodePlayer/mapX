package easymapping.jdbc.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Java时间适配器类，实现时间适配器TimeAdapter接口<br />
 * 用于获取Java虚拟机的当前系统时间
 * @author Ready
 * @date 2012-6-5
 */
public class JavaTimeAdapter implements TimeAdapter {
	public String currentDate() {
		return new java.sql.Date(System.currentTimeMillis()).toString();
	}

	public String currentDatetime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public String currentTime() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
}
