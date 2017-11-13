package com.zhisland.android.blog.aa.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 修改密码
 */
public class TaskModifyPwd extends TaskBase<Object, Object> {

    private String newPassword;

    public TaskModifyPwd(Object context,
                         String newPassword, TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.newPassword = newPassword;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "password", newPassword);
        this.put(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/user/password";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Object>() {
        }.getType();
    }

}