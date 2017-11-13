package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.lang.reflect.Type;

/**
 * 获取我发布的活动列表
 * */
public class TaskGetInitiatedEvents extends TaskBase<ZHPageData<Event>, Object> {

	private String nextId;

	public TaskGetInitiatedEvents(Object context,
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
		return "/event/my/public";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<Event>>() {
		}.getType();
	}

}
