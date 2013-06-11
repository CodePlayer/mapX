package mapx.util.filter;

public class LT implements SQLFilter {

	LT() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "<", value);
	}
}
