package easymapping.jdbc;

import org.apache.commons.logging.Log;
import easymapping.util.X;

/**
 * 用于表示程序在访问数据库、进行相关数据操作时所触发的异常
 * @author Ready
 * @date 2012-11-26
 */
public class JdbcException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public static final Log log = X.getLog();

	/**
	 * 构造具有指定异常信息的JDBC异常实例
	 * @param message 指定的异常信息
	 */
	public JdbcException(String message) {
		super(message);
	}

	/**
	 * 构造具有指定异常源(导致该异常的异常)的JDBC异常实例
	 * @param cause 指定的异常源
	 */
	public JdbcException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造具有指定异常信息和异常源(导致该异常的异常)的JDBC异常实例
	 * @param message 指定的异常信息
	 * @param cause 指定的异常源
	 */
	public JdbcException(String message, Throwable cause) {
		super(message, cause);
	}
}
