package mapx.core;

import java.math.BigDecimal;
import java.util.List;

/**
 * 增强的映射集合接口，继承Map接口，并增加常用的工具处理方法
 * @author Ready
 * @date 2012-10-11
 */
public interface EnhanceList<E> extends List<E> {

	/**
	 * 以String形式返回指定索引处的值，如果指定的值为null，仍返回null
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public String getString(int index);

	/**
	 * 以去除两边空格(trim)的String形式返回指定索引处的值<br>
	 * 如果值为null将返回空字符串""
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public String getTrim(int index);

	/**
	 * 以去除两边空格(trim)的String形式返回指定索引处的值<br>
	 * 如果值为null将返回html形式的空格字符串"&nbsp;"
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public String getTrimHtml(int index);

	/**
	 * 以String的形式返回指定的字符串，如果指定字符串(去空格后)超过限制长度maxLength,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串""<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param maxLength 允许的最大长度
	 * @return
	 */
	public String getLimit(int index, int maxLength);

	/**
	 * 以String的形式返回指定索引处的值，如果指定字符串(去空格后)超过限制长度maxLength,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串""<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀suffix
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param maxLength 最大限制长度
	 * @param suffix 超出长度时添加的指定后缀,如果不需要，可以为null
	 * @return
	 */
	public String getLimit(int index, int maxLength, String suffix);

	/**
	 * 解析指定索引index处的值，按照指定的表达式返回对应的结果
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param expressions 指定的表达式，例如：<code>"1", "男", "0", "女"</code><br>
	 * 方法将会将指定属性的值(value)，与表达式进行匹配，形如：<br>
	 * <code>
	 * if(value 等于 "1"){<br>
	 * 		return "男";<br>
	 * }else if(value 等于 "0"){<br>
	 * 		return "女";<br>
	 * }else{<br>
	 * 		return value;<br>
	 * }
	 * </code><br>
	 * 本方法接收的表达式参数个数可以为奇数，例如：<code>6, "星期六", 7, "星期天", "工作日"</code><br>
	 * 相当于：<br>
	 * if(value 等于 6){<br>
	 * 		return "星期六";<br>
	 * }else if(value 等于 7){<br>
	 * 		return "星期天";<br>
	 * }else{<br>
	 * 		return "工作日";<br>
	 * }
	 * @return
	 */
	public E decode(int index, E... expressions);

	/**
	 * 本方法与方法decode()具备一样的功能，不同的是，如果解析后的值与最初的映射值不相等(使用equals比较)，本方法还将会用解析后的值替换掉映射中原来的值
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param expressions 指定的表达式
	 * @return
	 * @see mapx.core.EnhanceMap#decode(Object, Object...)
	 */
	public E decodeReference(int index, E... expressions);

	/**
	 * 获取指定索引index处的值，如果值为空(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param defaultValue 原值为空时，用于返回的值
	 * @see mapx.util.X#isEmpty(Object)
	 * @return
	 */
	public E getDefault(int index, E defaultValue);

	/**
	 * 获取指定索引index处的值，如果值为空(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值
	 * <b>注意：</b>此方法与getDefault()方法不同的是：如果原值为空(<code>X.isEmpty(value) == true</code>)，该方法还会用默认值替换掉Bean中的原值
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @param defaultValue 原值为空时，用于返回的值
	 * @see mapx.util.X#isEmpty(Object)
	 * @return
	 */
	public E getDefaultReference(int index, E defaultValue);

	/**
	 * 以int形式返回指定索引index处的值<br>
	 * 如果该值无法转为整数形式，将引发异常
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public int getInt(int index);

	/**
	 * 以int形式返回指定索引index处的值<br>
	 * 此方法不会引发转换异常，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public int getInt(int index, int defaultValue);

	/**
	 * 以long形式返回指定索引index处的值<br>
	 * 如果该值无法转为整数形式，将引发异常
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public long getLong(int index);

	/**
	 * 以long形式返回指定索引index处的值<br>
	 * 此方法不会引发转换异常，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public long getLong(int index, long defaultValue);

	/**
	 * 以float形式返回指定索引index处的值<br>
	 * 如果该值无法转为整数形式，将引发异常
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public float getFloat(int index);

	/**
	 * 以float形式返回指定索引index处的值<br>
	 * 此方法不会引发转换异常，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public float getFloat(int index, float defaultValue);

	/**
	 * 以double形式返回指定索引index处的值<br>
	 * 如果该值无法转为整数形式，将引发异常
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public double getDouble(int index);

	/**
	 * 以double形式返回指定索引index处的值<br>
	 * 此方法不会引发转换异常，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public double getDouble(int index, double defaultValue);

	/**
	 * 以BigDecimal形式返回指定索引处的值<br>
	 * 如果指定的值为null或无法转为BigDecimal形式，将报错
	 * @param index 指定的索引，如果超出范围，将引发异常
	 * @return
	 */
	public BigDecimal getBigDecimal(int index);

