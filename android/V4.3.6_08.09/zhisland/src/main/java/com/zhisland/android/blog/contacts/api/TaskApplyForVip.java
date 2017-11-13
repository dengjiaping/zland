package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 申请成为岛亲
 */
public class TaskApplyForVip extends TaskBase<Object, Object> {

	private String source;

	public TaskApplyForVip(Object context, String source,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.source = source;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "source", source);
		this.post(params, null);
	}
	
	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user/memberApply";
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
