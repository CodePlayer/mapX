package easymapping.jdbc.adapter;

/**
 * Oracle数据库分页适配器，用于对Oracle数据库的数据进行数据分页
 * @author Ready
 * @date 2012-6-15
 */
public class OraclePageAdapter extends PageAdapter {
	@Override
	public String getPageSQL(String sql, int pageId, int pageSize) {
		int endSize = pageId * pageSize;
		int startSize = endSize - pageSize + 1;
		// SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (SELECT * FROM TABLE_NAME) A WHERE ROWNUM <= 40 ) WHERE RN >= 21
		return "SELECT * FROM ( SELECT A_T_0.*, ROWNUM R_N_0 FROM (" + sql + ") A_T_0 WHERE ROWNUM <= " + endSize + " ) WHERE R_N_0 >= " + startSize;
	}
}
