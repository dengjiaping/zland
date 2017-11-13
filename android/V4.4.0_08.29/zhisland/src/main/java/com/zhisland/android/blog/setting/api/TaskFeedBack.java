package com.zhisland.android.blog.setting.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 意见反馈
 */
public class TaskFeedBack extends TaskBase<Object, Object> {

	private String feedBack;

	public TaskFeedBack(Object context, String feedBack,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.feedBack = feedBack;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "feedback", feedBack);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/settings/feedback";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}