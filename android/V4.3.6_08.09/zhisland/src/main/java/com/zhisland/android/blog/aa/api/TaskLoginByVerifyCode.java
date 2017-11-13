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
 * 验证码登录
 */
public class TaskLoginByVerifyCode extends TaskBase<LoginResponse, Object> {

	private String phone;
    private String codes;

	public TaskLoginByVerifyCode(Object context, String phone, String codes,
                                 TaskCallback<LoginResponse> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.phone = phone;
		this.codes = codes;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "phone", phone);
		params = this.put(params, "codes", codes);
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
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
	protected Type getDeserializeType() {
		return new TypeToken<LoginResponse>() {
		}.getType();
	}

}
