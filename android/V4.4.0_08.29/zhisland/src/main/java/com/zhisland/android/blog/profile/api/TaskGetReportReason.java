package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获可举报原因
 */
public class TaskGetReportReason extends TaskBase<ArrayList<Country>, Object> {

	public TaskGetReportReason(Object context,
			TaskCallback<ArrayList<Country>> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/dict/user/report/reason";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<Country>>() {
		}.getType();
	}

}
