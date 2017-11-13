package com.zhisland.lib.async.http.task;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class ZHException extends Exception {

	private static final long serialVersionUID = 1L;
	public ZHException(int statusCode, String desc) {
		this.status_code = statusCode;
		this.desc = desc;
	}

	public ZHException(int statusCode, int warningCode, String desc) {
		this.status_code = statusCode;
		this.warning_code = warningCode;
		this.desc = desc;
	}

	@SerializedName("status_code")
	public int status_code;

	@SerializedName("warning_code")
	public int warning_code;

	@SerializedName("desc")
	public String desc;

}
