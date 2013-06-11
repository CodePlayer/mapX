package easymapping.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用于处理ResultSet对象的接口
 * @author Ready
 * @date 2012-5-20
 * @param <T>
 */
public interface ResultSetter<T> {


	/**
	 * 处理ResultSet并返回处理后的数据<br />
	 * 如果类型为集合类型，请将接口上的T改为对应的类型，例如：ResultSetter&lt;List&lt;Object&gt;&gt;或ResultSetter&lt;List&lt;Map&lt;String,String&gt;&gt;&gt;
	 * 如果为单个元素对象，请将接口上的T改为对应的类型，例如：Map&lt;String,String&gt;
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public T processResultSet(ResultSet rs) throws SQLException;
}
