package easymapping.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.struts2.ServletActionContext;
import easymapping.core.HashBean;
import easymapping.core.LogicException;

/**
 * HTTP请求辅助工具类
 * @author Ready
 * @date 2012-10-29
 */
public class HttpUtil {

	// 禁止实例创建
	private HttpUtil() {}
	private static final Log log = X.getLog();
	public static final int URL_ONLY = 1;
	public static final int PARAM_ONLY = 2;
	public static final int ATTR_ONLY = 4;
	public static final int ALL = 7;
	/**
	 * 默认编码UTF-8
	 */
	private static final String defaultEncoding = "UTF-8";

	/**
	 * 将请求中以指定前缀开头的参数以指定编码封装为URL参数字符串
	 * @param request 指定的HttpServletRequest
	 * @param hasParameter 指示是否已经有URL参数，如果已经有参数，将返回以“&”开头的字符串，如果没有参数，将返回以“?”开头的字符串
	 * @param srcPrefix 指定的前缀，程序只将以指定前缀开头的参数添加到URL中，如果没有前缀，可以为null
	 * @param encoding 指定URL编码,如果不指定，将使用当前request请求的编码
	 * @param destPrefix 目标的key目标前缀，如果该值不为null，则生成的URL中所有参数均以此为前缀，例如目标前缀为“item.”，则生成的URL类似“&item.name=zhang&item.age18”或“?item.name=zhang&item.age18”
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final String addURLParam(HttpServletRequest request, boolean hasParameter, String srcPrefix, String encoding, String destPrefix) {
		if (encoding == null) {
			encoding = request.getCharacterEncoding();
		}
		Map<String, Object> argsMap = request.getParameterMap();
		return addURLParam(null, argsMap, srcPrefix, encoding, destPrefix);
	}

	/**
	 * 将请求中以指定前缀开头的参数以指定编码封装为URL参数字符串
	 * @param url 指定的URL，此值不为null时，如果URL中有'?'，则追加以'&'开头的参数，如果没有，则追加以'?'开头的参数。 <b>因此，请不要在URL结尾处自行添加'?'或'&'</b>
	 * @param request 指定的HttpServletRequest
	 * @param filterPrefix 指定的前缀，程序只将以指定前缀开头的参数添加到URL中，如果没有前缀，可以为null
	 * @param destPrefix 目标的key目标前缀，如果该值不为null，则生成的URL中所有参数均以此为前缀，例如目标前缀为“item.”，则生成的URL类似“&item.name=zhang&item.age18”或“?item.name=zhang&item.age18”
	 * @param encoding 指定URL编码,如果不指定，将使用当前request请求的CHARSET编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final String addURLParam(CharSequence url, HttpServletRequest request, int parameterMode, String filterPrefix, String destPrefix, String encoding) {
		if (encoding == null) {
			encoding = request.getCharacterEncoding();
		}
		Map<String, String[]> argsMap = request.getParameterMap();
		return addURLParam(url, argsMap, filterPrefix, destPrefix, encoding);
	}

	/**
	 * 将请求中以指定前缀开头的参数以指定编码封装为URL参数字符串
	 * @param request 指定的HttpServletRequest
	 * @param hasParameter 指示是否已经有URL参数，如果已经有参数，将返回以“&”开头的字符串，如果没有参数，将返回以“?”开头的字符串
	 * @param srcPrefix 指定的前缀，程序只将以指定前缀开头的参数添加到URL中，如果没有前缀，可以为null
	 * @param encoding 指定URL编码,如果不指定，将使用当前request请求的编码
	 * @return
	 */
	public static final String addURLParam(HttpServletRequest request, boolean hasParameter, String srcPrefix, String encoding) {
		return addURLParam(request, hasParameter, srcPrefix, encoding, null);
	}

	/**
	 * 将请求中以指定前缀开头的参数以指定编码封装为URL参数字符串<br>
	 * 编码使用request.getCharacterEncoding()获取的编码
	 * @param request 指定的HttpServletRequest
	 * @param hasParameter 指示是否已经有URL参数，如果已经有参数，将返回以“&”开头的字符串，如果没有参数，将返回以“?”开头的字符串
	 * @param srcPrefix 指定的前缀，程序只将以指定前缀开头的参数添加到URL中，如果没有前缀，可以为null
	 * @return
	 */
	public static final String addURLParam(HttpServletRequest request, boolean hasParameter, String srcPrefix) {
		return addURLParam(request, hasParameter, srcPrefix, null, null);
	}

