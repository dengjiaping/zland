package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;

/**
 * 通过手机号邀请
 */
public class TaskInvite extends TaskBase<String, Object> {

    private String name;
    private String mobile;

    public TaskInvite(Object context, String name, String mobile,
                      TaskCallback<String> responseCallback) {
        super(context, responseCallback);
        this.name = name;
        this.mobile = mobile;
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "name", name);
        params = this.put(params, "phone", mobile);
        this.post(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/contacts/invite";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<String>() {
        }.getType();
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected String handleStringProxy(HttpResponse response) throws Exception {
        return convertToString(response);
    }
}
