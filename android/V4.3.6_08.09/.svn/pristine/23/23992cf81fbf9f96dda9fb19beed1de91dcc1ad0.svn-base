package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 审核通过Task
 * */
public class TaskSignApproved extends TaskBase<Object, Object> {

	private long eventId;
	private long userId;

	public TaskSignApproved(Object context, long eventId, long userId,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.userId = userId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "userId", userId);
		this.post(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/audit";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}
	
}
