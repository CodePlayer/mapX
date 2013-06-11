package easymapping.util.filter;

public class LK implements SQLFilter {

	LK() {}

	@Override
	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, " LIKE ", "%" + value + "%");
	}
}
