package com.zhisland.android.blog.freshtask.model.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 新手任务，召唤好友
 */
public class TaskCallFriend extends TaskBase<Object, Object> {

    private String data;

    public TaskCallFriend(Context context, String data,
                          TaskCallback<Object> responseCallback) {
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
    protected String getPartureUrl() {
        return "/relation/task/summon/friend/apply";
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
