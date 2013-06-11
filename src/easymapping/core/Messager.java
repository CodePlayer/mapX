package easymapping.core;

import easymapping.util.btn.Button;

/**
 * 用于完成相应操作需要显示相应提示信息页面的传输组件类
 * @author Ready
 * @date 2012-10-24
 */
public class Messager {
	/**
	 * 不使用图标的样式名称
	 */
	public static final String OFF = "off";
	/**
	 * 显示错误图标[X]
	 */
	public static final String ERROR = "error";
	/**
	 * 显示警告图标[！]
	 */
	public static final String WARN = "warn";
	/**
	 * 显示信息图标[√]
	 */
	public static final String INFO = "info";

	public Messager() {}

	public Messager(String icon, String message, Button... buttons) {
		this.icon = icon;
		this.message = message;
		this.buttons = buttons;
	}

	private String icon;// 图标
	private String message;// 提示消息
	private Button[] buttons;

	public String getIcon() {
		return icon == null ? WARN : icon;
	}

	/**
	 * 设置消息提示页面的图标样式，可以使用本类中的内置常量(如EasyMessage.ERROR)。<br>
	 * 也支持设置自定义图标样式的名称，同时您应将自定义名称的样式加入对应的CSS文件中<br>
	 * <b>注意：</b>如果当前页面抛出异常，并且图标不是ERROR模式，系统会自动转为ERROR模式并输出异常中的信息
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Button[] getButtons() {
		return buttons;
	}

	public void setButtons(Button... buttons) {
		this.buttons = buttons;
	}

	/**
	 * 设置消息提示图标、提示消息内容
	 * @param icon
	 * @param message
	 */
	public void setIconAndMessage(String icon, String message) {
		this.icon = icon;
		this.message = message;
	}

	/**
	 * 设置消息提示图标、提示消息内容、按钮
	 * @param icon
	 * @param message
	 * @param buttons
	 */
	public void setAll(String icon, String message, Button... buttons) {
		this.icon = icon;
		this.message = message;
		this.buttons = buttons;
	}

	/**
	 * 返回跳转到信息提示页面的RESULT名称(即:global_message)
	 * @return
	 */
	public String result() {
		return "global_message";
	}
}
