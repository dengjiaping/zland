package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取标签列表
 */
public class TaskGetTagList extends TaskBase<ArrayList<String>, Object> {

	private int tagType;

	public TaskGetTagList(Object context, int tagType,
			TaskCallback<ArrayList<String>> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.tagType = tagType;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "tagType", tagType);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/tag";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<String>>() {
		}.getType();
	}

	
	@Override
	protected Throwable handleFailureMessage(Throwable e, String responseBody) {
		return super.handleFailureMessage(e, responseBody);
	}
}
