package com.zhisland.android.blog.profile.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 用户是否可评价
 */
public class TaskCommentEnable extends TaskBase<Object, Object> {

    private long toUid;

    public TaskCommentEnable(Context context, long toUid,
                             TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.toUid = toUid;
        isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "toUid", toUid);
        this.put(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/comment/publish/enable";
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
}
