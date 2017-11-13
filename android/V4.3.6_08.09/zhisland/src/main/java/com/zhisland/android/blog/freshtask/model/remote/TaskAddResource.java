package com.zhisland.android.blog.freshtask.model.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 新手任务，添加资源
 */
public class TaskAddResource extends TaskBase<Object, Object> {

    private String resource;

    public TaskAddResource(Context context, String resource,
                           TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.resource = resource;
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "resource", resource);
        this.post(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/resource/save/task";
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
