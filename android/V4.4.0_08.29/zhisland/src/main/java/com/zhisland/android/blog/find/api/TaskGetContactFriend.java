package com.zhisland.android.blog.find.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取可能认识的人
 * Created by Mr.Tui on 2016/5/20.
 */
public class TaskGetContactFriend extends TaskBase<ZHPageData<User>, Object> {

    String nextId;
    int count;

    public TaskGetContactFriend(Object context, String nextId, int count, TaskCallback<ZHPageData<User>> responseCallback) {
        super(context, responseCallback);
        this.nextId = nextId;
        this.isPureRest = true;
        this.count = count;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "nextId", nextId);
        params = this.put(params, "count", count);
        this.get(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/relation/friend/mayknow/list";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ZHPageData<User>>() {
        }.getType();
    }

}
