package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 获取我报名的活动列表
 */
public class TaskGetSignUpEvents extends TaskBase<ZHPageData<Event>, Object> {

	private String nextId;

	public TaskGetSignUpEvents(Object context,
			String nextId, TaskCallback<ZHPageData<Event>> responseCallback) {
		super(context, responseCallback);
		this.nextId = nextId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		params = this.put(params, "count", 20);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/my/sign";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<Event>>() {
		}.getType();
	}

}
