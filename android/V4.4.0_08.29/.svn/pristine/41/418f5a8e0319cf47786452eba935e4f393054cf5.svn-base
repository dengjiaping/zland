package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.lang.reflect.Type;

/**
 * 获取报名人员列表
 * */
public class TaskGetMembersSigned extends TaskBase<ZHPageData<User>, Object> {

	private long eventId;
	private String nextId;

	public TaskGetMembersSigned(Object context, long eventId, String nextId,
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
		return "/event/" + eventId + "/signed";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<User>>() {
		}.getType();
	}

}
