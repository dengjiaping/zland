package com.zhisland.lib.async.http.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.util.StringUtil;

public class TaskHelper {

	protected enum EnumSwitch {
		LargeThan, EqualTo, SmallThan
	}

	/**
	 * put value for key to params, switch values, which will be used to compare
	 * with value, if match isEqual case, the key-value pair will be put
	 * 
	 * @param params
	 * @param key
	 * @param value
	 * @param switchValue
	 * 
	 * @param isEqual
	 * @return
	 */
	public RequestParams put(RequestParams params, String key, String value,
			String switchValue, boolean isEqual) {
		if (key == null || value == null) {
			return params;
		}
		if (isEqual) {
			if (switchValue != null && value.equals(switchValue)) {
				if (params == null) {
					params = new RequestParams();
				}
				params.put(key, value);
			}
		} else {
			if (!value.equals(switchValue)) {
				if (params == null) {
					params = new RequestParams();
				}
				params.put(key, value);
			}
		}

		return params;
	}

	/**
	 * put value for key to params, switch values, which will be used to compare
	 * with value, if match isEqual case, the key-value pair will be put
	 * 
	 * @param params
	 * @param key
	 * @param value
	 * @param switchValue
	 * @param switchType
	 * @return
	 */
	public RequestParams put(RequestParams params, String key, int value,
			int switchValue, EnumSwitch switchType) {
		if (StringUtil.isNullOrEmpty(key)) {
			return params;
		}
		boolean shouldPut = false;

		switch (switchType) {
		case LargeThan:
			if (value > switchValue) {
				shouldPut = true;
			}
			break;
		case EqualTo:
			if (value == switchValue) {
				shouldPut = true;
			}
			break;
		case SmallThan:
			if (value < switchValue) {
				shouldPut = true;
			}
			break;
		default:
			break;
		}
		if (shouldPut) {
			if (params == null) {
				params = new RequestParams();
			}

			params.put(key, String.valueOf(value));
		}

		return params;
	}

	public RequestParams put(RequestParams params, String key, long value) {
		if (StringUtil.isNullOrEmpty(key)) {
			return params;
		}

		if (params == null) {
			params = new RequestParams();
		}

		params.put(key, String.valueOf(value));

		return params;
	}

	public RequestParams put(RequestParams params, String key, long value,
			int switchValue, EnumSwitch switchType) {
		if (StringUtil.isNullOrEmpty(key)) {
			return params;
		}
		boolean shouldPut = false;

		switch (switchType) {
		case LargeThan:
			if (value > switchValue) {
				shouldPut = true;
			}
			break;
		case EqualTo:
			if (value == switchValue) {
				shouldPut = true;
			}
			break;
		case SmallThan:
			if (value < switchValue) {
				shouldPut = true;
			}
			break;
		default:
			break;
		}
		if (shouldPut) {
			if (params == null) {
				params = new RequestParams();
			}

			params.put(key, String.valueOf(value));
		}

		return params;
	}

	protected RequestParams put(RequestParams params, String key, File file) {
		if (StringUtil.isNullOrEmpty(key) || file == null) {
			return params;
		}

		if (params == null) {
			params = new RequestParams();
		}
		try {
			params.put(key, file);
		} catch (FileNotFoundException e) {
		}
		return params;
	}

	protected RequestParams put(RequestParams params, String key, File file,
			long start, int count) {
		if (StringUtil.isNullOrEmpty(key) || file == null) {
			return params;
		}

		if (params == null) {
			params = new RequestParams();
		}
		try {
			params.put(key, file, start, count);
		} catch (FileNotFoundException e) {
		}
		return params;
	}

}
