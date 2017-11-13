package com.zhisland.android.blog.aa.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 登录
 */
public class TaskLogin extends TaskBase<LoginResponse, Object> {

	private String uname, password;

	public TaskLogin(Object context, String uname, String password,
			TaskCallback<LoginResponse> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.uname = uname;
		this.password = password;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "mobile", uname);
		params = this.put(params, "password", password);
        params = this.put(params, "countryCode", Country.getUserCountry().code);
		this.post(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user/login";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<LoginResponse>() {
		}.getType();
	}

}
