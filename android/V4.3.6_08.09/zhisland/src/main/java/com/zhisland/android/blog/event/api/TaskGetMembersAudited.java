package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 获取报名审核中的人员列表
 * */
public class TaskGetMembersAudited extends TaskBase<ZHPageData<User>, Object> {

	private long eventId;
	private String nextId;

	public TaskGetMembersAudited(Object context, long eventId, String nextId,
			TaskCallback<ZHPageData<User>> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.nextId = nextId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		params = this.put(params, "count", 20);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/passed";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<User>>() {
		}.getType();
	}

}
