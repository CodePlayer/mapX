package mapx.jdbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import mapx.core.Config;
import mapx.util.X;

/**
 * 用于表示SQL执行相关参数的对象，主要用于作为缓存实体
 * @author Ready
 * @date 2012-11-28
 */
public class SQLBox {

	private static final Log log = X.getLog();
	// 用于保存缓存的SQL语句的静态Map
	private static final Map<String, SQLBox> SQL_CACHE = new ConcurrentHashMap<String, SQLBox>(Config.getCacheSize(), 0.75f, Config.getCacheSize());

	public SQLBox(String sql, String[] args) {
		this.sql = sql;
		this.args = args;
	}
	/**
	 * SQL语句
	 */
	final String sql;
	/**
	 * 执行SQL语句的预编译参数名数组
	 */
	final String[] args;

	/**
	 * 添加SQLBox缓存
	 * @param key
	 * @param value
	 */
	static final void addCache(String key, SQLBox value) {
		if (log.isDebugEnabled()) {
			log.debug("正在缓存SQL语句：[" + key + "]>>" + value.sql);
		}
		SQL_CACHE.put(key, value);
	}

	/**
	 * 获取SQLBox缓存
	 * @param key
	 * @return
	 */
	static final SQLBox getCache(String key) {
		SQLBox box = SQL_CACHE.get(key);
		if (log.isDebugEnabled()) {
			if (box == null) {
				log.debug("本次未使用SQL缓存");
			} else {
				log.debug("本次使用缓存的SQL：[" + key + "]>>" + box.sql);
			}
		}
		return box;
	}
}
