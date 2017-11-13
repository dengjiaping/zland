package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.VoteTo;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *  活动喜欢状态
 */
public class TaskLikeStatus extends TaskBase<Object,Object>{

    private long eventId;

    public TaskLikeStatus(Object context,long eventId,TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.eventId = eventId;
    }

    @Override
    public void execute() {
        this.post(null, null);
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected String getPartureUrl() {
        return "/event/like/" + eventId;
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ArrayList<VoteTo>>() {
        }.getType();
    }
}
