package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取公司列表
 * */
public class TaskGetCompanyList extends TaskBase<ArrayList<Company>, Object> {

	public TaskGetCompanyList(Object context,
			TaskCallback<ArrayList<Company>> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/company";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<Company>>() {
		}.getType();
	}

}
