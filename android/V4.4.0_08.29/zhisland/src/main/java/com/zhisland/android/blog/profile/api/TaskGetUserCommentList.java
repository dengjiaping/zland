package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

public class TaskGetUserCommentList extends
		TaskBase<ZHPageData<UserComment>, Object> {

	private long userId;
	private String nextId;

	public TaskGetUserCommentList(Object context, long userId, String nextId,
			TaskCallback<ZHPageData<UserComment>> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.userId = userId;
		this.nextId = nextId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/comment/" + userId;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<UserComment>>() {
		}.getType();
	}

}
