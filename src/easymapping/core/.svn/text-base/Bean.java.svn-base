package easymapping.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import easymapping.util.ArrayUtil;
import easymapping.util.EasyDate;
import easymapping.util.NumberUtil;
import easymapping.util.StringUtil;
import easymapping.util.X;

/**
 * 更加面向对象并集成常用操作的HashMap子类<br >
 * 此类主要用于充当JavaBean，并进行相关处理操作<br>
 * <strong>备注：</strong>如果要对当前对象进行数据库操作，
 * 可以使用BeanProcessor对象或者作为Action父类的ProcessorAction
 * @author Ready
 * @date 2012-10-4
 */
@SuppressWarnings("serial")
public class Bean extends LinkedHashMap<String, Object> {

	private String prefix;

	// 默认的构造方法
	public Bean() {
		super();
	}

	/**
	 * 构造一个指定初始化容量的Bean
	 * @param initialCapacity 实际可存储容量
	 */
	public Bean(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 构造一个指定初始化容量、指定负载因子的Bean
	 * @param initialCapacity 实际可存储容量
	 * @param loadFactor 指定负载因子
	 */
	public Bean(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * 构造一个Bean，并以指定的Map(<code>m</code>)进行填充
	 * @param m
	 */
	public Bean(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	/**
	 * 构造一个指定初始化容量、指定负载因子、指定排序模式的Bean
	 * @param initialCapacity 实际可存储容量
	 * @param loadFactor 指定负载因子
	 * @param accessOrder 指定排序模式，true=按访问顺序排序，false=按插入顺序排序
	 */
	public Bean(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	/**
	 * 根据指定请求对象中的参数，将参数名为指定前缀的值加入Bean中
	 * @param request 指定的HttpServletRequest 
	 * @param prefix 指定的参数名前缀，如果不指定，可以为null
	 */
	@SuppressWarnings("unchecked")
	public Bean(HttpServletRequest request, String prefix) {
		super();
		this.prefix = prefix;
		Enumeration<String> e = request.getParameterNames();
		boolean noPrefix = prefix == null;
		int prefixIndex = noPrefix ? 0 : prefix.length();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			if (noPrefix || name.startsWith(prefix)) {
				String[] values = request.getParameterValues(name);
				if (prefixIndex > 0) {
					name = name.substring(prefixIndex);
				}
				if (values == null) {
					continue;
				} else if (values.length == 1) {
					put(name, values[0]);
				} else {
					put(name, values);
				}
			}
		}
	}

	/**
	 * 根据指定请求对象中的参数，将参数名为指定前缀的值加入Bean中
	 * @param request 指定的HttpServletRequest
	 */
	public Bean(HttpServletRequest request) {
		this(request, null);
	}

	/**
	 * 创建具备指定可存储容量的Bean(使用默认负载因子0.75)<br>
	 * 本方法会提供最适当的空间，使bean在存放元素到指定的个数之前不会发生扩容操作
	 * @param capacity 指定实际可存储容量
	 * @return
	 */
	public static Bean create(int capacity) {
		int initCapacity = X.getCapacity(capacity);
		return initCapacity > 16 ? new Bean(initCapacity) : new Bean();
	}

	/**
	 * 获取key所对应的value的字符串形式，如果指定的值为null，则返回空字符串""
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return StringUtil.toString(get(key));
	}

	/**
	 * 获取key所对应的value的去空格字符串形式<br>
	 * 如果key不存在或值=null时将返回空字符串""
	 * @param key
	 * @return
	 */
	public String getTrim(String key) {
		return StringUtil.trim(get(key));
	}

	/**
	 * 获取key所对应的value的去空格字符串形式<br>
	 * 如果key不存在或值=null或空格字符串，则返回"&amp;nbsp;"
	 * @param key
	 * @return
	 */
	public String getTrimHtml(String key) {
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
	public String getLimit(String key, int maxLength) {
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
	public String getLimit(String key, int maxLength, String suffix) {
		return StringUtil.limitChars(getString(key), maxLength, suffix);
	}

	/**
	 * 获取key所对应的映射值，如果值为空(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值
	 * @param key 指定的key
	 * @param defaultValue 原映射为空时，用于返回的值
	 * @return
	 * @see easymapping.util.X#isEmpty(Object)
	 */
	public Object getDefault(String key, Object defaultValue) {
		return X.getDefault(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的映射值，如果值为空对象(<code>X.isEmpty(value) == true</code>)，则返回指定的默认值<br>
	 * <b>注意：</b>此方法与getDefault()方法不同的是：如果原值为空(<code>X.isEmpty(value) == true</code>)，该方法还会用默认值替换掉Bean中的原值
	 * @param key 指定的key
	 * @param defaultValue 原映射为空时，用于返回的值
	 * @return
	 * @see easymapping.util.StringUtil#isEmpty(Object)
	 */
	public Object defaultValue(String key, Object defaultValue) {
		Object oldValue = get(key);
		if (StringUtil.isEmpty(oldValue)) {
			put(key, defaultValue);
			return defaultValue;
		} else {
			return oldValue;
		}
	}

	/**
	 * 将指定参数名的值从request中放入Map中，如果参数不存在则不放入，并返回本次成功放入Map中的有效参数个数
	 * @param request 指定的HttpServletRequest
	 * @param keys 指定的参数名数组
	 * @return
	 * @see javax.servlet.ServletRequest#getParameter(String)
	 */
	public int putKeys(HttpServletRequest request, String... keys) {
		int size = 0;
		int length = ArrayUtil.getLength(keys, true);
		String value = null;
		do {
			value = request.getParameter(keys[--length]);
			if (value != null) {
				put(keys[length], value);
				size++;
			}
		} while (length > 0);
		return size;
	}

	/**
	 * 将指定的key的映射转为java.sql.Date类型，如果指定的映射不为Empty，则进行转换，如果为Empty，则不转换<br>
	 * 转换时使用 EasyDate.smartParse(String)进行转换，并返回本次成功转换的参数个数
	 * @param keys 指定的key数组
	 * @return
	 * @see easymapping.util.StringUtil#isEmpty(String)
	 * @see easymapping.util.EasyDate#smartParse(String)
	 */
	public int formatSqlDate(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		int size = 0;
		String dateStr = null;
		do {
			dateStr = getString(keys[--length]);
			if (!StringUtil.isEmpty(dateStr)) {
				put(keys[length], EasyDate.smartParse(dateStr).toSqlDate());
				size++;
			}
		} while (length > 0);
		return size;
	}

	/**
	 * 将指定参数值数组使用指定格式转换器依次转为java.util.Date类型，如果指定的映射不为Empty，则进行转换，如果为Empty，则不转换<br>
	 * 转换时使用 SimpleDateFormat进行转换，并返回本次成功转换的参数个数
	 * @param format
	 * @param keys
	 * @return
	 * @see easymapping.util.StringUtil#isEmpty(String)
	 * @see java.text.SimpleDateFormat#parse(String)
	 */
	public int formatDate(String format, String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		int size = 0;
		DateFormat dateFormat = new SimpleDateFormat(format);
		String dateStr = null;
		do {
			dateStr = getString(keys[--length]);
			if (!StringUtil.isEmpty(dateStr)) {
				try {
					put(keys[length], dateFormat.parse(keys[length]));
				} catch (ParseException e) {
					throw new LogicException("将字符串转为java.util.Date时发生异常！", e);
				}
			}
		} while (length > 0);
		return size;
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
	 * @see easymapping.util.X#decodeValue(Object, Object...)
	 */
	public Object decode(String key, Object... expressions) {
		return X.decodeValue(get(key), expressions);
	}

	/**
	 * 本方法与方法decode()具备一样的功能，不同的是，如果解析后的值与最初的映射值不相等(使用equals比较)，本方法还将会用解析后的值替换掉映射中原来的值
	 * @param key 指定的key
	 * @param expressions 指定的表达式
	 * @return
	 * @see easymapping.core.Bean#decode(Object, Object...)
	 */
	public Object decodeReference(String key, Object... expressions) {
		Object value = get(key);
		Object newValue = X.decodeValue(value, expressions);
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
	public String trim(String key) {
		String value = getTrim(key);
		put(key, value);
		return value;
	}

	/**
	 * 获取key所对应的value的整数形式<br>
	 * 如果key不存在或无法转为整数形式，将报错
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return NumberUtil.getInt(get(key));
	}

	/**
	 * 获取key所对应的value的整数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public int getInt(String key, int defaultValue) {
		return NumberUtil.getInt(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的long长整数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return NumberUtil.getLong(get(key));
	}

	/**
	 * 获取key所对应的value的long长整数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public long getLong(String key, long defaultValue) {
		return NumberUtil.getLong(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的float单精度小数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return NumberUtil.getFloat(get(key));
	}

	/**
	 * 获取key所对应的value的float单精度小数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		return NumberUtil.getFloat(get(key), defaultValue);
	}

	/**
	 * 获取key所对应的value的double双精度小数形式<br>
	 * 如果key不存在或无法转为小数形式，将报错
	 * @param key
	 * @return
	 */
	public double getDouble(String key) {
		return NumberUtil.getDouble(get(key));
	}

	/**
	 * 获取key所对应的value的double双精度小数形式<br>
	 * 此方法不会报错，如果无法正确取得对应的值将会返回指定的defaultValue
	 * @param key
	 * @return
	 */
	public double getDouble(String key, double defaultValue) {
		return NumberUtil.getDouble(get(key), defaultValue);
	}

	/**
	* 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将引发异常
	* @param key 指定的键
	* @return
	*/
	public BigDecimal getBigDecimal(String key) {
		return NumberUtil.getBigDecimal(get(key));
	}

	/**
	 * 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将返回指定的默认值
	 * @param key 指定的键
	 * @param defaultValue 指定用于返回的默认值
	 * @return
	 */
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return NumberUtil.getBigDecimal(get(key), defaultValue);
	}

	/**
	 * 以BigDecimal对象的形式返回指定key的值，如果对应的值为null或无法正确转为BigDecimal类型，将返回指定的默认值
	 * @param key 指定的键
	 * @param defaultValue 指定用于返回的String形式的默认值，如果应该返回该值但无法正确转为BigDecimal对象时，将引发异常
	 * @return
	 */
	public BigDecimal getBigDecimal(String key, String defaultValue) {
		return NumberUtil.getBigDecimal(get(key), defaultValue);
	}

	/**
	 * 以java.util.Date返回指定的参数值<br>
	 * 参数值仅支持java.sql.Date、java.util.Date以及可以转为日期的字符串类型
	 * @param key 指定的参数名
	 * @return
	 * @see  easymapping.util.EasyDate#smartParse(String)
	 */
	public java.util.Date getDate(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		} else if (value instanceof java.util.Date) {
			return (java.util.Date) value;
		} else {
			return EasyDate.smartParse(value.toString()).toDate();
		}
	}

	/**
	 * 以java.sql.Date返回指定的参数值<br>
	 * 参数值仅支持java.sql.Date、java.util.Date以及可以转为日期的字符串类型
	 * @param key 指定的参数名
	 * @return
	 * @see  easymapping.util.EasyDate#smartParse(String)
	 */
	public Date getSqlDate(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		} else if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof java.util.Date) {
			return new Date(((java.util.Date) value).getTime());
		} else {
			return EasyDate.smartParse(value.toString()).toSqlDate();
		}
	}

	/**
	 * 将指定的对象转为EasyDate对象<br>
	 * 支持java.util.Date、java.sql.Date、EasyDate、Calendar、字符串等类型转为EasyDate<br>
	 * 如果指定参数值为null，则返回null
	 * @param key 指定的参数名
	 * @return
	 * @see easymapping.util.EasyDate#valueOf(Object)
	 */
	public EasyDate getEasyDate(String key) {
		return EasyDate.valueOf(get(key));
	}

	/**
	 * 将指定的参数值使用指定的格式化字符串转换为日期对象<br>
	 * 如果指定参数字符串为空，则返回null
	 * @param key 指定的参数名
	 * @param format 指定的格式化字符串
	 * @return
	 * @see easymapping.util.EasyDate#parse(String, String)
	 */
	public EasyDate getEasyDate(String key, String format) {
		String date = getString(key);
		if (date.length() == 0) {
			return null;
		}
		return EasyDate.parse(format, date);
	}

	/**
	 * 判断key值所对应的value是否为空(null、空字符串),如果是将返回true<br>
	 * 如果key不存在也返回true
	 * @param key
	 * @return
	 */
	public boolean isEmpty(String key) {
		return StringUtil.isEmpty(get(key));
	}

	/**
	 * 判断指定key值数组所对应的value是否为空(null、空字符串),如果是将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true
	 * 如果指定的key不存在也返回true
	 * @param keys 指定的key数组
	 * @return
	 * @see  easymapping.util.StringUtil#isEmpty(Object)
	 */
	public boolean hasEmpty(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (StringUtil.isEmpty(get(keys[--length]))) {
				return true;
			}
		} while (length > 0);
		return false;
	}

	/**
	 * 判断key值所对应的value是否为空(null、空字符串、空格字符串),如果是将返回true<br>
	 * 如果key不存在也返回true
	 * @param key
	 * @return
	 */
	public boolean isBlank(String key) {
		return StringUtil.isBlank(get(key));
	}

	/**
	 * 判断指定key值数组所对应的value是否为空(null、空字符串、空格字符串),如果是将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true<br>
	 * 本方法会去除两边的空格后再判断
	 * 如果指定的key不存在也返回true
	 * @param keys 指定的key数组
	 * @return
	 * @see  easymapping.util.StringUtil#isBlank(Object)
	 */
	public boolean hasBlank(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (StringUtil.isBlank(get(keys[--length]))) {
				return true;
			}
		} while (length > 0);
		return false;
	}

	/**
	 * 判断key值所对应的value是否可以转为整数形式<br>
	 * 只有是整数形式即可，不区分byte、short、int和long。如果key不存在或无法转换将返回false<br>
	 * <b>注意：</b>只有所有的参数判断都返回true时，结果才为true
	 * @param keys 指定的key数组
	 * @return
	 * @see easymapping.util.NumberUtil#isInt(Object)
	 */
	public boolean isInt(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (!NumberUtil.isInt(get(keys[--length]))) {
				return false;
			}
		} while (length > 0);
		return true;
	}

	/**
	 * 判断key值所对应的value是否可以转为小数或整数形式<br>
	 * 如果key不存在或无法转换将返回false<br>
	 * <b>注意：</b>只有所有的参数判断都返回true时，结果才为true
	 * @param keys 指定的key数组
	 * @return
	 * @see easymapping.util.NumberUtil#isDouble(Object)
	 */
	public boolean isDouble(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (!NumberUtil.isDouble(get(keys[--length]))) {
				return false;
			}
		} while (length > 0);
		return true;
	}

	/**
	 * 判断指定key的映射值是否是数组类型<br>
	 * @param key
	 * @return
	 */
	public boolean isArray(String key) {
		return ArrayUtil.isArray(get(key));
	}

	/*
	 * 请求数组操作
	 */
	/**
	 * 获取Request中指定的参数值，并以字符串数组的形式返回<br>
	 * <b>注意：</b>本方法只用于处理Http请求，如果Bean中指定参数为字符串数组或字符串类型的，也可以使用本方法处理，不支持其他类型
	 * @param key
	 * @return
	 */
	public String[] getStringArray(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return new String[] { (String) value };
		} else {
			return (String[]) value;
		}
	}

