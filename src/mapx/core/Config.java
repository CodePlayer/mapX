package mapx.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import mapx.util.X;
import org.apache.commons.logging.Log;

/**
 * 核心配置类，主要读取src目录下的config.properties文件中的配置，如果读取失败，则使用内部的默认配置选项<br />
 * 目前支持的配置项如下：<br />
 * isDebug=true|false 调试开发模式选项
 * @author Ready
 * @date 2012-5-18
 */
public class Config {

	// 禁止实例创建
	private Config() {}
	private static final Log log = X.getLog();
	private static final HashMap<String, String> config = new HashMap<String, String>();
	private static char delimiter = '_'; // 参数的WHERE条件分割符
	private static int cacheSize = 1 << 10;// BeanProcessor用于缓存SQL语句的初始容量
	static {
		InputStream inputStream = Config.class.getResourceAsStream("/config.properties");
		if (inputStream != null) {
			Properties prop = new Properties();
			try {
				prop.load(inputStream);
				inputStream.close();
				System.out.println("读取到的config.properties文件配置如下：");
				for (Entry<Object, Object> entry : prop.entrySet()) {
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					config.put(key, value);
					System.out.println(key + "=" + value);
				}
				System.out.println("===============End===============");
				// 读取delimiter
				String tempDelimiter = config.get("delimiter");
				if (tempDelimiter != null) {
					tempDelimiter = X.trim(tempDelimiter);
					int length = tempDelimiter.length();
					if (length == 1) {
						delimiter = tempDelimiter.charAt(0);
					} else {
						System.err.println("配置参数有误，delimiter的值必须为1位长度，系统将采用默认值[_]。");
					}
				}
				// 读取cacheSize
				String size = config.get("cacheSize");
				int iSize = 0;
				if (size != null && (size = size.trim()).length() > 0) {
					try {
						iSize = Integer.parseInt(size);
					} catch (NumberFormatException e) {
						// 忽略此异常
					}
				}
				if (iSize == 0) {
					if (log.isDebugEnabled()) {
						log.debug("没有读取到cacheSize参数的有效配置，系统将使用默认值[" + cacheSize + "]。");
					}
				} else if (iSize < 16 || iSize > 4096) {
					if (log.isDebugEnabled()) {
						log.debug("cacheSize参数配置有误，该参数必须为大于16、小于4096的整数！系统将采用默认值[" + cacheSize + "]。");
					}
				} else {
					cacheSize = iSize;
				}
			} catch (IOException e) {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e1) {
					}
				}
				System.err.println("加载config.properties文件失败，该文件可能被其他程序占用！系统将使用默认配置！");
				e.printStackTrace();
			}
		} else {
			System.err.println("在src根目录下没有找到config.properties文件，系统将使用默认配置！");
		}
	}

	/**
	 * 获取config.properties配置文件中的所有配置，并以HashMap的形式返回其副本<br />
	 * 如果加载不到对应的配置文件，本方法仍然不会返回null，而是返回一个空的HashMap<br>
	 * <b>注意：</b>由于返回的只是一份拷贝的副本，因此，对返回的Map进行修改不会影响读取的配置
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getConfigMap() {
		return (HashMap<String, String>) config.clone();
	}

	/**
	 * 获取config.properties配置中指定属性的值，如果没有指定的属性，将返回null
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return config.get(key);
	}

	/**
	 * 获取处理系统请求参数的WHERE条件识别分割符(默认为"_")
	 * @return
	 */
	public static char getDelimiter() {
		return delimiter;
	}

	/**
	 * 获取系统配置的SQL语句缓存的容量大小
	 * @return
	 */
	public static int getCacheSize() {
		return cacheSize;
	}
}
