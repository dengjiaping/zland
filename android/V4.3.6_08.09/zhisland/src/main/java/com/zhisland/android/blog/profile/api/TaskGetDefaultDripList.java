package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取默认点滴列表
 */
public class TaskGetDefaultDripList extends TaskBase<List<CustomDict>, Object> {

	public TaskGetDefaultDripList(Object context,
			TaskCallback<List<CustomDict>> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "uid", PrefUtil.Instance().getUserId());
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/tips";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<List<CustomDict>>() {
		}.getType();
	}

}
