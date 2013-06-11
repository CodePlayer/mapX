package mapx.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import mapx.util.Assert;
import mapx.util.NumberUtil;
import mapx.util.StringUtil;
import mapx.util.X;

/**
 * 更加面向对象并集成常用操作的HashMap子类<br >
 * 此类主要用于充当JavaBean，并进行相关处理操作<br>
 * <strong>备注：</strong>如果要对当前对象进行数据库操作，
 * 可以使用BeanProcessor对象或者作为Action父类的ProcessorAction
 * @author Ready
 * @date 2012-10-4
 */
@SuppressWarnings("serial")
public class HashBean<K, V> extends HashMap<K, V> implements EnhanceMap<K, V> {

	// 默认的构造方法
	public HashBean() {
		super();
	}

	/**
	 * 构造一个指定实际可存储容量的Bean
	 * @param initialCapacity 实际可存储容量
	 */
	public HashBean(int initialCapacity) {
		super(X.getCapacity(initialCapacity));
	}

	/**
	 * 构造一个指定实际可存储容量、指定负载因子的Bean
	 * @param initialCapacity 指定实际可存储容量
	 * @param loadFactor 指定负载因子
	 */
	public HashBean(int initialCapacity, float loadFactor) {
		super(X.getCapacity(initialCapacity, loadFactor), loadFactor);
	}

	/**
	 * 构造一个Bean，并以指定的Map(<code>m</code>)进行填充
	 * @param m
	 */
	public HashBean(Map<? extends K, ? extends V> m) {
		super(m);
	}
	private HttpServletRequest request;

	/**
	 * 获取HttpServletRequest对象<br>
	 * <strong>警告：</strong>此方法只是为了方便获取HttpServletRequest对象，请不要在业务逻辑层或数据访问层随意调用此方法！
	 * @return
	 */
	public HttpServletRequest getRequest() {
		if (request == null) {
			request = ServletActionContext.getRequest();
		}
		return request;
	}

	/**
	 * 获取key所对应的value的字符串形式，如果指定的value为null，则返回空字符串""
	 * @param key
	 * @return
	 */
	public String getString(K key) {
		Object value = get(key);
		return value == null ? "" : value.toString();
	}

	/**
	 * 获取key所对应的value的去空格字符串形式<br>
	 * 如果key不存在或值=null时将返回空字符串""
	 * @param key
	 * @return
	 */
	public String getTrim(K key) {
		return StringUtil.trim(get(key));
	}

	/**
	 * 获取key所对应的value的去空格字符串形式<br>
	 * 如果key不存在或值=null时将返回空格html字符串"&amp;nbsp;"
	 * @param key
	 * @return
	 */
	public String getTrimHtml(K key) {
		return StringUtil.trim4Html(get(key));
	}

	/**
	 * 以String的形式返回指定的字符串，如果指定字符串(去空格后)超过限制长度maxLength,则返回限制长度前面的部分字符串
	 * 如果指定字符串==null，则返回空字符串
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 * @param key
	 * @param maxLength
	 * @return
	 */
	public String getLimit(K key, int maxLength) {
		return StringUtil.limitChars(getString(key), maxLength);
	}

	/**
	 * 以String的形式返回指定的字符串，如果指定字符串超过限制长度maxLength,则返回限制长度前面的部分字符串
	 * 如果指定字符串==null，则返回空字符串
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀suffix
	 * @param key 指定的字符串
	 * @param maxLength 最大限制长度
	 * @param suffix 超出长度时添加的指定后缀,如果不需要，可以为null
	 * @return
	 */
	public String getLimit(K key, int maxLength, String suffix) {
		return StringUtil.limitChars(getString(key), maxLength, suffix);
	}

	/**
	 * 获取key所对应的映射值，如果值为空(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值
	 * @param key 指定的key
	 * @param defaultValue 原映射为空时，用于返回的值
	 * @see mapx.util.X#isEmpty(Object)
	 * @return
	 */
	public V getDefault(K key, V defaultValue) {
		V oldValue = get(key);
		return X.isEmpty(oldValue) ? defaultValue : oldValue;
	}