	/**
	 * 获取Request中指定的参数值，并以整数数组的形式返回<br>
	 * <b>注意：</b>本方法只用于处理Http请求，如果Bean中指定参数为字符串数组或字符串类型的，也可以使用本方法处理，不支持其他类型
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
	 * 如果无法转换将会报错<br>
	 * <b>注意：</b>本方法只用于处理Http请求，如果Bean中指定参数为字符串数组或字符串类型的，也可以使用本方法处理，不支持其他类型
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
	 * 如果无法转换将会报错<br>
	 * <b>注意：</b>本方法只用于处理Http请求，如果Bean中指定参数为字符串数组或字符串类型的，也可以使用本方法处理，不支持其他类型
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
	 * 如果无法转换将会报错<br>
	 * <b>注意：</b>本方法只用于处理Http请求，如果Bean中指定参数为字符串数组或字符串类型的，也可以使用本方法处理，不支持其他类型
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
	public void removeKeys(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			remove(keys[--length]);
		} while (length > 0);
	}

	/**
	 * 删除Map中除了指定元素外的所有元素(也就是只保留指定的参数)<br>
	 * Map中指定的key不存在时，默认赋值为null
	 * @param keys 指定元素key数组(可变参数)
	 */
	public void removeKeysExcept(String... keys) {
		int length;
		if (keys == null || (length = keys.length) == 0) {
			throw new LogicException("无效的方法参数，必须指定有效的数组参数！");
		} else {
			Bean temp = new Bean(length);
			do {
				Object value = get(keys[--length]);
				temp.put(keys[length], value);
			} while (length > 0);
			this.clear();
			this.putAll(temp);
		}
	}

