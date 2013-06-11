package com.unisure.util;

import easymapping.util.EasyDate;
import easymapping.util.filter.Entry;
import easymapping.util.filter.SQLFilter;

public class DT implements SQLFilter {

	@Override
	public Entry filter(String realKey, Object value) {
		return new Entry(realKey, "=", EasyDate.valueOf(value));
	}
}
