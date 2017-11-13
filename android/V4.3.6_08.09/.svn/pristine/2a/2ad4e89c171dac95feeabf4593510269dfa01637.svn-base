package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.EventSpread;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取活动推广
 */
public class TaskGetEventSpread extends TaskBase<ArrayList<EventSpread>, Object> {


    public TaskGetEventSpread(Object context, TaskCallback<ArrayList<EventSpread>> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.post(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/event/list/spread";
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
    protected Type getDeserializeType() {
        return new TypeToken<ArrayList<EventSpread>>() {
        }.getType();
    }

}
