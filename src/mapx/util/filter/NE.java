package mapx.util.filter;

public class NE implements SQLFilter {

	NE() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "<>", value);
	}
}
