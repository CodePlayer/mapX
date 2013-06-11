package mapx.jdbc.adapter;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import mapx.core.Bean;
import mapx.core.Page;
import mapx.jdbc.JdbcCommand;
import mapx.jdbc.ResultSetter;
import mapx.util.Assert;
import mapx.util.StringUtil;

/**
 * 数据库分页适配器接口<br />
 * 主要用于兼容数据库在数据分页方面的差异，以便于不同数据库实现统一风格的分页算法调用<br />
 * <b>注意：</b>此类的子类实现必须注入JdbcCommand属性值，一般情况下，实现类应该使用Spring进行依赖注入
 * @author Ready
 * @date 2012-6-6
 */
public abstract class PageAdapter {

	protected JdbcCommand jdbcCommand;

	public void setJdbcCommand(JdbcCommand jdbcCommand) {
		this.jdbcCommand = jdbcCommand;
	}

	public JdbcCommand getJdbcCommand() {
		return jdbcCommand;
	}

	/**
	 * 数据分页方法，返回分页引擎类(分页数据在分页引擎类中)，返回元素类型支持自定义
	 * @param <T>
	 * @param sql 查询的SQL语句
	 * @param args 预编译语句的参数值数组，没有可以为null
	 * @param pageId 需要查询的分页页数，1=第一页
	 * @param pageSize 每页显示的条数
	 * @param isNeedCount 是否需要查询总页数、总记录数。true=需要，false=不需要
	 * @param resultSetter ResultSet实现类，接口方法的返回值即为获取到的List数据集合
	 * @return
	 */
	public <T> Page<T> getPage(String sql, Object[] args, int pageId, int pageSize, boolean isNeedCount, ResultSetter<List<T>> resultSetter) {
		Assert.notNull(sql, "用于数据分页的SQL查询语句不能为null！");
		if (pageId < 1)
			pageId = 1;
		if (pageSize < 1)
			pageSize = 10;
		String pageSQL = getPageSQL(sql, pageId, pageSize);
		List<T> list = jdbcCommand.query(pageSQL, resultSetter, args);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setId(pageId);
		page.setSize(pageSize);
		if (isNeedCount) {
			int count = list.size();
			if (pageSize >= count) {// 如果查询出来的记录数大于等于当前分页标准条数，才查询总记录数
				count = countAll(sql, args);
			}
			page.setCount(count);
		}
		return page;
	}

	/**
	 * 数据分页方法，返回分页引擎类(分页数据在分页引擎类中)，返回元素类型支持自定义
	 * @param <T>
	 * @param sql 查询的SQL语句
	 * @param args 预编译语句的参数值数组，没有可以为null
	 * @param pageId 需要查询的分页页数，1=第一页
	 * @param pageSize 每页显示的条数
	 * @param isNeedCount 是否需要查询总页数、总记录数。true=需要，false=不需要
	 * @param resultSetter ResultSet实现类，接口方法的返回值即为获取到的List数据集合
	 * @return
	 */
	public <T> Page<T> getPage(String sql, Object[] args, int pageId, int pageSize, boolean isNeedCount, RowMapper<T> rowMapper) {
		Assert.notNull(sql, "用于数据分页的SQL查询语句不能为null！");
		if (pageId < 1)
			pageId = 1;
		if (pageSize < 1)
			pageSize = 10;
		String pageSQL = getPageSQL(sql, pageId, pageSize);
		List<T> list = jdbcCommand.query(pageSQL, rowMapper, args);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setId(pageId);
		page.setSize(pageSize);
		if (isNeedCount) {
			int count = list.size();
			if (pageSize >= count) {// 如果查询出来的记录数大于等于当前分页标准条数，才查询总记录数
				count = countAll(sql, args);
			}
			page.setCount(count);
		}
		return page;
	}

	/**
	 * 该方法由子类具体实现<br />
	 * 根据查询的SQL语句、分页查询页数以及每页显示条数获取适用于对应数据库的数据分页查询语句<br />
	 * 传入的sql、pageId和pageSize都已经进行了必须是非空、正数等逻辑性判断，无需再重复判断
	 * @param sql 查询的SQL语句
	 * @param pageId 需要查询的页数,第一页为1
	 * @param pageSize 每页显示条数
	 * @return
	 */
	public abstract String getPageSQL(String sql, int pageId, int pageSize);

	/**
	 * 为BeanProcessor准备的数据分页方法，返回分页引擎类(分页数据在分页引擎类中)，返回元素类型为Bean(继承自LinkedHashBean )
	 * @param sql 查询的SQL语句(SQL语句中的FROM必须为大写)
	 * @param args 预编译语句的参数值数组，没有可以为null
	 * @param page 封装分页数据以及相关参数的对象(提供使用的参数： id：需要查询的分页页数，1=第一页； size： 每页显示的条数
	 *            isNeedCount： 是否需要查询总页数、总记录数。true=需要，false=不需要 )
	 * @return
	 */
	public Page<Bean> getPageForProcessor(String sql, Object[] args, Page<Bean> page) {
		Assert.notNull(sql, "用于数据分页的SQL查询语句不能为null！");
		int pageId = page.getId();
		if (pageId < 1)
			page.setId(pageId = 1);
		int pageSize = page.getSize();
		if (pageSize < 1)
			page.setSize(pageSize = 10);
		String orderSQL = page.getOrderBy(); // 如果有排序，追加排序语句
		if (!StringUtil.isBlank(orderSQL)) {
			sql += orderSQL;
		}
		String pageSQL = getPageSQL(sql, pageId, pageSize);
		List<Bean> list = jdbcCommand.queryForList(pageSQL, args);
		page.setList(list);
		if (page.isNeedCount()) { // 如果需要查询总记录数
			int count = list.size();
			if (pageSize >= count) {// 如果查询出来的记录数大于等于当前分页标准条数，才查询总记录数
				count = countAll(sql, args);
			}
			page.setCount(count);
		}
		return page;
	}

	/**
	 * 根据SQL语句和对应的预编译参数值数组，获取符合当前SQL语句查询的总记录数
	 * @param sql 指定的SQL语句，<b>其中的关键字FROM必须大写</b>
	 * @param args 指定的预编译参数值数组
	 * @return
	 */
	protected int countAll(String sql, Object[] args) {
		int fromIndex = sql.indexOf(" FROM "); // FROM的位置索引，前后加空格，以防止读到含有FROM字符的列名
		Assert.isTrue(fromIndex > 0, "数据分页时要求传入的SQL语句中的关键字FROM必须为大写！");
		String countSQL = sql.substring(fromIndex); // 取“ FROM ”及其后面的SQL语句
		return jdbcCommand.queryForInt("SELECT COUNT(*)" + countSQL, args);
	}
}
