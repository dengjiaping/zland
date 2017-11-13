package com.zhisland.android.blog.common.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 获取权限列表
 */
public class TaskGetPermissions extends TaskBase<List<String>, Object> {

    public TaskGetPermissions(Object context, TaskCallback<List<String>> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.get(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "user/permissions";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<List<String>>() {
        }.getType();
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected List<String> handleStringProxy(HttpResponse response)
            throws Exception {
        handleHeaderInfo(response);
        String responseBody = convertToString(response);
        PrefUtil.Instance().setKeyValue(PermissionsMgr.PREF_KEY_PERMISSIONS, responseBody);
        return null;
    }

    @Override
    protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        return super.handleFailureMessage(e, responseBody);
    }
}
