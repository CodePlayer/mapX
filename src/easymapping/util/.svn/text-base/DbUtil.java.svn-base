package easymapping.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import easymapping.util.filter.Entry;
import easymapping.util.filter.FilterPool;
import easymapping.util.filter.SQLFilter;

/**
 * 用于执行数据库操作时所需的常用处理方法的工具类
 * @author Ready
 * @date 2012-12-1
 */
public class DbUtil {

	// 禁止实例创建
	private DbUtil() {}
	private static final WhereBox emptyBox = new WhereBox(new StringBuilder(), ArrayUtil.EMPTY_OBJECTS, null);

	/**
	 * 根据指定参数生成WHERE条件子句及相关的值，并以数组的形式返回<br>
	 * 返回的数组索引：0=SQL条件句(StringBuiler)，1=对应的预编译参数值数组，2=URL参数(如果不需要生成，则为null)<br>
	 * 如果指定的参数名数组为空，则返回null
	 * @param bean 指定的参数值集合Map
	 * @param whereSize 指定生成的子句中有效预编译参数值的个数，如果不为指定的个数，则
	 * @param condition 额外的SQL条件句，例如“IS_VALID = 1 AND RESULT = 2”，请不要添加“WHERE”或“AND”前缀，内部会自行添加
	 * @param urlPrefix 生成的URL参数前缀，如果不需要生成URL参数，请传入null，如果需要生成没有前缀的URL参数，请传入""，其他前缀，传入对应的前缀，例如“e.”
	 * @param keys 指定需要添加到WHERE条件句中的参数名数组，如果对应的参数值为空(如果是字符串且为null或length=0则为空，如果是其他对象，并且为null，才为空)，则不添加到条件句中
	 * @return
	 */
	public static WhereBox processWhere(Map<String, Object> bean, int whereSize, String condition, String urlPrefix, String... keys) {
		int length;
		if (keys == null || (length = keys.length) == 0) {// 如果指定的参数名数组为空
			return emptyBox;
		}
		StringBuilder whereSQL = new StringBuilder(length << 4);// WHERE子句
		List<Object> preparedValues = new ArrayList<Object>(length);// 预编译参数值集合
		boolean needURLArgs = urlPrefix != null;
		StringBuilder urlArgs = needURLArgs ? new StringBuilder(length << 4) : null;
		boolean notFirst = false;// 是否不是SQL语句第一个WHERE条件参数，默认为false
		for (int i = 0; i < length; i++) {
			String key = keys[i];
			Object value = bean.remove(key); // 从item中删除key，同时返回删除的value用于处理
			if (value != null && !(value instanceof String && value.toString().length() == 0)) { // 如果value值有效
				if (needURLArgs) {// 如果需要整合URL 参数
					if (notFirst) {
						urlArgs.append('&');
					}
					urlArgs.append(urlPrefix).append(key).append('=').append(value);
				}
				int keyLength = key.length();// key的长度
				int srcLength = keyLength - 3;
				String operator = "=";// 默认操作符为等号
				if (srcLength > 0 && key.charAt(srcLength) == SQLFilter.delimiter) {
					String suffix = key.substring(srcLength + 1).toUpperCase();// 取后面两位后缀的大写形式
					SQLFilter filter = FilterPool.getFilter(suffix);// 查找过滤器
					if (filter != null) {// 如果存在指定后缀的过滤器
						String realKey = key.substring(0, srcLength);
						Entry entry = filter.filter(realKey, value);
						if (entry.key != null) {
							key = entry.key;
						}
						operator = entry.operator;
						value = entry.value;
					}
				}
				if (notFirst) {// 如果已存在WHERE条件
					whereSQL.append(" AND ");
				} else {// 如果是第一个WHERE条件
					whereSQL.append(" WHERE ");
					notFirst = true;
				}
				whereSQL.append(key).append(operator).append('?');
				preparedValues.add(value);
			}
		}
		Assert.isTrue(whereSize == 0 || preparedValues.size() == whereSize, "非法请求，缺失必要的请求参数！");
		if (!X.isEmpty(condition)) { // 如果存在有效条件
			whereSQL.append(notFirst ? " AND " : " WHERE ").append(condition);
		}
		return new WhereBox(whereSQL, preparedValues.toArray(), needURLArgs ? urlArgs : null);
	}

	public static class WhereBox {

		public final StringBuilder whereSQL;
		public final Object[] whereValues;
		public final StringBuilder urlArgs;

		public WhereBox(StringBuilder whereSQL, Object[] whereValues, StringBuilder urlArgs) {
			this.whereSQL = whereSQL;
			this.whereValues = whereValues;
			this.urlArgs = urlArgs;
		}
	}
}
