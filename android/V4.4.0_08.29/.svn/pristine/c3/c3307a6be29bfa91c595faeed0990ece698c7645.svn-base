package com.zhisland.android.blog.setting.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 设置是否对附近的人可见
 * */
public class TaskInvisiblePosition extends TaskBase<Object, Object> {

	Integer invisible;

	public TaskInvisiblePosition(Object context, Integer invisible,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.invisible = invisible;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "invisible", invisible);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/lbs/position/invisible";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		return super.handleStringProxy(response);
	}
}
