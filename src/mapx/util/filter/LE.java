package mapx.util.filter;

public class LE implements SQLFilter {

	LE() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "<=", value);
	}
}
