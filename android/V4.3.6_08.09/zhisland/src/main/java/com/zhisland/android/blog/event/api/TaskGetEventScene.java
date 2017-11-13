package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取当前现场活动
 */
public class TaskGetEventScene extends TaskBase<Event, Object> {

	public TaskGetEventScene(Object context,
			TaskCallback<Event> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
	}

	@Override
	public void execute() {
		this.get(null, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/scene";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Event>() {
		}.getType();
	}

}
