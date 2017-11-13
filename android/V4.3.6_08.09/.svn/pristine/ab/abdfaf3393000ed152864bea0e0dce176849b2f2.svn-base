package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取荣誉列表
 * */
public class TaskGetHonorList extends TaskBase<ArrayList<Honor>, Object> {

	public static final Integer TYPE_SUPPLY = 1;
	public static final Integer TYPE_DEMAND = 2;

	public TaskGetHonorList(Object context,
			TaskCallback<ArrayList<Honor>> responseCallback) {
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
		return "/honor";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<Honor>>() {
		}.getType();
	}

}