	/**
	 * 将参数名批量填充到Bean中，并设置统一的value值
	 * @param value 统一的默认值
	 * @param keys 参数名数组(可变参数)
	 */
	public void fillKeys(Object value, String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			put(keys[--length], value);
		} while (length > 0);
	}

	/**
	 * 将指定键src的映射值，以剪切的形式放入指定键dest的映射中<br>
	 * 如果src不存在，本方法仍然会在dest中存放一个null值
	 * @param src 指定的键
	 * @param dest 指定的目标键
	 * @return
	 */
	public Object cut(String src, String dest) {
		Object value = remove(src);
		put(dest, value);
		return value;
	}

	/**
	 * 将指定键src的映射值，以复制的形式放入指定键dest的映射中，并返回该值<br>
	 * 如果src不存在，本方法仍然会在dest中存放一个null值
	 * @param src 指定的键
	 * @param dest 指定的目标键
	 * @return
	 */
	public Object copy(String src, String dest) {
		Object value = get(src);
		put(dest, value);
		return value;
	}

	/**
	 * 检测指定key数组对应的参数值数组中是否存在空值(null、空字符串)，如果是，则返回值为空的key<br>
	 * 本方法不会去除两边的空格后再判断，如需去空格，请调用checkBlank方法
	 * @param keys 指定的参数名数组
	 * @return
	 */
	public void checkEmpty(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (isEmpty(keys[--length])) {
				throw new LogicException("无效的参数：" + keys[length]);
			}
		} while (length > 0);
	}

	/**
	 * 检测指定key数组对应的参数值数组中是否存在空值(null、空字符串、空格字符串)，如果是，则返回值为空的key
	 * @param callException 是否直接引发异常，如果为是，则直接抛出异常，内容为：“无效的参数：key”
	 * @param keys 指定的参数名数组
	 * @return
	 */
	public void checkBlank(String... keys) {
		int length = ArrayUtil.getLength(keys, true);
		do {
			if (isBlank(keys[--length])) {
				throw new LogicException("无效的参数：" + keys[length]);
			}
		} while (length > 0);
	}

	/**
	 * 用指定的key值替换Map中原有的key值(如果没有原有的key，则不替换)<br>
	 * 例如：原Map中存在key="uname" value="张三"，但是表中对应的字段为username， 则可以使用replaceKey("uname","username");将uname改为username
	 * @param oldKey 被替换的key
	 * @param newKey 指定的新key
	 */
	public void replaceKey(String oldKey, String newKey) {
		if (this.containsKey(oldKey)) {
			Object value = this.get(oldKey);
			this.remove(oldKey);
			this.put(newKey, value);
		}
	}

	/**
	 * 当参数key的值等于oldValue时，将其替换为newValue，如果符合替换条件将返回true<br>
	 * @param key 指定的key
	 * @param oldValue 符合条件的参数值
	 * @param newValue 符合条件时，替换后的值
	 */
	public Object resetWhen(String key, Object oldValue, Object newValue) {
		Object value = this.get(key);
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
	public Object resetNull(String key, Object newValue) {
		Object value = get(key);
		if (value == null) {
			put(key, newValue);
			return newValue;
		}
		return value;
	}

	/**
	 * 当指定key的映射值为空对象时，重置其值为newValue,如果符合重置条件，将返回true<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象==null或空字符串<br>
	 * 2.其他对象==null
	 * @param key 指定的key
	 * @param newValue 新值
	 */
	public Object resetEmpty(String key, Object newValue) {
		Object value = get(key);
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
	public Object resetBlank(String key, Object newValue) {
		Object value = get(key);
		if (X.isBlank(value)) {
			put(key, newValue);
			return newValue;
		}
		return value;
	}

	/**
	 * 返回当前Bean生成的URL参数前缀，如果没有，则为null
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 设置当前Bean生成的URL参数前缀
	 * @param prefix 
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
