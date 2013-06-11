package mapx.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring辅助工具类，主要用于在任何地方获取Spring管理的Bean对象
 * @author Ready
 * @date 2012-08-18
 */
public class SpringUtil implements ApplicationContextAware {

	// Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取ApplicationContext对象
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取Spring管理的Bean对象
	 * @param name spring管理的bean的名称
	 * @return Object 返回Spring管理的bean实例，如果指定实例不存在将抛出异常
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}
}
