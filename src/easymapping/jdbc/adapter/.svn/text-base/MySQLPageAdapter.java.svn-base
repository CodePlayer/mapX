package easymapping.jdbc.adapter;

/**
 * MySQL数据库分页适配器，用于对MySQL数据库的数据进行数据分页
 * @author Ready
 * @date 2012-6-15
 */
public class MySQLPageAdapter extends PageAdapter {
	@Override
	public String getPageSQL(String sql, int pageId, int pageSize) {
		int startIndex = (pageId - 1) * pageSize; // 起始条数索引
		return sql + " LIMIT " + startIndex + "," + pageSize;
	}
}