	/**
	 * 获取key所对应的映射值，如果值为空对象(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值<br>
	 * <b>注意：</b>此方法与getDefault()方法不同的是：如果原值为空(<code>X.isEmpty(value) == true</code>)，该方法还会用默认值替换掉Bean中的原值
	 * @param key 指定的key
	 * @param defaultValue 原映射为空时，用于返回的值
	 * @see mapx.util.X#isEmpty(Object)
	 * @return
	 */
	public V defaultValue(K key, V defaultValue) {
		V oldValue = get(key);
		if (X.isEmpty(oldValue)) {
			put(key, defaultValue);
			return defaultValue;
		} else {
			return oldValue;
		}
	}

	/**
	 * 将key指定的映射值根据指定的表达式解析，并返回解析后的结果
	 * @param key 指定的key
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
	public V decode(K key, V... expressions) {
		return X.decodeValue(get(key), expressions);
	}

	/**
	 * 本方法与方法decode()具备一样的功能，不同的是，如果解析后的值与最初的映射值不相等(使用equals比较)，本方法还将会用解析后的值替换掉映射中原来的值
	 * @param key 指定的key
	 * @param expressions 指定的表达式
	 * @return
	 * @see mapx.core.HashBean#decode(Object, Object...)
	 */
	public V decodeReference(K key, V... expressions) {
		V value = get(key);
		V newValue = X.decodeValue(value, expressions);
		if (value == null && value != newValue || value != null && !value.equals(newValue)) {
			put(key, newValue);
		}
		return newValue;
	}

	/**
	 * 获取key所对应的value的去空格字符串形式<br>
	 * 如果key不存在将返回空字符串""<br>
	 * 与getTrim()不同的是：本方法不仅返回去空格后的字符串，还会将Map中对应的value去空格<br>
	 * 注意：字符串必须能够转为泛型V的类型，如果无法转换成功，调用此方法将引发类型转换异常
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String trim(K key) {
		String value = getTrim(key);
		put(key, (V) value);
		return value;
	}

	/**
	 * 获取key所对应的value的整数形式<br>
	 * 如果key不存在或无法转为整数形式，将报错
	 * @param key
	 * @return
	 */
	public int getInt(K key) {
		return NumberUtil.getInt(get(key));
	}

	/**
	 * 获取key所对应的value的整数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public int getInt(K key, int defaultValue) {
		return NumberUtil.getInt(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的long长整数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public long getLong(K key) {
		return NumberUtil.getLong(get(key));
	}

	/**
	 * 获取key所对应的value的long长整数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public long getLong(K key, long defaultValue) {
		return NumberUtil.getLong(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的float单精度小数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public float getFloat(K key) {
		return NumberUtil.getFloat(get(key));
	}

	/**
	 * 获取key所对应的value的float单精度小数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public float getFloat(K key, float defaultValue) {
		return NumberUtil.getFloat(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的double双精度小数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public double getDouble(K key) {
		return NumberUtil.getDouble(get(key));
	}

	/**
	 * 获取key所对应的value的double双精度小数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public double getDouble(K key, double defaultValue) {
		return NumberUtil.getDouble(get(key), defaultValue);
	}

	/**
	 * 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将引发异常
	 * @param key 指定的键
	 * @return
	 */
	public BigDecimal getBigDecimal(K key) {
		return NumberUtil.getBigDecimal(get(key));
	}

