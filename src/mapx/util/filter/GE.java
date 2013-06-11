package mapx.util.filter;

public class GE implements SQLFilter {

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, ">=", value);
	}
}
