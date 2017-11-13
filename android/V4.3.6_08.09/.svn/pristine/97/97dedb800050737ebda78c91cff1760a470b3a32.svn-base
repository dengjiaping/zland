package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.SceneNotify;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 获取现场模式中某个活动的通知list
 * */
public class TaskGetSceneNotifyList extends TaskBase<ZHPageData<SceneNotify>, Object> {

	private long eventId;
	private String nextId;
	public TaskGetSceneNotifyList(Object context, long eventId,String nextId,TaskCallback<ZHPageData<SceneNotify>> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.eventId = eventId;
		this.nextId = nextId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		params = this.put(params, "nextId", nextId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/scene/"+eventId+"/notice";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<SceneNotify>>() {
		}.getType();
	}
}
