package mapx.util;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;

/**
 * session操作工具类
 * @author Ready
 * @date 2012-9-27
 */
public class SessionUtil {

	// 禁止实例创建
	private SessionUtil() {}
	/**
	 * 存储用户数据到session作用域中的键
	 */
	public static final String USER = "user";

	/**
	 * 存储指定键值的数据到session作用域中
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(key, value);
	}

	/**
	 * 从session作用域中存储指定键的数据
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		return session.get(key);
	}

	/**
	 * 存储用户对象数据到session作用域中
	 * @param value
	 */
	public static void setUser(Object value) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(USER, value);
	}

	/**
	 * 从session作用域中取对应的用户对象数据
	 * @return
	 */
	public static Object getUser() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		return session.get(USER);
	}

	/**
	 * 清除Session作用域中的用户数据，以完成用户退出操作
	 */
	public static void removeUser() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.remove(USER);
	}
}
