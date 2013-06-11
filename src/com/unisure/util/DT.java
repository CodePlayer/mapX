package com.unisure.util;

import mapx.util.EasyDate;
import mapx.util.filter.Entry;
import mapx.util.filter.SQLFilter;

public class DT implements SQLFilter {

	@Override
	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "=", EasyDate.valueOf(value));
	}
}
