package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 删除公司task
 * */
public class TaskDeleteCompany extends TaskBase<Object, Object> {

	private long companyId;

	public TaskDeleteCompany(Object context, long companyId,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.companyId = companyId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		this.delete(null, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/company/"+companyId;
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
