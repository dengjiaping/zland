package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 举报用户
 */
public class TaskReportUser extends TaskBase<Object, Object> {

	private long userId;
	private String reasonId;

	public TaskReportUser(Object context, long userId, String reasonId,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.userId = userId;
		this.reasonId = reasonId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "userId", userId);
		params = this.put(params, "reasonId", reasonId);
		this.post(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/center/report";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}
}
