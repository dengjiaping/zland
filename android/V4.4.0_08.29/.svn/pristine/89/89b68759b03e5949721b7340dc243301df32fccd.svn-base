package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取发展阶段字典的接口
 */
public class TaskgetCompanyStateList extends
		TaskBase<ArrayList<CompanyState>, Object> {

	public TaskgetCompanyStateList(Object context,
			TaskCallback<ArrayList<CompanyState>> responseCallback) {
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
		return "/dict/org/stage";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<CompanyState>>() {
		}.getType();
	}

}