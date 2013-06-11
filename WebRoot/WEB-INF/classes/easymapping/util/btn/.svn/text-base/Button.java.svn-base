package easymapping.util.btn;

/**
 * 用于指示JS代码中动态创建Button的抽象父类
 * @author Ready
 * @date 2012-10-28
 */
public abstract class Button {
	/**
	 * 【返回】按钮
	 */
	public static final Button BACK = forName("back");
	/**
	 * 【关闭】按钮
	 */
	public static final Button CLOSE = forName("close");

	/**
	 * 获取在JS中指定命名的按钮
	 * @param name
	 * @return
	 */
	public static Button forName(String name) {
		return new NamedButton(name);
	}

	/**
	 * 获取跳转到指定命名的按钮
	 * @param value
	 * @param url
	 * @return
	 */
	public static Button forForward(String value, String url) {
		return new ForwardButton(value, url);
	}

	/**
	 * 获取执行指定函数的按钮
	 * @param value
	 * @param functionName
	 * @return
	 */
	public static Button forFunction(String value, String functionName) {
		return new FunctionButton(value, functionName);
	}

	/**
	 * 表示用于获取已在JS代码中注册的按钮的JS函数名称【参数(String name[, String value])】
	 */
	public static final String GET_NAMED_BUTTON_FUNCTION = "getNamedButton";
	/**
	 * 在JS中用于获取地址跳转函数的函数名称【参数(String value, String url)】
	 */
	public static final String GET_FORWARD_FUNCTION = "getForwardButton";
	/**
	 * 
	 */
	public static final String GET_FUNCTION_BUTTON_FUNCTION = "getFunctionButton";

	/**
	 * 用于返回可直接使用的JS代码<br>
	 * 系统将采用如下形式调用(返回值两边需要加引号的，请自行添加)： var btn = 返回值;
	 * @return
	 */
	public abstract String toJsCode();
}
