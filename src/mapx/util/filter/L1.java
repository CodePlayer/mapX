package mapx.util.filter;

public class L1 implements SQLFilter {

	L1() {}

	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, " LIKE ", "%" + value);
	}
}