	/**
	 * 将请求中以指定前缀开头的参数以指定编码封装为URL参数字符串<br>
	 * 编码使用request.getCharacterEncoding()获取的编码
	 * @param request 指定的HttpServletRequest
	 * @param hasParameter 指示是否已经有URL参数，如果已经有参数，将返回以“&”开头的字符串，如果没有参数，将返回以“?”开头的字符串
	 * @return
	 */
	public static final String addURLParam(HttpServletRequest request, boolean hasParameter) {
		return addURLParam(request, hasParameter, null, null, null);
	}

	/**
	 * 将指定的请求Map转为对应的URL参数字符串，并追加在指定的URL后
	 * @param url 指定的URL，此值不为null时，如果URL中有'?'，则追加以'&'开头的参数，如果没有，则追加以'?'开头的参数。 <b>因此，请不要在URL结尾处自行添加'?'或'&'</b>
	 * @param map 指定的请求Map，K必须为字符串类型
	 * @param filterPrefix 指定符合条件的参数前缀，如果该值不为null，则仅将以此前缀开头的参数key的映射加入URL参数
	 * @param destPrefix 指定生成的目标参数的前缀，如果<code>filterPrefix</code>为null，那么所有目标参数均将添加此前缀，如果<code>filterPrefix</code>不为null，那么前缀为<code>filterPrefix</code>的参数会被替换为此前缀。<br>
	 * 如果不想在生成的目标参数中追加前缀，那么此参数可以为null
	 * @param encoding 指定的编码，如果为null，默认使用UTF-8编码，如果指定的编码无法正确解析将引发异常
	 * @return
	 */
	public static final String addURLParam(CharSequence url, Map<String, ?> map, String filterPrefix, String destPrefix, String encoding) {
		int length = 0;
		StringBuilder sb = url != null && (length = url.length()) > 0 ? new StringBuilder(length + 32).append(url) : new StringBuilder(32);
		if (encoding == null) {
			encoding = defaultEncoding;
		}
		boolean noFilterPrefix = filterPrefix == null; // 指示没有前缀过滤
		int index = 0;// 前缀所占用的索引最大值
		if (destPrefix != null && !noFilterPrefix) {
			index = filterPrefix.length();
		}
		String key = null;
		Object value = null;
		boolean notFirst = false;// 指示不是第一个有效的参数
		for (Entry<String, ?> entry : map.entrySet()) {
			key = entry.getKey();
			if (noFilterPrefix || key.startsWith(filterPrefix, 0) && key != null) {
				// 如果没有前缀，或有前缀并以指定前缀开头，才拼接为URL
				if (notFirst) {
					sb.append('&');
				} else {
					if (length > 0) {
						sb.append(sb.lastIndexOf("?") > -1 ? '&' : '?');
					}
					notFirst = true;
				}
				if (index > 0) {
					key = key.substring(index);
				}
				value = entry.getValue();
				push(sb, key, value, encoding, destPrefix);
			}
		}
		return sb.toString();
	}

	/**
	 * 将指定的请求Map以UTF-8编码方式转为对应的URL参数字符串，并追加在指定的URL后
	 * @param url 指定的URL，此值不为null时，如果URL中有'?'，则追加以'&'开头的参数，如果没有，则追加以'?'开头的参数。 <b>因此，请不要在URL结尾处自行添加'?'或'&'</b>
	 * @param map 指定的请求Map，K必须为字符串类型
	 * @param filterPrefix 指定符合条件的参数前缀，如果该值不为null，则仅将以此前缀开头的参数key的映射加入URL参数
	 * @param destPrefix 指定生成的目标参数的前缀，如果<code>filterPrefix</code>为null，那么所有目标参数均将添加此前缀，如果<code>filterPrefix</code>不为null，那么前缀为<code>filterPrefix</code>的参数会被替换为此前缀。<br>
	 * 如果不想在生成的目标参数中追加前缀，那么此参数可以为null
	 * @return
	 */
	public static final String addURLParam(CharSequence url, Map<String, ?> map, String filterPrefix, String destPrefix) {
		return addURLParam(url, map, filterPrefix, destPrefix, null);
	}

