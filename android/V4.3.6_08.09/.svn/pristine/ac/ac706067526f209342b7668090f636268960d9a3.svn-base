package com.zhisland.android.blog.aa.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 获取手机验证码
 * */
public class TaskGetVerifyCode extends TaskBase<Object, Object> {

	private String mobileNum;
    private String countryCode;

	public TaskGetVerifyCode(Object apiHolder, String mobileNum, String countryCode,
			TaskCallback<Object> responseCallback) {
		super(apiHolder, responseCallback);
		this.isPureRest = true;
		this.mobileNum = mobileNum;
        this.countryCode = countryCode;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "mobile", mobileNum);
        params = this.put(params, "countryCode", countryCode);
		this.post(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/message/verifyCode";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}
