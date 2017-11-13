package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 取消报名Task
 * */
public class TaskSignCancel extends TaskBase<PayData, Object> {

	private long eventId;

	public TaskSignCancel(Object context, long eventId,
			TaskCallback<PayData> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		this.delete(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/sign";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<PayData>() {
		}.getType();
	}
}
