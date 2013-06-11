package mapx.util.btn;

/**
 * 用户表示具备URL跳转功能的按钮对象
 * @author Ready
 * @date 2012-10-27
 */
public class ForwardButton extends Button {
	private String url;
	private String value;

	/**
	 * 创建具备跳转到指定URL的按钮对象
	 * @param value 指定的按钮显示值
	 * @param url 指定的URL
	 */
	public ForwardButton(String value, String url) {
		this.value = value;
		this.url = url;
	}

	@Override
	public String toJsCode() {
		return GET_FORWARD_FUNCTION + "(\"" + value + "\", \"" + url + "\")";
	}
}
