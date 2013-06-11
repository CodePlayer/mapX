package mapx.util.filter;

/**
 * 用于存储SQLFilter过滤后的相关参数的过滤实例
 * @author Ready
 * @date 2012-12-8
 */
public class Entry {

	/**
	 * 过滤后的键值
	 */
	public final String key;
	/**
	 * 过滤后的操作符
	 */
	public final String operator;
	/**
	 * 过滤后的参数值
	 */
	public final Object value;

	/**
	 * 构造具备所有相关参数的过滤箱实例
	 * @param key
	 * @param operator
	 * @param value
	 */
	public Entry(String key, String operator, Object value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
	}
}
