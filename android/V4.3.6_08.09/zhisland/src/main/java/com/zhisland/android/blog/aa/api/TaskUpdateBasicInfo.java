package com.zhisland.android.blog.aa.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 完善基本信息
 */
public class TaskUpdateBasicInfo extends TaskBase<Integer, Object> {

    private User user;

    public TaskUpdateBasicInfo(Object context, User user, TaskCallback<Integer> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.user = user;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "user",
                GsonHelper.GetCommonGson().toJson(user));
        this.put(params, null);
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
        return "1.1";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Integer>() {
        }.getType();
    }

}
