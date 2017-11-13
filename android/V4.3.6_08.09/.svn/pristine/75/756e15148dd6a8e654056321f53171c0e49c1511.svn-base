package com.zhisland.android.blog.setting.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 登出
 */
public class TaskLogout extends TaskBase<Object, Object> {

	public TaskLogout(Object apiHolder, TaskCallback<Object> responseCallback) {
		super(apiHolder, responseCallback);
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.delete(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}