	/**
	 * 将指定的请求Map以UTF-8编码方式转为对应的URL参数字符串，并追加在指定的URL后
	 * @param url 指定的URL，此值不为null时，如果URL中有'?'，则追加以'&'开头的参数，如果没有，则追加以'?'开头的参数。 <b>因此，请不要在URL结尾处自行添加'?'或'&'</b>
	 * @param map 指定的请求Map，K必须为字符串类型
	 * @param filterPrefix 指定符合条件的参数前缀，如果该值不为null，则仅将以此前缀开头的参数key的映射加入URL参数
	 * @return
	 */
	public static final String addURLParam(CharSequence url, Map<String, ?> map, String filterPrefix) {
		return addURLParam(url, map, filterPrefix, null, null);
	}

	/**
	 * 将指定的请求Map以UTF-8编码方式转为对应的URL参数字符串，并追加在指定的URL后
	 * @param url 指定的URL，此值不为null时，如果URL中有'?'，则追加以'&'开头的参数，如果没有，则追加以'?'开头的参数。 <b>因此，请不要在URL结尾处自行添加'?'或'&'</b>
	 * @param map 指定的请求Map，K必须为字符串类型
	 * @return
	 */
	public static final String addURLParam(CharSequence url, Map<String, ?> map) {
		return addURLParam(url, map, null, null, null);
	}

	/**
	 * 获取指定请求地址的参数部分，例如请求地址为“abc.do?name=jim&age=5”，将返回“name=jim&age=5”<br>
	 * 如果指定请求没有参数部分，将返回空字符串“”(有insertBefore参数仍然返回"")<br>
	 * <b>注意：</b>本方法只获取URL中传递的参数，不获取POST表单的请求参数
	 * @param request 指定的请求
	 * @param insertBefore 在生成的链接字符串前需要添加的内容，如果该值不为null，则返回结果如：<code>insertBefore</code> + "生成的参数字符串"(如果生成的参数字符串为null，将直接返回"");
	 * @return
	 */
	public static final String getURLParameter(HttpServletRequest request, String insertBefore) {
		String url = request.getQueryString();
		if (url == null) {
			return "";
		} else if (insertBefore == null) {
			return url;
		} else {
			return insertBefore + url;
		}
	}

	/**
	 * 获取指定链接地址的参数部分，例如“abc.do?name=jim&age=5”，将返回“name=jim&age=5”<br>
	 * 如果没有参数部分，将返回空字符串“”
	 * @param url 指定的链接
	 * @return
	 */
	public static final String getURLParameter(String url) {
		int index = url.indexOf('?', 0);
		if (index > 0 && ++index < url.length()) {
			// 如果有?字符，并且字符的索引不在第一位和最后一位
			return url.substring(index);
		} else {
			return "";
		}
	}

	public static String object2URL() {
		return null;
	}

	public static String request2SQL() {
		return null;
	}

