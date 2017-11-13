package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取资源列表
 * */
public class TaskGetResourceList extends TaskBase<ArrayList<Resource>, Object> {

	public static final Integer TYPE_SUPPLY = 1;
	public static final Integer TYPE_DEMAND = 2;

	Integer type;

	public TaskGetResourceList(Object context, Integer type,
			TaskCallback<ArrayList<Resource>> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.type = type;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "type", type);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/resource";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<Resource>>() {
		}.getType();
	}

}
