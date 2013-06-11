package mapx.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * 存储过程执行器
 * @author Ready
 * @date 2012-5-20
 * @param <T>
 */
public interface CallableStatementExecuter<T> {
	/**
	 * 处理存储过程对象，该方法的返回值将作为JdbcCommand.call()的返回值<br />
	 * 此方法执行前的语句为cstmt = conn.prepareCall(sql); <br />
	 * 因此，cstmt = conn.prepareCall(sql);语句后面的操作完全由此方法执行
	 * @param cstmt
	 * @return
	 * @throws SQLException
	 */
	public T executeProc(CallableStatement cstmt) throws SQLException;
}
