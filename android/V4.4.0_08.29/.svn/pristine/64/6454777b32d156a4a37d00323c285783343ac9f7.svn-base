package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 
 * 取消活动
 */
public class TaskEventCancel extends TaskBase<Object, Object> {

	private long eventId;
	private String reason;

	public TaskEventCancel(Object context, long eventId, String reason,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.eventId = eventId;
		this.reason = reason;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "reason", reason);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/delete";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}
	
}
