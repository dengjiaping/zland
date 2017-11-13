package com.zhisland.android.blog.freshtask.model.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 新手任务，更新user形象照
 */
public class TaskUpdateUserFigure extends TaskBase<Object, Object> {

    private String figure;

    public TaskUpdateUserFigure(Context context, String figure,
                          TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.figure = figure;
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "figure", figure);
        this.put(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/user/figure/task";
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
