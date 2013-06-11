package mapx.util.filter;

public class EQ implements SQLFilter {

	EQ() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "=", value);
	}
}
