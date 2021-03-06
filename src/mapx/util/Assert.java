package mapx.util;

import mapx.core.LogicException;

/**
 * 项目中的通用断言类，用于处理异常，如果断言失败将会抛出异常<br>
 * 断言方法均以is开头，相反的方法均以not开头<br>
 * 例如：isTrue和notTrue、isNull和notNull、isEmpty和notEmpty、isBlank和notBlank
 * @author Ready
 * @date 2012-4-23
 */
public class Assert {

	// 断言类均为静态方法，无需实例化
	private Assert() {}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 * @param expression boolean表达式
	 * @param message 异常消息内容
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new LogicException(message);
		}
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 * @param expression boolean表达式
	 * @param message 异常消息内容
	 */
	public static void notTrue(boolean expression, String message) {
		isTrue(!expression, message);
	}

	/**
	 * 断言指定对象为null<br>
	 * 如果断言失败则抛出异常
	 * @param object 指定对象
	 * @param message 异常消息内容
	 */
	public static void isNull(Object object, String message) {
		isTrue(object == null, message);
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 * @param object 指定对象
	 * @param message 异常消息内容
	 */
	public static void notNull(Object object, String message) {
		isTrue(object != null, message);
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see mapx.util.StringUtil#isEmpty(Object)
	 */
	public static void isEmpty(Object str, String message) {
		isTrue(StringUtil.isEmpty(str), message);
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see mapx.util.StringUtil#isEmpty(Object)
	 */
	public static void notEmpty(Object str, String message) {
		isTrue(!StringUtil.isEmpty(str), message);
	}

	/**
	 * 断言指定对象为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 * @param obj 指定对象
	 * @param message 异常消息内容
	 * @see mapx.util.StringUtil#isBlank(Object)
	 */
	public static void isBlank(Object obj, String message) {
		isTrue(StringUtil.isBlank(obj), message);
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 * @param obj 指定对象
	 * @param message 异常消息内容
	 * @see mapx.util.StringUtil#isBlank(Object)
	 */
	public static void notBlank(Object obj, String message) {
		isTrue(!StringUtil.isBlank(obj), message);
	}
}
