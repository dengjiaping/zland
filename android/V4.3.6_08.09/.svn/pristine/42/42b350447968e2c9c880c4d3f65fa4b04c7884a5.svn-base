package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 主动邀请
 */
public class TaskGetInvites extends TaskBase<InviteStructure, Object> {

    public TaskGetInvites(Object context,
                          TaskCallback<InviteStructure> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        this.get(null, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/invite";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<InviteStructure>() {
        }.getType();
    }

}