	/**
	 * 以BigDecimal形式返回指定索引处的值<br>
	 * 如果指定的值为null或无法转为BigDecimal形式，将返回指定的<code>defaultValue</code>
	 * @param index 指定的索引，如果超出范围，将引发异常
	 * @param defaultValue 指定的默认值
	 * @return
	 */
	public BigDecimal getBigDecimal(int index, BigDecimal defaultValue);

	/**
	 * 以BigDecimal形式返回指定索引处的值<br>
	 * 如果指定的值为null或无法转为BigDecimal形式，将返回指定的<code>defaultValue</code>
	 * @param index 指定的索引，如果超出范围，将引发异常
	 * @param defaultValue 指定的可封装成BigDecimal字符串形式默认值(应当返回默认值时，如果无法有效的转为BigDecimal，将引发异常)
	 * @return
	 */
	public BigDecimal getBigDecimal(int index, String defaultValue);

	/**
	 * 判断指定索引index对应的值是否为空(null、空字符串、空格字符串),如果是，则返回true
	 * @param index 指定的索引，如果超出范围，将会引发异常
	 * @return
	 */
	public boolean isEmpty(int index);

	/**
	 * 判断指定索引index对应的值是否可以转为整数形式<br>
	 * 只有是整数形式即可，不区分byte、short、int和long。如果key不存在或无法转换将返回false
	 * @param index 如果指定的索引超出范围，将会引发异常
	 * @return
	 */
	public boolean isInt(int index);

	/**
	 * 判断指定索引index对应的值是否可以转为小数或整数形式<br>
	 * @param index 如果指定的索引超出范围，将会引发异常
	 * @return
	 */
	public boolean isDouble(int index);

	/**
	 * 判断指定索引index对应的值是否是数组类型<br>
	 * @param index 指定的索引，如果超出范围，将引发越界异常
	 * @return
	 */
	public boolean isArray(int index);

	/**
	 * 当指定索引index处的值等于(equals)oldValue时，将其替换为newValue，并返回对应的值<br>
	 * @param index 指定的索引，如果超出范围，将引发越界异常
	 * @param oldValue 符合条件的参数值
	 * @param newValue 符合条件时，替换后的值
	 */
	public E resetWhen(int index, E oldValue, E newValue);

	/**
	 * 当指定索引index处的值为null时，设置其值为newValue,并返回对应的值
	 * @param index 指定的索引，如果超出范围，将引发越界异常
	 * @param newValue 新值
	 */
	public E resetNull(int index, E newValue);

	/**
	 * 当指定索引index处的值为空对象时，重置其值为newValue,并返回对应的值<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象(去空格后) 为 空字符串<br>
	 * 2.其他对象==null
	 * @param index 指定的索引，如果超出范围，将引发越界异常
	 * @param newValue 新值
	 */
	public E resetEmpty(int index, E newValue);

	/**
	 * 当指定索引index处的值为空对象时，重置其值为newValue,并返回对应的值<br>
	 * 空对象obj的定义如下(按照判断顺序排序)： <br>
	 * 1.<code>obj == null</code><br>
	 * 2.如果<code>obj</code>是字符序列对象，去空格后，<code>length() == 0</code><br>
	 * 3.如果<code>obj</code>是数值对象，去空格后，<code>值  == 0</code><br>
	 * 4.如果<code>obj</code>是映射集合(Map)对象，<code>size() == 0</code><br>
	 * 5.如果<code>obj</code>是集合(Collection)对象，<code>size() == 0</code><br>
	 * 6.如果<code>obj</code>是数组(Array)对象，<code>length == 0</code><br>
	 * 7.如果<code>obj</code>是布尔(Boolean)对象，<code>obj 为  false</code><br>
	 * 8.满足上述任意情况均为true，其他情况为false
	 * @param index 指定的索引，如果超出范围，将引发越界异常
	 * @param newValue 新值
	 */
	public E resetBlank(int index, E newValue);
}
