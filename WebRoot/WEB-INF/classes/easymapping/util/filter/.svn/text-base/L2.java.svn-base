package easymapping.util.filter;

public class L2 implements SQLFilter {

	L2() {}

	@Override
	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, " LIKE ", value + "%");
	}
}
