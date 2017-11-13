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
 * 注册
 */
public class TaskRegister extends TaskBase<LoginResponse, Object> {

    private String mobileNum;
    private String code;

    public TaskRegister(Object context, String mobileNum, String code,
                        TaskCallback<LoginResponse> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.mobileNum = mobileNum;
        this.code = code;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "countryCode", Country.getUserCountry().code);
        params = this.put(params, "phone", mobileNum);
        params = this.put(params, "codes", code);
        this.post(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/user";
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