	public static Object[] map2SQL(StringBuilder sb, Map<String, ?> map, String srcPrefix) {
		if (map.isEmpty()) {
			return new Object[] {};
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static String map2InsertSQL(String tableName, Map<String, ?> map, String srcPrefix, int valueSize) {
		HashBean<String, Object> bean = (HashBean<String, Object>) map;
		int size = bean.size();// 如果为null，直接抛出空指针
		// 如果valueSize为0，则不限制size的大小；如果valueSize大于0，那么bean中的参数对必须大于或等于valueSize
		Assert.isTrue(size > 0 && (valueSize == 0 || valueSize == size), "非法请求，缺失必要的请求参数！");
		boolean needCheck = valueSize > 0;
		// INSERT子句，类似于"INSERT INTO tableName(c1,c2,c3,c4) "
		StringBuilder sqlBuilder = new StringBuilder(1 << 5).append("INSERT INTO ").append(tableName).append('(');
		// VALUES子句 ，类似于“ VALUES(?,?,?,?)”
		StringBuilder valuesBuilder = new StringBuilder(1 << 5).append(") VALUES(");
		String[] keys = new String[size];
		Object[] values = new Object[size];
		int count = 0;
		boolean notFirst = false;
		for (Entry<String, Object> entry : bean.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Assert.isTrue(!needCheck || X.isBlank(value), "非法请求，无效的请求参数！");
			if (notFirst) {
				sqlBuilder.append(',');
				valuesBuilder.append(',');
			} else {
				notFirst = true;
			}
			keys[count] = key;
			values[count] = value;
			sqlBuilder.append(key);
			valuesBuilder.append('?');
			count++;
		}
		return null;
	}

	public static String object2SQL() {
		return null;
	}

	/**
	 * 根据指定的请求获取用户请求的真实IP地址，如果无法正确获取，有可能返回"unknown"或null
	 * @param request 指定的请求
	 * @return
	 */
	public static String getRealIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		boolean isInvalid = false;
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			isInvalid = true;
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isInvalid && (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))) {
			isInvalid = true;
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isInvalid && (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.length() > 15) {
			// 因为IP地址最短形式如1.1.1.1，长度至少为7，那么两个IP最短为7*2+2=16
			// 又因为一个IP最大长度为111.111.111.111，共计15位，因此用大于15来判断IP地址是否有2个及以上(此处未考虑IPv6)
			// 代理IP地址可能有多个，应该取第一个不为unknown的IP地址
			String[] ips = ip.split(", ");
			int length;
			if ((length = ips.length) > 1) {
				for (int i = 0; i < length; i++) {
					if (!"unknown".equalsIgnoreCase(ips[i])) {
						ip = ips[i];
						break;
					}
				}
			}
		}
		if ("127.0.0.1".equals(ip)) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				if (log.isDebugEnabled()) {
					log.debug("获取当前服务器的本地IP地址时发生异常！", e);
				}
			}
		}
		return ip;
	}

	/**
	 * 将指定键值对以指定编码追加至指定的StringBuilder中
	 * @param url 指定的StringBuilder
	 * @param key 指定的key
	 * @param value 指定的value
	 * @param encoding 指定的编码
	 */
	@SuppressWarnings("unchecked")
	private static final void push(StringBuilder url, String key, Object value, String encoding, String destPrefix) {
		// 此方法其实可以使用Array进行反射获取数组元素来代替，不过这个方法在此之前已经完成了，而且对于基础数据类型的处理，不用反射操作数组效率会更高，因此保留此方法
		if (destPrefix != null) {// 如果有目标前缀，则添加目标前缀
			url.append(destPrefix);
		}
		if (value == null) {// 如果没有参数值
			url.append(key).append('=');
		} else if (X.isArray(value)) {// 如果参数值为数组类型
			Class clazz = value.getClass();
			boolean notFirst = false;
			if (String[].class == clazz) {// 字符串数组
				String[] values = (String[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=');
					if (values[--length] != null) {
						try {
							url.append(URLEncoder.encode(values[length], encoding));
						} catch (UnsupportedEncodingException e) {
							throw new LogicException(e);
						}
					}
				} while (length > 0);
			} else if (int[].class == clazz) {// int数组
				int[] values = (int[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else if (double[].class == clazz) {// double数组
				double[] values = (double[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else if (long[].class == clazz) {// long数组
				long[] values = (long[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else if (float[].class == clazz) {// float数组
				float[] values = (float[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else if (byte[].class == clazz) {// byte数组
				byte[] values = (byte[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else if (short[].class == clazz) {// short数组
				short[] values = (short[]) value;
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=').append(values[--length]);
				} while (length > 0);
			} else {
				Object[] values = (Object[]) value;// 对象数组
				int length = values.length;
				do {
					if (notFirst) {
						url.append('&');
					} else {
						notFirst = true;
					}
					url.append(key).append('=');
					if (values[--length] != null) {
						try {
							url.append(URLEncoder.encode(values[length].toString(), encoding));
						} catch (UnsupportedEncodingException e) {
							throw new LogicException(e);
						}
					}
				} while (length > 0);
			}
		} else {// 其他非数组对象类型
			try {
				url.append(key).append('=').append(URLEncoder.encode(value.toString(), encoding));
			} catch (UnsupportedEncodingException e) {
				throw new LogicException(e);
			}
		}
	}

	/**
	 * 获取HttpServletRequest对象
	 * @return
	 */
	public static final HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获取HttpServletResponse对象
	 * @return
	 */
	public static final HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 获取ServletContext对象
	 * @return
	 */
	public static final ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	/**
	 * 获取当前HttpServletResponse的PrintWriter，并设置Content-Type响应头为：<code>"text/html;charset=" + encoding</code><br>
	 * 如果指定编码为null，默认使用UTF-8编码
	 * @return
	 */
	public static final PrintWriter getWriter(String encoding) {
		HttpServletResponse response = getResponse();
		response.setContentType(encoding == null ? "text/html;charset=utf-8" : "text/html;charset=" + encoding);
		try {
			return response.getWriter();
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * 获取当前HttpServletResponse的PrintWriter，并设置Content-Type响应头为：<code>"text/html;charset=utf-8"</code>
	 * @return
	 */
	public static final PrintWriter getWriter() {
		return getWriter(null);
	}
}
