package com.zhisland.android.blog.find.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取最新岛邻。
 * Created by Mr.Tui on 2016/5/20.
 */
public class TaskGetNewDaolin extends TaskBase<ArrayList<User>, Object> {

    public TaskGetNewDaolin(Object context, TaskCallback<ArrayList<User>> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.get(params, null);
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected String getPartureUrl() {
        return "/recommend/user/lastJoinUser";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ArrayList<User>>() {
        }.getType();
    }

}
