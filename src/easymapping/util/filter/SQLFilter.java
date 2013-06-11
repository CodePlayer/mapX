package easymapping.util.filter;

import easymapping.core.Config;

/**
 * 用于处理用于WHERE子句的请求参数的过滤器接口
 * @author Ready
 * @date 2012-12-8
 */
public interface SQLFilter {

	/**
	 * 指定的请求参数过滤分隔符
	 */
	char delimiter = Config.getDelimiter();

	/**
	 * 过滤指定的参数后缀，并返回处理后的结果
	 * @param realKey 真实的参数名称，已过滤掉后缀
	 * @param value 该值已经经过判断，不会为null，或空字符串(length() == 0)
	 * @return
	 */
	Entry filter(String realKey, Object value);
}
