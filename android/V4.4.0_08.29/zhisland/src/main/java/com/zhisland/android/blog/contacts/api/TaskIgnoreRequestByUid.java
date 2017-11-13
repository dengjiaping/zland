package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 批量获取好友列表
 */
public class TaskIgnoreRequestByUid extends TaskBase<Object, Object> {

	private long applyUid;

	public TaskIgnoreRequestByUid(Object context,
			TaskCallback<Object> callback, long applyUid) {
		super(context, callback);
		this.isPureRest = true;
		this.applyUid = applyUid;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "applyUid", applyUid);
		this.post(params, null);
	}

	@Override
	protected String getUrl() {
		return Config.getHostUrl() + "relation/request/ignore";
	};

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}
