package easymapping.util.filter;

import java.util.HashMap;
import java.util.Map;
import easymapping.util.SpringUtil;

@SuppressWarnings("unchecked")
public class FilterPool {

	private FilterPool() {}
	private final static Map<String, SQLFilter> pool = new HashMap<String, SQLFilter>();
	static {
		pool.put("EQ", new EQ());
		pool.put("NE", new NE());
		pool.put("LT", new LT());
		pool.put("LE", new LE());
		pool.put("GT", new GT());
		pool.put("GE", new GE());
		pool.put("LK", new LK());
		pool.put("L1", new L1());
		pool.put("L2", new L2());
		String name = "extendSQLFilters";
		if (SpringUtil.getApplicationContext().containsBean(name)) {
			Map<String, SQLFilter> extend = (Map<String, SQLFilter>) SpringUtil.getBean(name);
			if (extend != null) {
				addFilters(extend);
			}
		}
	}

	public static final void addFilters(Map<String, SQLFilter> filters) {
		pool.putAll(filters);
	}

	public static final SQLFilter getFilter(String suffix) {
		return pool.get(suffix);
	}
}
