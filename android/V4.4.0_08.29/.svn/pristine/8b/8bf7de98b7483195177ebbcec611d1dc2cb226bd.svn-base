package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 保存活动推广内容
 */
public class TaskSpreadEvent extends TaskBase<Object, Object> {

	private long eventId;
	private String content;

	public TaskSpreadEvent(Object context, long eventId, String content,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.eventId = eventId;
		this.content = content;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		params = this.put(params, "contents", content);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/extension";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}
