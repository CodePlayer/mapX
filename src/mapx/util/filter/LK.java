package mapx.util.filter;

public class LK implements SQLFilter {

	LK() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, " LIKE ", "%" + value + "%");
	}
}
