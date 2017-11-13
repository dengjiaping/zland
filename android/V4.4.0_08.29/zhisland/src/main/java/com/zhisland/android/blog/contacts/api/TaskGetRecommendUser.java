package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 推荐人列表
 */
public class TaskGetRecommendUser extends TaskBase<ArrayList<User>, Object> {

	public TaskGetRecommendUser(Object context,
			TaskCallback<ArrayList<User>> responseCallback) {
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
		return "/search/recommend";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<User>>() {
		}.getType();
	}

}
