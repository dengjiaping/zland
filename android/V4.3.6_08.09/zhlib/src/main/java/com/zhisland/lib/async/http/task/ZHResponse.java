package com.zhisland.lib.async.http.task;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

public class ZHResponse<T> extends OrmDto{

	@SerializedName("code")
	public int code;

	@SerializedName("msg")
	public String msg;

	@SerializedName("data")
	public T data;

	public void copyTo(ZHResponse<T> res) {
		if (res == null)
			return;

		res.code = code;
		res.msg = msg;
		res.data = data;
	}

}
