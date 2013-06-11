package mapx.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * JSON工具类，用于将Java对象转换为JS JSON对象形式的字符串
 * @author Ready
 * @date 2012-6-15
 */
public class JsonUtil {

	// 禁止实例创建
	private JsonUtil() {}
	/**
	 * 转义字符正则表达式，用于将字符串中的"替换为\"，将\替换为\\
	 */
	public static final Pattern pattern = Pattern.compile("(\"|\\\\)");
	/**
	 * 辅助正则表达式的替换字符串
	 */
	public static final String replacement = "\\\\$1";

	/**
	 * 将Java对象转为JSON格式字符串。<br>
	 * 目前只支持转换JavaBean、所有基本数据类型数组、所有对象类型数组、所有集合、所有Map集合以及可迭代对象。<br>
	 * 无法转换为JSON形式的将返回null，例如String、Integer、Date等。<br>
	 * 对象、数组或集合内部的java.util.Date将会直接调用toString方法，如需转换格式，请先转换后再传入<br>
	 * 内部会自动将字符串中的"或\进行转义，转义为\"或\\<br>
	 * 如果指定内容用于HTML输出，并可能存在歧义，请自行在Java或JavaScript中将<或>等特殊字符进行转义
	 * @param object
	 * @return
	 */
	public static String object2Json(Object object) {
		if (object == null || object instanceof Number || object instanceof CharSequence || object instanceof Character || object instanceof Boolean || object instanceof Date) {
			// 如果为null或常见的非JavaBean形式的对象
			return null;
		}
		StringBuilder sb = new StringBuilder(1 << 8);
		object2Json(sb, object);
		return sb.toString();
	}

	/**
	 * 此类内部使用的对象转JSON字符串方法
	 * @param sb
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	private static void object2Json(StringBuilder sb, Object object) {
		if (object == null || object instanceof Number || object instanceof Boolean) {
			sb.append(object); // 两段不用加双引号
		} else if (object instanceof CharSequence || object instanceof Character || object instanceof Date) {
			string2Json(sb, object.toString());
		} else if (object instanceof Collection) { // 如果属于一般集合
			array2Json(sb, ((Collection) object).toArray());
		} else if (object instanceof Map) { // 如果属于Map集合
			map2Json(sb, (Map) object);
		} else if (object.getClass().isArray()) { // 如果属于数组
			if (object instanceof Object[]) { // 如果是对象数组
				array2Json(sb, (Object[]) object);
			} else { // 如果是基础数据类型数组
				baseArray2Json(sb, object);
			}
		} else if (object instanceof Iterable) { // 如果属于可迭代对象
			Iterable iterable = (Iterable) object;
			Iterator iterator = iterable.iterator();
			List<Object> list = new ArrayList<Object>();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			array2Json(sb, list.toArray());
		} else { // JavaBean处理
			bean2Json(sb, object);
		}
	}

	/**
	 * 此类内部使用的将JavaBean对象转JSON字符串方法
	 * @param sb
	 * @param bean
	 */
	private static void bean2Json(StringBuilder sb, Object bean) {
		try {
			PropertyDescriptor[] propDescs = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
			if (propDescs != null) {
				sb.append('{');
				boolean notFirst = false;
				for (PropertyDescriptor desc : propDescs) {
					String name = desc.getName();
					if (!"class".equals(name)) { // 由于所有对象都自带一个class属性，因此要忽略此属性
						Method method = desc.getReadMethod();
						if (method != null) {
							if (notFirst) {
								sb.append(", ");
							} else {
								notFirst = true;
							}
							method.setAccessible(true);
							Object value = method.invoke(bean);
							string2Json(sb, name);
							sb.append(':');
							object2Json(sb, value);
						}
					}
				}
				sb.append('}');
			}
		} catch (IntrospectionException e) {
			sb.append("null");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此类内部使用的对象数组转JSON字符串方法
	 * @param sb
	 * @param array
	 */
	private static void array2Json(StringBuilder sb, Object[] array) {
		sb.append('[');
		int length = array.length;
		int count = 0;
		for (Object object : array) {
			count++;
			object2Json(sb, object);
			if (count < length) {
				sb.append(", ");
			}
		}
		sb.append(']');
	}

	/**
	 * 此类内部使用的Java基本数据类型数组转JSON字符串方法
	 * @param sb
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	private static void baseArray2Json(StringBuilder sb, Object object) {
		int length;
		if ((length = Array.getLength(object)) == 0) {
			sb.append("[]");
		} else {
			Class clazz = object.getClass();
			sb.append('[');
			if (clazz == int[].class) {
				int[] array = (int[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == double[].class) {
				double[] array = (double[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == long[].class) {
				long[] array = (long[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == byte[].class) {
				byte[] array = (byte[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == char[].class) {
				char[] array = (char[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == float[].class) {
				float[] array = (float[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == boolean[].class) {
				boolean[] array = (boolean[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			} else if (clazz == short[].class) {
				short[] array = (short[]) object;
				sb.append(array[0]);
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(array[i]);
				}
			}
			sb.append(']');
		}
	}

	/**
	 * 此类内部使用的字符串对象转JSON字符串方法
	 * @param sb
	 * @param str
	 */
	private static void string2Json(StringBuilder sb, String str) {
		sb.append('"').append(pattern.matcher(str).replaceAll(replacement)).append('"');
	}

	/**
	 * 此类内部使用的Map对象转JSON字符串方法
	 * @param sb
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private static void map2Json(StringBuilder sb, Map map) {
		sb.append('{');
		int size = map.size();
		int count = 0;
		Entry entry = null;
		for (Iterator<Entry> it = map.entrySet().iterator(); it.hasNext();) {
			entry = it.next();
			count++;
			string2Json(sb, entry.getKey().toString());
			sb.append(':');
			object2Json(sb, entry.getValue());
			if (count < size) {
				sb.append(", ");
			}
		}
		sb.append('}');
	}
}
