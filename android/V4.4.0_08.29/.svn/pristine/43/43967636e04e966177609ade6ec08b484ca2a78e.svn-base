package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 更新邀请请求状态
 * */
public class TaskUpdateUserInvitationStatus extends TaskBase<Object, Object> {

	public static final int STATUS_REFUSE = 0;
	public static final int STATUS_AGREE = 1;

	private long userId;
	private int status;

	public TaskUpdateUserInvitationStatus(Object context, long userId,
			int status, TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.userId = userId;
		this.status = status;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "status", status);
		params = this.put(params, "userId", userId);
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
