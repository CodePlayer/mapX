package mapx.jdbc;

/**
 * 用于对Bean处理器获得的参数进行验证、过滤的工具类
 * @author Ready
 * @date 2012-12-1
 */
public class BeanFilter {

	int wordCase = 2;
	int valueSize;
	int whereSize;
	String joinSQL;
	String condition;
	String orderBy;
	boolean cache;

	public int getWordCase() {
		return wordCase;
	}

	/**
	 * 设置查询SQL语句返回的列名的大小写形式
	 * @param isUpperCase 是否为大写形式，true=大写形式，false=小写形式，如果想为默认值，请不要调用此方法
	 */
	public void setWordCase(boolean isUpperCase) {
		this.wordCase = isUpperCase ? 1 : 0;
	}

	public int getValueSize() {
		return valueSize;
	}

	public void setValueSize(int valueSize) {
		this.valueSize = valueSize;
	}

	public int getWhereSize() {
		return whereSize;
	}

	/**
	 * 验证BeanProcessor处理后的SQL中WHERE子句的预编译参数个数，如果实际个数不等于指定的个数，则表示为非法请求、无效的参数<br>
	 * 如果指定的值为负数或0，则表示不验证
	 * @param whereSize
	 */
	public void setWhereSize(int whereSize) {
		this.whereSize = whereSize;
	}

	public String getJoinSQL() {
		return joinSQL;
	}

	public void setJoinSQL(String joinSQL) {
		this.joinSQL = joinSQL;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isCache() {
		return cache;
	}

	/**
	 * 对BeanProcessor分析的SQL语句及对应参数开启缓存，开启缓存后，BeanProcessor会在第一次执行的时候生成对应的SQL语句及预编译参数并缓存<br>
	 * 以后每次不再解析，直接利用缓存的SQL语句来执行<br>
	 * 由于缓存的SQL语句不能动态改变，因此要求INSERT时，插入的列都是固定不变的，SELECT、DELETE、UPDATE时，查询的WHERE子句列也是固定不变的<br>
	 * 因此执行INSERT插入时，必须指定插入的列数<code>valueSize</code>，系统会验证每次动态获得的有效预编译值个数必须等于<code>valueSize</code><br>
	 * 在执行SELECT、UPDATE、DELETE时，必须指定WHERE子句的预编译列数<code>whereSize</code>，系统会验证每次动态获得的有效预编译值个数必须等于<code>whereSize</code><br>
	 * 如果两者不等，就会引发异常，提示“非法请求，无效的请求参数!”
	 * @param valueSize 指定INSERT时，插入的固定列数，如果该值为0，则不验证
	 * @param whereSize 指定SELECT、UPDATE、DELETE时，WHERE子句的预编译列数，如果该值为0，则不验证
	 */
	public void startCache(int valueSize, int whereSize) {
		this.cache = true;
		this.valueSize = valueSize;
		this.whereSize = whereSize;
	}
}
