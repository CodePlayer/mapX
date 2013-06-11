package mapx.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import mapx.core.Bean;
import mapx.core.Page;
import mapx.jdbc.adapter.PageAdapter;
import mapx.util.Assert;
import mapx.util.DbUtil;
import mapx.util.StringUtil;
import mapx.util.X;
import mapx.util.DbUtil.WhereBox;

/**
 * 专用于对Bean对象(继承自LinkedHashMap&lt;String, Object&gt;)进行常用数据处理操作的封装工具类<br>
 * <strong>注意：</strong>本类必须设置属性jdbcCommand和pageAdapter的值，一般使用Spring进行依赖注入
 * @author Ready
 * @date 2012-10-21
 */
public class BeanProcessor {

	private JdbcCommand jdbcCommand;
	private PageAdapter pageAdapter;

	public void setPageAdapter(PageAdapter pageAdapter) {
		this.pageAdapter = pageAdapter;
	}

	public JdbcCommand getJdbcCommand() {
		return jdbcCommand;
	}

	public void setJdbcCommand(JdbcCommand jdbcCommand) {
		this.jdbcCommand = jdbcCommand;
	}

	/**
	 * 经过深度封装的单表更新方法，用于执行涉及单个表的更新操作<br>
	 * 需要更新的参数值为null时，转为空字符串<br>
	 * <b>重要：</b>当<code>valueSize</code>和<code>whereSize</code>都大于0时，将会自动开启缓存<br>
	 * 如果用于WHERE子句的参数值为空时，则不加入WHERE条件
	 * @param valueSize 需要更新的预编译列参数个数，为0则不限制
	 * @param whereSize WHERE子句的预编译参数个数，为0则不限制
	 * @param tableName 需要更新的单个表名
	 * @param bean 封装单表数据的BeanMap
	 * @param condition 额外的WHERE参数条件，不能以WHERE或者AND开头，内部会智能添加，例如：“IS_VALID = 1 AND PAY_STATUS = 2”，如果不需要添加，可以为null
	 * @param updateKeys WHERE子句的列名数组(可变参数)，如果列名在map中对应的值为null，则不加入WHERE子句
	 * @return
	 */
	public int update(String tableName, Map<String, Object> bean, String condition, boolean isCache, String... updateKeys) {
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		if (box == null) { // 如果找不到缓存的SQL语句，则分析相关请求参数
			int whereSize = isCache ? (updateKeys == null ? 0 : updateKeys.length) : 0;
			WhereBox wb = DbUtil.processWhere(bean, whereSize, condition, null, updateKeys);
			int setSize = bean.size(); // 去除WHERE条件参数后剩余的map元素个数
			Assert.isTrue(setSize > 0, "非法请求，缺失必要的请求参数！");
			StringBuilder sqlBuilder = new StringBuilder(20 + (setSize << 4));
			sqlBuilder.append("UPDATE ").append(tableName).append(" SET ");
			int whereLength = wb.whereValues.length;
			Object[] valueArray = new Object[setSize + whereLength];
			String[] keyArray = new String[setSize + whereLength];
			int count = 0; // 循环计数器
			Iterator<Entry<String, Object>> it = bean.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				// 拼凑SET语句
				String column = entry.getKey();
				keyArray[count] = column;
				valueArray[count] = entry.getValue();
				// SET语句最后一个列末尾没有逗号
				sqlBuilder.append(column).append(setSize == ++count ? "=?" : "=?, ");
			}
			if (wb.whereSQL.length() > 0) {
				sqlBuilder.append(wb.whereSQL); // 最终的SQL语句
			}
			if (whereLength > 0) { // 如果WHERE子句有参数，则合并参数到valueArray中
				System.arraycopy(wb.whereValues, 0, valueArray, setSize, whereLength);
			}
			String updateSQL = sqlBuilder.toString();
			if (isCache) { // 如果开启缓存
				if (whereLength > 0) {
					System.arraycopy(updateKeys, 0, keyArray, setSize, whereLength);
				}
				SQLBox.addCache(cacheKey, new SQLBox(updateSQL, keyArray));
			}
			return jdbcCommand.update(updateSQL, valueArray);
		} else { // 如果找到缓存数据，使用相应的方法进行处理
			return jdbcCommand.update(box.sql, getArgs(bean, box.args));
		}
	}

	/**
	 * 经过深度封装的单表删除方法，用于执行涉及单个表的删除操作<br>
	 * 用于WEHRE子句的参数值为空时，则不加入WHERE子句
	 * @param map 封装单表数据的Map
	 * @param whereSize WHERE子句的参数个数，为0则不限制
	 * @param tableName 需要删除数据的单个表名
	 * @param condition 额外的WHERE参数条件
	 * @param deleteKeys WHERE子句的列名数组(可变参数)，如果列名在map中对应的值为空，则不加入WHERE子句
	 * @return
	 */
	public int delete(String tableName, Map<String, Object> bean, String condition, boolean isCache, String... deleteKeys) {
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		if (box == null) {
			int whereSize = isCache ? (deleteKeys == null ? 0 : deleteKeys.length) : 0;
			WhereBox wb = DbUtil.processWhere(bean, whereSize, condition, null, deleteKeys);
			StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ").append(tableName); // DELETE主句
			if (wb.whereSQL.length() > 0) {
				sqlBuilder.append(wb.whereSQL); // 最终的SQL语句
			}
			String deleteSQL = sqlBuilder.toString();
			if (isCache) {
				SQLBox.addCache(cacheKey, new SQLBox(deleteSQL, deleteKeys));
			}
			return jdbcCommand.update(deleteSQL, wb.whereValues);
		} else {
			return jdbcCommand.update(box.sql, getArgs(bean, box.args));
		}
	}

	/**
	 * 经过深度封装的单表插入方法，用于执行涉及单个表的插入操作<br>
	 * 需要插入的参数值为null时，转为空字符串
	 * @param bean 封装单表数据的Map
	 * @param valueSize 需要插入的列数
	 * @param tableName 需要插入数据的表名
	 * @return
	 */
	public int insert(String tableName, Map<String, Object> bean, int valueSize) {
		int size = bean.size();// 如果为null，直接抛出异常
		// 要求插入的参数值必须大于0，如果指定了预编译个数，则bean的参数值个数应该大于等于指定的预编译个数
		boolean isCache = valueSize > 0;
		Assert.isTrue(size > 0 && (!isCache || size >= valueSize), "非法请求，缺失必要的请求参数！");
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		if (box == null) {
			StringBuilder sql = new StringBuilder(64).append("INSERT INTO ").append(tableName).append('('); // INSERT子句，类似于"INSERT INTO tableName(c1,c2,c3,c4) "
			StringBuilder valuesSQL = new StringBuilder(32).append(" VALUES("); // VALUES子句，类似于"VALUES(?,?,?,?)"
			List<Object> valueList = new ArrayList<Object>(size);
			List<String> keyList = isCache ? new ArrayList<String>(size) : null;
			Iterator<Entry<String, Object>> it = bean.entrySet().iterator();
			boolean notFirst = false;
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				Object value = entry.getValue();
				if (StringUtil.isEmpty(value)) {
					continue;
				}
				String key = entry.getKey();
				if (isCache) {
					keyList.add(key);
				}
				valueList.add(value);
				if (notFirst) {
					sql.append(',');
				} else {
					notFirst = true;
				}
				sql.append(key);
				valuesSQL.append('?');
			}
			Assert.isTrue(!isCache || valueSize == valueList.size(), "非法请求，缺失必要的请求参数！");
			sql.append(valuesSQL);
			String insertSQL = sql.toString();
			if (isCache) {
				SQLBox.addCache(cacheKey, new SQLBox(insertSQL, keyList.toArray(new String[keyList.size()])));
			}
			return jdbcCommand.update(insertSQL, valueList.toArray());
		} else {// 如果找到缓存数据，使用相应的方法进行处理
			return jdbcCommand.update(box.sql, getArgs(bean, box.args));
		}
	}

	/**
	 * 经过深度封装的单表查询方法，用于执行涉及单个表的查询操作
	 * @param tableName 需要查询的表名
	 * @param columnSQL 查询列的列名
	 * @param bean 封装单表数据的Map
	 * @param cacheKey 用于缓存SQL语句的key
	 * @param valueSize 需要读取的列数(0=不限制)
	 * @param whereSize WHERE子句的预编译条件个数
	 * @param wordCase 是否返回指定大小写形式的列名(0=小写，1=大写，其他=默认)
	 * @param condition 额外的WHERE条件
	 * @param selectKeys WHERE子句的列名数组(可变参数)，如果列名在map中对应的值为null，则不加入WHERE子句
	 * @return
	 */
	public Bean getBean(String tableName, String columnSQL, Map<String, Object> bean, String condition, String orderSQL, int wordCase, boolean isCache, String... selectKeys) {
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		Object[] args = null;
		String selectSQL = null;
		if (box == null) {
			int whereSize = isCache ? (selectKeys == null ? 0 : selectKeys.length) : 0;
			WhereBox wb = DbUtil.processWhere(bean, whereSize, condition, null, selectKeys);
			StringBuilder sqlBuilder = new StringBuilder("SELECT ").append(columnSQL == null ? "*" : columnSQL).append(" FROM ").append(tableName);
			if (wb.whereSQL.length() > 0) {
				sqlBuilder.append(wb.whereSQL);
			}
			args = wb.whereValues;
			selectSQL = sqlBuilder.toString();
			if (isCache) {
				SQLBox.addCache(cacheKey, new SQLBox(selectSQL, selectKeys));
			}
		} else {// 如果找到缓存数据，使用相应的方法进行处理
			args = getArgs(bean, box.args);
		}
		if (!X.isEmpty(orderSQL)) {
			selectSQL += orderSQL;
		}
		Bean result;
		switch (wordCase) {
		case 0:
			result = jdbcCommand.queryForBean(selectSQL, false, args);
			break;
		case 1:
			result = jdbcCommand.queryForBean(selectSQL, true, args);
			break;
		default:
			result = jdbcCommand.queryForBean(selectSQL, args);
			break;
		}
		return result;
	}

	/**
	 * 经过深度封装的单表查询方法，用于执行涉及单个表的查询操作
	 * @param tableName 需要查询的表名
	 * @param columnSQL 查询列的列名
	 * @param bean 封装单表数据的Map
	 * @param cacheKey 用于缓存SQL语句的key
	 * @param valueSize 需要读取的列数(0=不限制)
	 * @param whereSize WHERE子句的预编译条件个数
	 * @param wordCase 是否返回指定大小写形式的列名(0=小写，1=大写，其他=默认)
	 * @param condition 额外的WHERE条件
	 * @param selectKeys WHERE子句的列名数组(可变参数)，如果列名在map中对应的值为null，则不加入WHERE子句
	 * @return
	 */
	public List<Bean> getList(String tableName, String columnSQL, Map<String, Object> bean, String condition, String orderSQL, int wordCase, boolean isCache, String... selectKeys) {
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		Object[] args = null;
		String selectSQL = null;
		if (box == null) {
			int whereSize = isCache ? (selectKeys == null ? 0 : selectKeys.length) : 0;
			WhereBox wb = DbUtil.processWhere(bean, whereSize, condition, null, selectKeys);
			StringBuilder sqlBuilder = new StringBuilder("SELECT ").append(columnSQL == null ? "*" : columnSQL).append(" FROM ").append(tableName);
			if (wb.whereSQL.length() > 0) {
				sqlBuilder.append(wb.whereSQL);
			}
			args = wb.whereValues;
			selectSQL = sqlBuilder.toString();
			if (isCache) {
				SQLBox.addCache(cacheKey, new SQLBox(selectSQL, selectKeys));
			}
		} else {// 如果找到缓存数据，使用相应的方法进行处理
			selectSQL = box.sql;
			args = getArgs(bean, box.args);
		}
		if (!X.isEmpty(orderSQL)) {
			selectSQL += orderSQL;
		}
		List<Bean> list;
		switch (wordCase) {
		case 0:
			list = jdbcCommand.queryForList(selectSQL, false, args);
			break;
		case 1:
			list = jdbcCommand.queryForList(selectSQL, true, args);
			break;
		default:
			list = jdbcCommand.queryForList(selectSQL, args);
			break;
		}
		return list;
	}

	/**
	 * 用于进行数据分页功能的单表查询方法
	 * @param tableName 查询的表名
	 * @param columnSQL 列名部分SQL语句
	 * @param bean 请求数据集合
	 * @param cacheKey 用于缓存SQL的key
	 * @param valueSize 查询的列数
	 * @param whereSize WHERE子句的预编译参数值个数
	 * @param condition 额外的WHERE条件
	 * @param order 排序部分的SQL语句
	 * @param selectKeys WHERE子句的列名数组(可变参数)，如果列名在map中对应的值为空，则不加入WHERE子句
	 * @return
	 */
	public Page<Bean> getListAsPage(String columnSQL, String tableName, Map<String, Object> bean, Page<Bean> page, String condition, boolean isCache, String... selectKeys) {
		Assert.notNull(this.pageAdapter, "您尚未向ProcessorService对象中的分页适配器属性pageAdapter注入值，请先使用Spring进行相关依赖注入！");
		SQLBox box = null;
		String cacheKey = null;
		if (isCache) {
			StackTraceElement caller = new Throwable().getStackTrace()[1];
			cacheKey = caller.getClassName() + '_' + caller.getLineNumber();
			box = SQLBox.getCache(cacheKey);
		}
		Object[] whereArray = null;
		String selectSQL = null;
		if (box == null) {
			int whereSize = isCache ? (selectKeys == null ? 0 : selectKeys.length) : 0;
			String urlPrefix = bean instanceof Bean ? ((Bean) bean).getPrefix() : "e.";
			if (urlPrefix == null) {
				urlPrefix = "";
			}
			WhereBox wb = DbUtil.processWhere(bean, whereSize, condition, urlPrefix, selectKeys);
			if (wb.urlArgs != null) {
				page.setArgs(wb.urlArgs.toString());
			}
			StringBuilder sqlBuilder = new StringBuilder("SELECT ").append(columnSQL == null ? "*" : columnSQL).append(" FROM ").append(tableName);
			if (wb.whereSQL.length() > 0) {
				sqlBuilder.append(wb.whereSQL);
			}
			whereArray = wb.whereValues;
			selectSQL = sqlBuilder.toString();
			if (isCache) {
				SQLBox.addCache(cacheKey, new SQLBox(selectSQL, selectKeys));
			}
		} else {
			selectSQL = box.sql;
			whereArray = getArgs(bean, box.args);
		}
		return this.pageAdapter.getPageForProcessor(selectSQL, whereArray, page);
	}

	/**
	 * 自定义SQL语句的更新、插入、删除操作
	 * @param bean
	 * @param sql
	 * @param updateKeys
	 * @return
	 */
	public int customUpdate(Map<String, Object> bean, String sql, String... updateKeys) {
		return jdbcCommand.update(sql, getArgs(bean, updateKeys));
	}

	/**
	 * 自定义SQL语句的查询操作
	 * @param bean
	 * @param sql
	 * @param selectKeys
	 * @return
	 */
	public List<Bean> customQuery(Map<String, Object> bean, String sql, String... selectKeys) {
		return jdbcCommand.queryForList(sql, getArgs(bean, selectKeys));
	}

	/**
	 * 将Map中指定参数名的参数值以对象数组的形式返回，如果存在为空的参数值则引发异常
	 * @param bean
	 * @param keys
	 * @return
	 */
	private static final Object[] getArgs(Map<String, Object> bean, String[] keys) {
		Object[] args = null;
		int length;
		if (keys != null && (length = keys.length) > 0) {
			args = new Object[length];
			for (int i = 0; i < length; i++) {
				args[i] = bean.get(keys[i]);
				Assert.notEmpty(args[i], "非法操作，缺失必要的请求参数！");
			}
		}
		return args;
	}
}
