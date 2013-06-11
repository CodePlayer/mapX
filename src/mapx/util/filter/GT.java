package mapx.util.filter;

public class GT implements SQLFilter {

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, ">", value);
	}
}
