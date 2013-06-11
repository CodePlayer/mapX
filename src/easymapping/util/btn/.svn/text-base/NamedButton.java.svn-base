package easymapping.util.btn;

/**
 * 用于表示已经在JS代码中注册创建的按钮对象
 * @author Ready
 * @date 2012-10-27
 */
public class NamedButton extends Button {
	private String btnName;

	public NamedButton(String btnName) {
		this.btnName = btnName;
	}

	@Override
	public String toJsCode() {
		return GET_NAMED_BUTTON_FUNCTION + "(\"" + btnName + "\")";
	}
}
