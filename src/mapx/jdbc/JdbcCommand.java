package mapx.jdbc;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import mapx.core.Bean;
import mapx.util.Assert;
import mapx.util.StringUtil;
import mapx.util.X;

/**
 * JDBC操作执行类，负责执行SQL语句的数据库操作<br />
 * 在其他所有方法之前，必须先调用setDataSource()方法注入数据源，否则将报错(一般使用Spring进行依赖注入)
 * @author Ready
 * @date 2012-5-20
 */
public class JdbcCommand {

	private static ResultSetter<Bean> _Bean;
	private static ResultSetter<Bean> _UpperCaseBean;
	private static ResultSetter<Bean> _LowerCaseBean;
	private static ResultSetter<List<Bean>> _BeanList;
	private static ResultSetter<List<Bean>> _UpperCaseBeanList;
	private static ResultSetter<List<Bean>> _LowerCaseBeanList;
	private static ResultSetter<Integer> _Integer;
	private static ResultSetter<Long> _Long;
	private static ResultSetter<Number> _Number;
	private DataSource dataSource;

	/**
	 * 获取数据源
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据源(调用所有方法前，必须先调用此方法，注入数据源)
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/*
	 * 执行没有返回值的SQL操作
	 */
	/**
	 * 执行指定的SQL语句，并忽略掉返回值<br>
	 * 该返回适合建表、建列等不需要返回值的SQL操作
	 * @param sql 指定的SQL语句
	 */
	public void execute(String sql) {
		Assert.notNull(sql, "SQL语句不能为null！");
		JdbcUtil.debugSQL(sql, null);
		Connection conn = JdbcUtil.getConnection(dataSource);
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			throw new JdbcException("执行SQL操作时[execute]发生异常", e);
		} finally {
			JdbcUtil.closeAll(stmt, conn);
		}
	}

	/*
	 * 执行数据的更新操作(插入、修改、删除)，并返回受影响的行数
	 */
	/**
	 * 本方法专门用于执行数据插入INSERT操作，并以long形式返回生成的主键值<br>
	 * <strong>警告：</strong>如果无法获取主键或者主键不是整数类型，则可能引发异常
	 * @param sql 指定的INSERT SQL
	 * @param args 预编译形式的参数值数组，如果没有，请不要传入
	 */
	public long insertForLong(String sql, Object... args) {
		Assert.notNull(sql, "SQL语句不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			JdbcUtil.processArgs(pstmt, args);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {// 如果有主键
				return rs.getLong(1);
			}
			throw new SQLException("无法获取执行数据插入操作的SQL语句返回的主键！");
		} catch (Exception e) {
			throw new JdbcException("执行可返回生成的主键的数据插入操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 本方法专门用于执行数据插入INSERT操作，并以Bean集合的形式返回生成的主键值<br>
	 * 本方法主要用于获取多列组合主键或者不是整数类型的主键<br>
	 * <strong>警告：</strong>如果无法获取主键，则可能引发异常
	 * @param sql 指定的INSERT SQL
	 * @param args 预编译形式的参数值数组，如果没有，请不要传入
	 * @return
	 */
	public Bean insertForBean(String sql, Object... args) {
		Assert.notNull(sql, "SQL语句不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			JdbcUtil.processArgs(pstmt, args);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {// 如果有主键
				ResultSetMetaData rsmd = rs.getMetaData();
				int columns = rsmd.getColumnCount();
				Bean bean = new Bean();
				for (int i = 1; i <= columns; i++) {
					bean.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				return bean;
			}
			throw new SQLException("无法获取执行数据插入操作的SQL语句返回的主键！");
		} catch (Exception e) {
			throw new JdbcException("执行可返回生成的主键的数据插入操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 以预编译方法执行指定的SQL语句，并返回受影响的行数
	 * @param sql 指定的SQL语句
	 * @param args 预编译对应的参数值数组，如果没有，请传入null
	 * @param notStrict 是否是非严格模式，如果是，受影响的行数为0则不报错，否则报错。
	 * @return
	 */
	private int update(String sql, Object[] args, boolean notStrict) {
		Assert.notNull(sql, "SQL语句不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.processArgs(pstmt, args);
			int rowCount = pstmt.executeUpdate();
			Assert.isTrue(notStrict || rowCount != 0, "执行的数据操作没有对数据库中的数据造成任何改变！");
			return rowCount;
		} catch (Exception e) {
			throw new JdbcException("执行数据更新操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 以预编译方法执行指定的SQL语句，并返回受影响的行数
	 * @param sql 指定的SQL语句
	 * @param args 可变参数形式的预编译参数值数组，如果没有参数，请不要传入
	 * @return
	 */
	public int update(String sql, Object... args) {
		return update(sql, args, true);
	}

	/**
	 * 以预编译方法执行指定的SQL语句，并返回受影响的行数<br>
	 * 此方法和update方法的区别是，此方法返回的受影响的行数如果为0，则抛出异常
	 * @param sql 指定的SQL语句
	 * @param args 可变参数形式的预编译参数值数组，如果没有参数，请不要传入
	 * @return
	 */
	public int mustUpdate(String sql, Object... args) {
		return update(sql, args, false);
	}

	/**
	 * 对指定的SQL语句和预编译参数值数组集合进行批处理操作，并返回受影响的行数数组<br>
	 * @param sql 指定的SQL语句 ，不能为null，否则报错
	 * @param argsList 参数值数组集合不能为null，集合中的对象数组也不能为null，否则报错
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> argsList) {
		Assert.isTrue(sql != null && argsList != null, "用于执行批处理的SQL语句或参数值数组集合不能为null！");
		int size = argsList.size();
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < size; i++) {
				Object[] args = argsList.get(i);
				JdbcUtil.processArgs(pstmt, args);
				pstmt.addBatch();
			}
			return pstmt.executeBatch();
		} catch (Exception e) {
			throw new JdbcException("执行批处理操作时[batchUpdate]发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 对指定的SQL语句集合进行批处理操作，并返回受影响的行数数组<br>
	 * @param sqlList SQL语句字符串集合，参数不能为null，否则报错！
	 * @return
	 */
	public int[] batchUpdate(List<String> sqlList) {
		Assert.notNull(sqlList, "SQL语句集合不能为null！");
		int size = sqlList.size();
		Connection conn = JdbcUtil.getConnection(dataSource);
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for (int i = 0; i < size; i++) {
				stmt.addBatch(sqlList.get(i));
			}
			return stmt.executeBatch();
		} catch (Exception e) {
			throw new JdbcException("执行批处理操作时[batchUpdate]发生异常！", e);
		} finally {
			JdbcUtil.closeAll(stmt, conn);
		}
	}

	/*
	 * 核心的查询方法
	 */
	/**
	 * 执行指定的SQL语句并返回rowMapper处理后的对象集合<br>
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param rowMapper RowMapper接口实例
	 * @param args 可变参数形式的预编译参数值数组，如果没有参数，请不要传入
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
		Assert.isTrue(sql != null && rowMapper != null, "SQL语句或RowMapper对象不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.processArgs(pstmt, args);
			ResultSet rs = pstmt.executeQuery();
			List<T> list = new ArrayList<T>();
			int rowNum = 0;
			while (rs.next()) {
				list.add(rowMapper.mapRow(rs, rowNum++));
			}
			return list;
		} catch (Exception e) {
			throw new JdbcException("执行数据查询操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 执行指定的SQL语句并返回ResultSetter处理后的对象集合<br>
	 * 返回值完全由ResultSetter的processResultSet()方法确定
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param resultSetter
	 * @param args 可变参数形式的预编译参数值数组，如果没有参数，请不要传入
	 * @return
	 */
	public <T> List<T> query(String sql, ResultSetter<List<T>> resultSetter, Object... args) {
		Assert.isTrue(sql != null && resultSetter != null, "SQL语句或ResultSetter对象不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.processArgs(pstmt, args);
			ResultSet rs = pstmt.executeQuery();
			return resultSetter.processResultSet(rs);
		} catch (Exception e) {
			throw new JdbcException("执行数据查询操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/*
	 * 查询单个对象
	 */
	/**
	 * 执行指定的SQL查询语句并以指定的对象形式返回查询到的第一行数据<br>
	 * 返回值完全由RowMapper接口的实现类决定
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param args 预编译参数值数组，如果没有，可以为null
	 * @param rowMapper 本方法的返回值与RowMapper接口方法的返回值一致
	 * @return
	 */
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
		Assert.isTrue(sql != null && rowMapper != null, "SQL语句或RowMapper对象不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.processArgs(pstmt, args);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? rowMapper.mapRow(rs, 0) : null;
		} catch (Exception e) {
			throw new JdbcException("执行数据查询操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 执行指定的SQL查询语句并返回查询到的数据的指定对象形式<br>
	 * 返回值可以为对象、集合等任意形式，返回值完全由ResultSetter接口的实现类决定
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param args 预编译参数值数组，如果没有，可以为null
	 * @param resultSetter 本方法的返回值与ResultSetter接口方法的返回值一致
	 * @return
	 */
	public <T> T queryForObject(String sql, ResultSetter<T> resultSetter, Object... args) {
		Assert.isTrue(sql != null && resultSetter != null, "SQL语句或ResultSetter对象不能为null！");
		JdbcUtil.debugSQL(sql, args);
		Connection conn = JdbcUtil.getConnection(dataSource);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.processArgs(pstmt, args);
			ResultSet rs = pstmt.executeQuery();
			return resultSetter.processResultSet(rs);
		} catch (SQLException e) {
			throw new JdbcException("执行数据查询操作时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(pstmt, conn);
		}
	}

	/**
	 * 获取指定SQL语句查询结果第一行的第一列，并以指定的类型返回<br>
	 * 方法内部智能判断返回类型，如果指定的类型为null，则返回null<br>
	 * 如果指定的类型无法识别，将调用ResultSet.getObject()方法获取
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param args 用于预编译的参数值数组，如果没有，可以为null
	 * @param clazz 指定返回值的类型
	 * @return
	 */
	public <T> T queryForObject(String sql, final Class<T> clazz, Object... args) {
		return queryForObject(sql, new ResultSetter<T>() {

			public T processResultSet(ResultSet rs) throws SQLException {
				return rs.next() ? getResultSetValue(clazz, rs, 1) : null;
			}
		}, args);
	}

	/*
	 * 查询 第一行第一列为数值类型的数据
	 */
	/**
	 * 执行指定的SQL语句并返回整数形式的值，返回的值为查询到的数据中第一行第一列的值的整数形式<br>
	 * 如果查询不到数据或无法转换为整型，将会报错！
	 * @param sql 指定的SQL语句
	 * @param args 对应的参数值数组，如果没有，可以为null
	 * @return
	 */
	public int queryForInt(String sql, Object... args) {
		return queryForObject(sql, getIntSetter(), args);
	}

	/**
	 * 执行指定的SQL语句并返回整数形式的值，返回的值为查询到的数据中第一行第一列的值的long整数形式<br>
	 * 如果查询不到数据或无法转换为长整型，将会报错！
	 * @param sql 指定的SQL语句
	 * @param args 对应的参数值数组，如果没有，可以为null
	 * @return
	 */
	public long queryForLong(String sql, Object... args) {
		return queryForObject(sql, getLongSetter(), args);
	}

	/**
	 * 执行指定的SQL语句并返回java.lang.Number形式的值，返回的值为查询到的数据中第一行第一列的值的Number形式<br>
	 * 此方法是获取第一行第一列数字类型数据的综合体，因为Number可以自由地转为其他数值类型的数据<br>
	 * 如果查询不到数据或无法转换为长整型，将可能报错！
	 * @param sql 指定的SQL语句
	 * @param args 对应的参数值数组，如果没有，可以为null
	 * @return
	 */
	public Number queryForNumber(String sql, Object... args) {
		return queryForObject(sql, getNumberSetter(), args);
	}

	/*
	 * 查询单个Bean对象或Bean对象集合
	 */
	/**
	 * 执行指定SQL语句并返回Bean形式的结果集<br>
	 * 如果查询到多条记录，只返回第一条，如果没有记录，则返回null<br>
	 * 返回的列名将以指定的大小写形式返回
	 * @param sql 指定SQL语句
	 * @param args 参数值数组，如果没有参数，可以为null
	 * @param isUpperCase 指示返回的列名采用大写还是小写，true=大写，false=小写
	 * @return
	 * @see mapx.core.HashBean
	 */
	public Bean queryForBean(String sql, boolean isUpperCase, Object... args) {
		return queryForObject(sql, isUpperCase ? getUpperCaseBeanSetter() : getLowerCaseBeanSetter(), args);
	}

	/**
	 * 执行指定SQL语句并返回Bean形式的ArrayList集合<br>
	 * 如果查询到多条记录，只返回第一条，如果没有记录，则返回null<br>
	 * @param sql 指定SQL语句
	 * @param args 参数值数组，如果没有参数，可以为null
	 * @return
	 * @see mapx.core.Bean
	 */
	public Bean queryForBean(String sql, Object... args) {
		return queryForObject(sql, getBeanSetter(), args);
	}

	/**
	 * 执行指定SQL语句并返回Bean<String, Object>形式的ArrayList集合<br>
	 * 如果查询不到数据，则返回空的List(长度为0) 如果没有参数，则为null<br>
	 * 返回的列名将以指定的大小写形式返回
	 * @param sql 指定的SQL语句
	 * @param args 预编译对应的参数值数组，如果没有，可以为null
	 * @param isUpperCase 指示返回的列名采用大写还是小写，true=大写，false=小写
	 * @return
	 * @see mapx.core.Bean
	 */
	public List<Bean> queryForList(String sql, boolean isUpperCase, Object... args) {
		return query(sql, isUpperCase ? getUpperCaseBeanListSetter() : getLowerCaseBeanListSetter(), args);
	}

	/**
	 * 执行指定SQL语句并返回Bean<String, Object>形式的ArrayList集合<br>
	 * 如果查询不到数据，则返回空的List(长度为0) 如果没有参数，则为null
	 * @param sql 指定的SQL语句
	 * @param args 预编译对应的参数值数组，如果没有，可以为null
	 * @return
	 * @see mapx.core.HashBean
	 */
	public List<Bean> queryForList(String sql, Object... args) {
		return query(sql, getBeanListSetter(), args);
	}

	/**
	 * 执行指定SQL语句并返回Bean<String, Object>形式的ArrayList集合<br>
	 * 如果查询不到数据，则返回空的List(长度为0) 如果没有参数，则为null
	 * @param sql 指定的SQL语句
	 * @return
	 * @see mapx.core.HashBean
	 */
	public List<Bean> queryForList(String sql) {
		return query(sql, getBeanListSetter());
	}

	/**
	 * 执行指定SQL语句并返回指定类型的List集合<br>
	 * 指定的类型必须为Java自带的常用类型，如String.class、int.class，并将第一列中每个单元格的数据转为对应的类型(如果有多列，
	 * 也不会报错，后面的列将被忽略)
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param args 对应的预编译参数对象数组
	 * @param clazz 指定的类型
	 * @return
	 */
	public <T> List<T> queryForList(String sql, final Class<T> clazz, Object... args) {
		return query(sql, new ResultSetter<List<T>>() {

			public List<T> processResultSet(ResultSet rs) throws SQLException {
				List<T> list = new ArrayList<T>();
				while (rs.next()) {
					T value = getResultSetValue(clazz, rs, 1);
					list.add(value);
				}
				return list;
			}
		}, args);
	}

	/**
	 * 执行指定SQL语句并返回指定类型的List集合<br>
	 * 指定的类型必须为Java自带的常用类型，如String.class、int.class，并将第一列中每个单元格的数据转为对应的类型(如果有多列，
	 * 也不会报错，后面的列将被忽略)
	 * @param <T>
	 * @param sql 指定的SQL语句
	 * @param args 对应的预编译参数对象数组
	 * @param clazz 指定的类型
	 * @return
	 */
	public <T> List<T> queryForList(String sql, final Class<T> clazz) {
		return queryForList(sql, null, clazz);
	}

	/**
	 * 检查指定表中是否存在符合条件的记录，如果存在(记录数大于0)，返回true<br>
	 * 内部构造的SQL为："SELECT COUNT(*) FROM " + tableName + " WHERE " +condition;<br>
	 * (condition为空时，不会加WHERE子句)
	 * @param tableName 指定的表名
	 * @param condition WHERE子句条件
	 * @param args 对应的预编译参数值数组，如果没有，可以为null
	 * @return
	 */
	public boolean queryExists(String tableName, String condition, Object[] args) {
		StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM ").append(tableName);
		if (!StringUtil.isEmpty(condition)) {
			sqlBuilder.append(" WHERE ").append(condition);
		}
		return queryForInt(sqlBuilder.toString(), args) > 0;
	}

	/**
	 * 调用存储过程并返回自定义的返回值
	 * @param <T>
	 * @param sql 指定的存储过程语句，例如："{ call PROC_NAME(?,?,?)}"
	 * @param cstmtExecuter 本方法的返回值由此接口的泛型实现决定
	 * @return
	 */
	public <T> T call(String sql, CallableStatementExecuter<T> cstmtExecuter) {
		Connection conn = JdbcUtil.getConnection(dataSource);
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall(sql);
			return cstmtExecuter.executeProc(cstmt);
		} catch (Exception e) {
			throw new JdbcException("执行存储过程时发生异常！", e);
		} finally {
			JdbcUtil.closeAll(cstmt, conn);
		}
	}

	/**
	 * 根据指定的ResultSet、索引返回指定类型的返回值
	 * @param <T>
	 * @param clazz 指定返回值的类型
	 * @param rs 数据集
	 * @param index 列的索引，第一行=1
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getResultSetValue(final Class<T> clazz, ResultSet rs, int index) throws SQLException {
		Object value = null;
		boolean wasNullCheck = false;
		if (clazz == null) {
			return null;
		} else if (String.class.equals(clazz)) {
			value = rs.getString(index);
			wasNullCheck = true;
		} else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
			value = rs.getInt(index);
			wasNullCheck = true;
		} else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
			value = rs.getDouble(index);
			wasNullCheck = true;
		} else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
			value = rs.getLong(index);
			wasNullCheck = true;
		} else if (Date.class.equals(clazz)) {
			value = rs.getDate(index);
		} else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
			value = rs.getBoolean(index);
			wasNullCheck = true;
		} else if (BigDecimal.class.equals(clazz)) {
			value = rs.getBigDecimal(index);
		} else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
			value = rs.getFloat(index);
			wasNullCheck = true;
		} else if (java.util.Date.class.equals(clazz) || java.sql.Date.class.equals(clazz)) {
			// 日期
			value = rs.getDate(index);
		} else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
			// 短整型
			value = rs.getShort(index);
			wasNullCheck = true;
		} else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
			// 字节
			value = rs.getByte(index);
			wasNullCheck = true;
		} else if (byte[].class.equals(clazz)) {
			value = rs.getBytes(index);
		} else if (Time.class.equals(clazz)) {
			value = rs.getTime(index);
		} else if (Timestamp.class.equals(clazz)) {
			value = rs.getTimestamp(index);
		} else if (Clob.class.equals(clazz)) {
			value = rs.getClob(index);
		} else if (Blob.class.equals(clazz)) {
			value = rs.getBlob(index);
		} else {
			value = rs.getObject(index);
		}
		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return (T) value;
	}

	/**
	 * 获取queryForBean方法的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Bean> getBeanSetter() {
		if (_Bean == null) {
			synchronized (Bean.class) {
				if (_Bean == null) {
					_Bean = new ResultSetter<Bean>() {

						public Bean processResultSet(ResultSet rs) throws SQLException {
							Bean bean = null;
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								int columns = rsmd.getColumnCount();
								bean = new Bean(columns);
								int i = 0;
								while (columns > i) {
									bean.put(rsmd.getColumnLabel(i), rs.getObject(i));
								}
							}
							return bean;
						}
					};
				}
			}
		}
		return _Bean;
	}

	/**
	 * 获取queryForBean方法的字段名大写的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Bean> getUpperCaseBeanSetter() {
		if (_UpperCaseBean == null) {
			synchronized (Bean.class) {
				if (_UpperCaseBean == null) {
					_UpperCaseBean = new ResultSetter<Bean>() {

						public Bean processResultSet(ResultSet rs) throws SQLException {
							Bean bean = null;
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								int columns = rsmd.getColumnCount();
								bean = new Bean(columns);
								int i = 0;
								while (columns > i) {
									bean.put(rsmd.getColumnLabel(i).toUpperCase(), rs.getObject(i));
								}
							}
							return bean;
						}
					};
				}
			}
		}
		return _UpperCaseBean;
	}

	/**
	 * 获取queryForBean方法的字段名小写的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Bean> getLowerCaseBeanSetter() {
		if (_LowerCaseBean == null) {
			synchronized (Bean.class) {
				if (_LowerCaseBean == null) {
					_LowerCaseBean = new ResultSetter<Bean>() {

						public Bean processResultSet(ResultSet rs) throws SQLException {
							Bean bean = null;
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								int columns = rsmd.getColumnCount();
								bean = new Bean(columns);
								int i = 0;
								while (columns > i) {
									bean.put(rsmd.getColumnLabel(i).toLowerCase(), rs.getObject(i));
								}
							}
							return bean;
						}
					};
				}
			}
		}
		return _LowerCaseBean;
	}

	/**
	 * 获取queryForList方法的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	public ResultSetter<List<Bean>> getBeanListSetter() {
		if (_BeanList == null) {
			synchronized (List.class) {
				if (_BeanList == null) {
					_BeanList = new ResultSetter<List<Bean>>() {

						public List<Bean> processResultSet(ResultSet rs) throws SQLException {
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								List<Bean> list = new ArrayList<Bean>();
								int columns = rsmd.getColumnCount();
								String[] columnNames = new String[columns];
								int i = 0;
								while (i < columns) {
									columnNames[i] = rsmd.getColumnLabel(++i);
								}
								int capacity = X.getCapacity(columns); // HashMap默认初始化容量
								Bean bean = null;
								do {
									bean = capacity > 16 ? new Bean(capacity) : new Bean();
									i = 0;
									while (i < columns) {
										bean.put(columnNames[i], rs.getObject(++i));
									}
									list.add(bean);
								} while (rs.next());
								return list;
							} else {
								return new ArrayList<Bean>(0);
							}
						}
					};
				}
			}
		}
		return _BeanList;
	}

	/**
	 * 获取queryForList方法的字段名大写的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	public ResultSetter<List<Bean>> getUpperCaseBeanListSetter() {
		if (_UpperCaseBeanList == null) {
			synchronized (List.class) {
				if (_UpperCaseBeanList == null) {
					_UpperCaseBeanList = new ResultSetter<List<Bean>>() {

						public List<Bean> processResultSet(ResultSet rs) throws SQLException {
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								List<Bean> list = new ArrayList<Bean>();
								int columns = rsmd.getColumnCount();
								String[] columnNames = new String[columns];
								int i = 0;
								while (i < columns) {
									columnNames[i] = rsmd.getColumnLabel(++i).toUpperCase();
								}
								int capacity = X.getCapacity(columns); // HashMap默认初始化容量
								Bean bean = null;
								do {
									bean = capacity > 16 ? new Bean(capacity) : new Bean();
									i = 0;
									while (i < columns) {
										bean.put(columnNames[i], rs.getObject(++i));
									}
									list.add(bean);
								} while (rs.next());
								return list;
							} else {
								return new ArrayList<Bean>(0);
							}
						}
					};
				}
			}
		}
		return _UpperCaseBeanList;
	}

	/**
	 * 获取queryForList方法的字段名小写的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	public ResultSetter<List<Bean>> getLowerCaseBeanListSetter() {
		if (_LowerCaseBeanList == null) {
			synchronized (List.class) {
				if (_LowerCaseBeanList == null) {
					_LowerCaseBeanList = new ResultSetter<List<Bean>>() {

						public List<Bean> processResultSet(ResultSet rs) throws SQLException {
							if (rs.next()) {
								ResultSetMetaData rsmd = rs.getMetaData();
								List<Bean> list = new ArrayList<Bean>();
								int columns = rsmd.getColumnCount();
								String[] columnNames = new String[columns];
								int i = 0;
								while (i < columns) {
									columnNames[i] = rsmd.getColumnLabel(++i).toLowerCase();
								}
								int capacity = X.getCapacity(columns); // HashMap默认初始化容量
								Bean bean = null;
								do {
									bean = capacity > 16 ? new Bean(capacity) : new Bean();
									i = 0;
									while (i < columns) {
										bean.put(columnNames[i], rs.getObject(++i));
									}
									list.add(bean);
								} while (rs.next());
								return list;
							} else {
								return new ArrayList<Bean>(0);
							}
						}
					};
				}
			}
		}
		return _LowerCaseBeanList;
	}

	/**
	 * 获取queryForInt方法的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Integer> getIntSetter() {
		if (_Integer == null) {
			synchronized (Integer.class) {
				if (_Integer == null) {
					_Integer = new ResultSetter<Integer>() {

						public Integer processResultSet(ResultSet rs) throws SQLException {
							if (rs.next())
								return rs.getInt(1);
							throw new JdbcException("指定的SQL查询不到对应的Int数据！");
						}
					};
				}
			}
		}
		return _Integer;
	}

	/**
	 * 获取queryForLong方法的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Long> getLongSetter() {
		if (_Long == null) {
			synchronized (Long.class) {
				if (_Long == null) {
					_Long = new ResultSetter<Long>() {

						public Long processResultSet(ResultSet rs) throws SQLException {
							if (rs.next())
								return rs.getLong(1);
							throw new JdbcException("执行数据查询操作时发生异常！");
						}
					};
				}
			}
		}
		return _Long;
	}

	/**
	 * 获取queryForNumber方法的ResultSetter实例对象(内部采用单例)
	 * @return
	 */
	private ResultSetter<Number> getNumberSetter() {
		if (_Number == null) {
			synchronized (Number.class) {
				if (_Number == null) {
					_Number = new ResultSetter<Number>() {

						public Number processResultSet(ResultSet rs) throws SQLException {
							if (rs.next())
								return rs.getDouble(1);
							throw new JdbcException("执行数据查询操作时发生异常！");
						}
					};
				}
			}
		}
		return _Number;
	}
}
