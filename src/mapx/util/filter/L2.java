package mapx.util.filter;

public class L2 implements SQLFilter {

	L2() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, " LIKE ", value + "%");
	}
}
