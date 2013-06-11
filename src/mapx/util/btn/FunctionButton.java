package mapx.util.btn;

/**
 * 表示执行指定函数的按钮对象
 * @author Ready
 * @date 2012-10-28
 */
public class FunctionButton extends Button {
	private String value;
	private String function;

	/**
	 * 创建执行指定函数的按钮对象
	 * @param value 指定按钮的显示值
	 * @param functionName 指定执行的函数，如“test()”或“test('hello')”或“test(this)”
	 */
	public FunctionButton(String value, String functionName) {
		this.value = value;
		this.function = functionName;
	}

	@Override
	public String toJsCode() {
		return GET_FUNCTION_BUTTON_FUNCTION + "(\"" + value + "\",\"" + function + "\")";
	}
}
