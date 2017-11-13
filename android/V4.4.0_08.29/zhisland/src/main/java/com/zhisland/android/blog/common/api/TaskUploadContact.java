package com.zhisland.android.blog.common.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/5/25.
 */
public class TaskUploadContact extends TaskBase<Object, Object> {

    private String data;

    public TaskUploadContact(Object context, String data, TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.data = data;
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "data", data);
        this.post(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "user/contact/sync/json";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Object>() {
        }.getType();
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        return super.handleFailureMessage(e, responseBody);
    }

}
