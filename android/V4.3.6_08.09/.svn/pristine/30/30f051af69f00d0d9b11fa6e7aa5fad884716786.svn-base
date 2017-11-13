package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 同意或者拒绝求邀请
 */
public class TaskUpdateRequestStatus extends TaskBase<Object, Object> {

    //同意请求
    public static final int STATUS_AGREE = 1;
    //拒绝请求
    public static final int STATUS_REFUSE = 2;

    private String userId;
    private int status;

    public TaskUpdateRequestStatus(Object context, String userId, int status,
                                   TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "userId", userId);
        params = this.put(params, "status", status);
        this.put(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/invite/request";
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
