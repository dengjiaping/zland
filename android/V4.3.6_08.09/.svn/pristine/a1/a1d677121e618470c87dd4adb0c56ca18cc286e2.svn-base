package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import de.greenrobot.event.EventBus;

/**
 * 删除神评论
 */
public class TaskDeleteUserComment extends TaskBase<Object, Object> {

    private long commentId;

    public TaskDeleteUserComment(Object context, long commentId, TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        isPureRest = true;
        this.commentId = commentId;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "commentId", commentId);
        this.put(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/comment/remove";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Object>() {
        }.getType();
    }

    @Override
    protected Object handleStringProxy(HttpResponse response) throws Exception {
        EventBus.getDefault()
                .post(new EBProfile(
                        EBProfile.TYPE_CHANGE_COMMENT));
        return super.handleStringProxy(response);
    }
}