	/**
	 * 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将返回指定的默认值
	 * @param key 指定的键
	 * @param defaultValue 指定用于返回的默认值
	 * @return
	 */
	public BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
		return NumberUtil.getBigDecimal(get(key), defaultValue);
	}

	/**
	 * 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将返回指定的默认值
	 * @param key 指定的键
	 * @param defaultValue 指定用于返回的String形式的默认值，如果应该返回该值但无法正确转为BigDecimal对象时，将引发异常
	 * @return
	 */
	public BigDecimal getBigDecimal(K key, String defaultValue) {
		return NumberUtil.getBigDecimal(get(key), defaultValue);
	}

	/**
	 * 判断key值所对应的value是否为空(null、空字符串、空格字符串),如果是将返回true<br>
	 * 如果key不存在也返回true
	 * @param key
	 * @return
	 */
	public boolean isEmpty(K key) {
		return StringUtil.isEmpty(get(key));
	}

	/**
	 * 判断key值所对应的value是否可以转为整数形式<br>
	 * 只有是整数形式即可，不区分byte、short、int和long。如果key不存在或无法转换将返回false
	 * @param key
	 * @return
	 */
	public boolean isInt(K key) {
		Object value = get(key);
		return value != null && (value instanceof Integer || X.isNumber(value.toString()));
	}

	/**
	 * 判断key值所对应的value是否可以转为小数或整数形式<br>
	 * 如果key不存在或无法转换将返回false
	 * @param key
	 * @return
	 */
	public boolean isDouble(K key) {
		Object value = get(key);
		return value != null && X.isDouble(value.toString());
	}

	/**
	 * 判断指定key的映射值是否是数组类型<br>
	 * <b>警告：</b>前端传进来的请求参数，如果使用了ProcessorAction，请不要使用此方法来获取，因为Bean中的数组在映射中默认为字符串类型，其中的数组只能使用getStringArray等方法获取
	 * @param key
	 * @return
	 */
	public boolean isArray(K key) {
		Object value = get(key);
		return value != null && value.getClass().isArray();
	}

	/*
	 * 请求数组操作
	 */
	/**
	 * 获取Request中指定的参数值，并以字符串数组的形式返回
	 * @param key
	 * @return
	 */
	public String[] getStringArray(String key) {
		return getRequest().getParameterValues(key);
	}

	/**
	 * 获取Request中指定的参数值，并以整数数组的形式返回
	 * @param key
	 * @return
	 */
	public int[] getIntArray(String key) {
		String[] array = getStringArray(key);
		if (array == null) {
			return null;
		} else {
			int length = array.length;
			int[] values = new int[length];
			for (int i = 0; i < length; i++) {
				values[i] = Integer.parseInt(array[i]);
			}
			return values;
		}
	}

	/**
	 * 获取Request中指定的参数值，并以整数数组的形式返回<br>
	 * 如果无法转换将会报错
	 * @param key
	 * @return
	 */
	public long[] getLongArray(String key) {
		String[] array = getStringArray(key);
		if (array == null) {
			return null;
		} else {
			int length = array.length;
			long[] values = new long[length];
			for (int i = 0; i < length; i++) {
				values[i] = Long.parseLong(array[i]);
			}
			return values;
		}
	}

	/**
	 * 获取Request中指定的参数值，并以双精度浮点数数组的形式返回<br>
	 * 如果无法转换将会报错
	 * @param key
	 * @return
	 */
	public double[] getDoubleArray(String key) {
		String[] array = getStringArray(key);
		if (array == null) {
			return null;
		} else {
			int length = array.length;
			double[] values = new double[length];
			for (int i = 0; i < length; i++) {
				values[i] = Double.parseDouble(array[i]);
			}
			return values;
		}
	}

	/**
	 * 获取Request中指定的参数值，并以单精度浮点数数组的形式返回<br>
	 * 如果无法转换将会报错
	 * @param key
	 * @return
	 */
	public float[] getFloatArray(String key) {
		String[] array = getStringArray(key);
		if (array == null) {
			return null;
		} else {
			int length = array.length;
			float[] values = new float[length];
			for (int i = 0; i < length; i++) {
				values[i] = Float.parseFloat(array[i]);
			}
			return values;
		}
	}

	/*
	 * 扩展处理部分
	 */
	/**
	 * 批量删除指定键对应的映射
	 * @param keys 指定的可变参数形式的key数组，
	 */
	public void removeKeys(K... keys) {
		int size;
		if (keys != null && (size = size()) > 0) {
			do {
				remove(keys[--size]);
			} while (size > 0);
		}
	}

	/**
	 * 删除Map中除了指定元素外的所有元素(也就是只保留指定的参数)<br>
	 * Map中指定的key不存在时，默认赋值为null
	 * @param keys 指定元素key数组(可变参数)
	 */
	public void removeKeysExcept(K... keys) {
		if (keys.length > 0) {
			HashBean<K, V> temp = new HashBean<K, V>();
			for (K key : keys) {
				V value = this.get(key);
				temp.put(key, value);
			}
			this.clear();
			this.putAll(temp);
		}
	}

	/**
	 * 将参数名批量填充到Bean中，并设置统一的value值
	 * @param value 统一的默认值
	 * @param keys 参数名数组(可变参数)
	 */
	public void fillKeys(V value, K... keys) {
		for (K key : keys) {
			this.put(key, value);
		}
	}

	/**
	 * 将指定键src的映射值，以剪切的形式放入指定键dest的映射中
	 * @param src 指定的键
	 * @param dest 指定的目标键
	 * @return
	 */
	public V cut(K src, K dest) {
		V value = remove(src);
		put(dest, value);
		return value;
	}

	/**
	 * 将指定键src的映射值，以复制的形式放入指定键dest的映射中，并返回该值
	 * @param src 指定的键
	 * @param dest 指定的目标键
	 * @return
	 */
	public V copy(K src, K dest) {
		V value = get(src);
		put(dest, value);
		return value;
	}

	/**
	 * 用指定的key值替换Map中原有的key值(如果没有原有的key，则不替换)<br>
	 * 例如：原Map中存在key="uname" value="张三"，但是表中对应的字段为username， 则可以使用replaceKey("uname","username");将uname改为username
	 * @param oldKey 被替换的key
	 * @param newKey 指定的新key
	 */
	public void replaceKey(K oldKey, K newKey) {
		if (this.containsKey(oldKey)) {
			V value = this.get(oldKey);
			this.remove(oldKey);
			this.put(newKey, value);
		}
	}

	/**
	 * 强制验证指定参数不能为空。<br>
	 * <strong>注意：</strong>本方法传入的参数个数必须为大于0的偶数，否则报错！
	 * @param isStrict 验证是否严格标记，如果为true，发现为空就报错，并返回null； 如果为false，则循环验证，并返回所有报错的信息字符串数组(字符串内容为"XX不能为空！")
	 * @param expression expression 非空验证表达式字符串数组(可变参数)，形式如："username","用户名","password","密码"
	 */
	@SuppressWarnings("unchecked")
	public String[] checkRequire(boolean isStrict, String... expression) {
		int length = expression.length;
		Assert.isTrue(length > 0 && length % 2 == 0, "非空验证参数表达式不正确！");
		List<String> errors = new ArrayList<String>(length >> 1);
		for (int i = 0; i < length; i += 2) {
			if (this.isEmpty((K) expression[i])) {
				String error = new StringBuilder(expression[i + 1]).append("不能为空！").toString();
				if (isStrict) { // 严格模式，直接抛出异常
					throw new LogicException(error);
				} else {
					errors.add(error);
				}
			}
		}
		return isStrict ? null : (String[]) errors.toArray();
	}

	/**
	 * 强制验证指定参数的value不能为空。若为空则抛出"XX不能为空！"的异常信息<br>
	 * <strong>注意：</strong>本方法传入的参数个数必须为大于0的偶数，否则报错！
	 * @param expression 非空验证表达式字符串数组(可变参数)，例如："username","用户名","password","密码"
	 */
	public void checkRequire(String... expression) {
		this.checkRequire(true, expression);
	}

	/**
	 * 强制验证指定参数的value是否为整数类型。若不为整数则抛出"XX必须为整数形式！"的异常信息<br>
	 * <strong>注意：</strong>本方法传入的参数个数必须为大于0的偶数，否则报错！
	 * @param isStrict 验证是否严格标记，如果为true，发现为空就报错，并返回null； 如果为false，则循环验证，并返回所有报错的信息字符串数组(字符串内容为"XX必须为整数形式！")
	 * @param expression 数字格式验证表达式字符串数组(可变参数)，例如："age","年龄","money","金额"
	 */
	@SuppressWarnings("unchecked")
	public String[] checkNumber(boolean isStrict, String... expression) {
		int length = expression.length;
		Assert.isTrue(length > 0 && length % 2 == 0, "非空验证参数表达式不正确！");
		List<String> errors = new ArrayList<String>(length >> 1);
		for (int i = 0; i < length; i += 2) {
			if (this.isInt((K) expression[i])) {
				String error = new StringBuilder(expression[i + 1]).append("必须为数字形式！").toString();
				if (isStrict) { // 严格模式，直接抛出异常
					throw new LogicException(error);
				} else {
					errors.add(error);
				}
			}
		}
		return isStrict ? null : (String[]) errors.toArray();
	}

	/**
	 * 强制验证指定参数的value是否为整数类型。若不为整数则抛出"XX必须为整数形式！"的异常信息<br>
	 * <strong>注意：</strong>本方法传入的参数个数必须为大于0的偶数，否则报错！
	 * @param expression 数字格式验证表达式字符串数组(可变参数)，例如："age","年龄","money","金额"
	 */
	public void checkNumber(String... expression) {
		this.checkNumber(true, expression);
	}

	/**
	 * 当参数key的值等于oldValue时，将其替换为newValue，如果符合替换条件将返回true<br>
	 * @param key 指定的key
	 * @param oldValue 符合条件的参数值
	 * @param newValue 符合条件时，替换后的值
	 */
	public V resetWhen(K key, V oldValue, V newValue) {
		V value = this.get(key);
		if (value == null && oldValue == null) {
			this.put(key, newValue);
			return newValue;
		} else if (value != null) {
			if (value.equals(oldValue)) {
				this.put(key, newValue);
				return newValue;
			}
		} else if (oldValue != null) {
			if (oldValue.equals(value)) {
				this.put(key, newValue);
				return newValue;
			}
		}
		return value;
	}

	/**
	 * 当指定key的映射值为null时，设置其值为newValue,如果符合重置条件，将返回true
	 * @param key 指定的key
	 * @param newValue 新值
	 */
	public V resetNull(K key, V newValue) {
		V value = get(key);
		if (value == null) {
			put(key, newValue);
			return newValue;
		}
		return value;
	}

	/**
	 * 当指定key的映射值为空对象时，重置其值为newValue,如果符合重置条件，将返回true<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象(去空格后)==空字符串<br>
	 * 2.其他对象==null
	 * @param key 指定的key
	 * @param newValue 新值
	 */
	public V resetEmpty(K key, V newValue) {
		V value = get(key);
		if (X.isEmpty(value)) {
			put(key, newValue);
			return newValue;
		}
		return value;
	}

	/**
	 * 当指定key的映射值为空对象时，重置其值为newValue,如果符合重置条件，将返回true<br>
	 * 空对象obj的定义如下(按照判断顺序排序)： <br>
	 * 1.<code>obj == null</code><br>
	 * 2.如果<code>obj</code>是字符序列对象，去空格后，<code>length() == 0</code><br>
	 * 3.如果<code>obj</code>是数值对象，去空格后，<code>值  == 0</code><br>
	 * 4.如果<code>obj</code>是映射集合(Map)对象，<code>size() == 0</code><br>
	 * 5.如果<code>obj</code>是集合(Collection)对象，<code>size() == 0</code><br>
	 * 6.如果<code>obj</code>是数组(Array)对象，<code>length == 0</code><br>
	 * 7.如果<code>obj</code>是布尔(Boolean)对象，<code>obj 为  false</code><br>
	 * 8.满足上述任意情况均为true，其他情况为false
	 * @param key 指定的key
	 * @param newValue 新值
	 */
	public V resetBlank(K key, V newValue) {
		V value = get(key);
		if (X.isBlank(value)) {
			put(key, newValue);
			return newValue;
		}
		return value;
	}
}
