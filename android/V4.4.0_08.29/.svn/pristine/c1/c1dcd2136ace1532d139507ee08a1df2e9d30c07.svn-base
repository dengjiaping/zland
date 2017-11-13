package com.zhisland.android.blog.im.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.im.dto.MessageCount;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 获取未读消息数
 */
public class TaskGetMessageCount extends TaskBase<MessageCount, Object> {

    public TaskGetMessageCount(Object context, TaskCallback<MessageCount> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.get(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "notification/center/quantity";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<MessageCount>() {
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

}
