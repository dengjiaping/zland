package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取现场活动签到的用户列表
 */
public class TaskGetUserCountInScene extends TaskBase<Integer, Object> {

	private long eventId;

	public TaskGetUserCountInScene(Object context, long eventId,
			TaskCallback<Integer> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/scene/" + eventId + "/users/count";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Integer>() {
		}.getType();
	}

}
