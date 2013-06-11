package mapx.util.filter;

public class LT implements SQLFilter {

	LT() {}

	@Override
	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "<", value);
	}
}
