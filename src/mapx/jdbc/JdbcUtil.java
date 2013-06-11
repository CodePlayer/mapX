package mapx.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import mapx.util.Assert;
import mapx.util.X;

/**
 * JDBC操作公共工具类(内部全为静态方法)<br />
 * 用于处理数据库连接、事务、共用连接等操作。
 * @author Ready
 * @date 2012-5-20
 */
public final class JdbcUtil {

	// 禁用实例调用
	private JdbcUtil() {}
	private static final Log log = X.getLog();
	/**
	 * 不需要事务或共用连接
	 */
	public static final int NONE = 0;
	/**
	 * 需要事务，但尚未开启
	 */
	public static final int TX_READY = 1;
	/**
	 * 已经开启事务
	 */
	public static final int TX_ACTIVE = 2;
	/**
	 * 需要共用连接，但尚未开启
	 */
	public static final int SHARE_READY = 3;
	/**
	 * 已经开启共用连接
	 */
	public static final int SHARE_ACTIVE = 4;
	/**
	 * 基于线程安全的连接存储变量
	 */
	private static final ThreadLocal<Connection> THREAD_RESOURCE = new ThreadLocal<Connection>();
	/**
	 * 基于线程安全的事务状态标识存储变量。存储是否开启事务的状态标识:0=不需要开启事务； 1=需要开启事务，但尚未开启；2=已经开启事务
	 */
	private static final IntegerThreadLocal DB_FLAG = new IntegerThreadLocal();

	/**
	 * 获取数据库连接，本方法会根据设定智能获取共用的连接或者事务连接
	 * @param dataSource
	 * @return
	 */
	public static Connection getConnection(DataSource dataSource) {
		Assert.notNull(dataSource, "没有指定的DataSource，请检查相关配置！");
		int dbFlag = DB_FLAG.get(); // 去数据连接状态标识
		Connection conn = null;
		try {
			switch (dbFlag) {
			case NONE: // 无特殊状态
				conn = dataSource.getConnection();
				break;
			case TX_READY: // 事务准备状态
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
				THREAD_RESOURCE.set(conn);
				DB_FLAG.set(TX_ACTIVE);
				break;
			case SHARE_READY: // 共享连接准备状态
				conn = dataSource.getConnection();
				THREAD_RESOURCE.set(conn);
				DB_FLAG.set(SHARE_ACTIVE);
				log.debug("开启数据连接共享。");
				break;
			case TX_ACTIVE: // 事务进行时状态
			case SHARE_ACTIVE: // 共享连接进行时状态
				conn = THREAD_RESOURCE.get();
				break;
			default:
				conn = dataSource.getConnection();
			}
		} catch (Exception e) {
			throw new JdbcException("获取数据库连接时发生异常！", e);
		}
		return conn;
	}

	/**
	 * 开启事务(注意：不可与共享连接beginShareConnection()交叉使用，否则可能报错！)<br />
	 * 和事务相关的方法还有commitTransaction()提交事务和rollback()回滚事务
	 */
	public static void beginTransaction() {
		DB_FLAG.set(TX_READY);
		log.debug("准备开启事务。");
	}

	/**
	 * 获取当前数据库连接的标识 :<br />
	 * 标识含义：NONE=不需要开启事务或共享连接，TX_READY=需要开启事务，但未正式开启，TX_ACTIVE=已经开启事务<br />
	 * SHARE_READY=需要共享连接，但尚未正式开启，SHARE_ACTIVE=已经开启共享连接
	 * @return
	 */
	public static int getDbFlag() {
		return DB_FLAG.get();
	}

	/**
	 * 回滚事务并关闭连接
	 */
	public static void rollback() {
		Connection conn = THREAD_RESOURCE.get();
		if (conn != null) {
			try {
				DB_FLAG.set(NONE); // 将事务状态改为关闭事务
				THREAD_RESOURCE.set(null);
				conn.rollback();
				log.debug("回滚事务。");
			} catch (Exception e) {
				throw new JdbcException("事务回滚时发生异常！", e);
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 提交事务并关闭连接(必须先调用beginTransaction()开启事务并执行数据操作后才能调用此方法，否则报错)
	 */
	public static void commitTransaction() {
		Assert.isTrue(DB_FLAG.get() == TX_ACTIVE, "必须先开启事务才能关闭事务！");
		Connection conn = THREAD_RESOURCE.get();
		try {
			DB_FLAG.set(NONE);
			THREAD_RESOURCE.set(null);
			conn.commit(); // 提交事务
			log.debug("提交事务。");
		} catch (Exception e) {
			throw new JdbcException("提交事务时发生异常！", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * 调用本方法后，当前线程将会共用一个数据库连接直到调用endShareConnection()方法停止共用<br />
	 * 注意：本方法不可与开启事务beginTransaction()交叉使用，否则可能报错！
	 */
	public static void beginShareConnection() {
		DB_FLAG.set(SHARE_READY);
	}

	/**
	 * 停止当前线程共用一个连接
	 */
	public static void endShareConnection() {
		Connection conn = THREAD_RESOURCE.get();
		if (conn != null) {
			try {
				THREAD_RESOURCE.set(null);
				DB_FLAG.set(NONE);
				conn.close();
				log.debug("关闭数据连接共享。");
			} catch (SQLException e) {
				log.debug("关闭当前线程共用的数据库连接时发生异常！", e);
			}
		}
	}

	/**
	 * 关闭数据库相关对象，如果没有对应对象，请传入null<br />
	 * 方法内部会智能判断当前线程是否开启事务或共用连接，从而确定是否关闭连接 <br />
	 * 如果方法内部发生异常，本方法不对外抛出异常，但会输出异常信息到控制台<br />
	 * 由于调用Statement.close()方法时，JDBC会自动关闭掉相关的ResultSet，所以此处不必传入ResultSet进行关闭
	 * @param stmt 指定的Statement
	 * @param conn 指定的Connection
	 */
	public static final void closeAll(Statement stmt, Connection conn) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception stmtE) {
			log.debug("关闭Statement时发生异常(异常被忽略)", stmtE);
		} finally {
			try {
				if (conn != null && DB_FLAG.get() == NONE) {
					THREAD_RESOURCE.set(null);
					conn.close();
				}
			} catch (Exception connE) {
				log.debug("关闭Connection时发生异常(异常被忽略)", connE);
			}
		}
	}

	/**
	 * 为指定的参数数组依次预编译赋值给指定的PreparedStatement
	 * @param pstmt 指定的PreparedStatement
	 * @param args 指定的预编译参数值数据
	 * @throws SQLException
	 */
	public static final void processArgs(PreparedStatement pstmt, Object[] args) throws SQLException {
		if (args != null) {
			int length = args.length;
			while (length > 0) {
				pstmt.setObject(length--, args[length]);
			}
		}
	}

	/**
	 * 在调试开发模式下，打印SQL语句及相关参数信息(没有参数时，可以为null)
	 * @param sql 指定的SQL语句
	 * @param args 预编译参数值数组，如果没有，可以为null
	 */
	public static final void debugSQL(String sql, Object[] args) {
		if (log.isDebugEnabled()) {
			System.out.println("SQL语句：" + sql);
			int length;
			if (args != null && (length = args.length) > 0) {
				StringBuilder sb = new StringBuilder("对应的参数值如下：");
				for (int i = 0; i < length; i++) {
					sb.append(args[i]).append("; ");
				}
				System.out.println(sb);
			}
		}
	}
}
