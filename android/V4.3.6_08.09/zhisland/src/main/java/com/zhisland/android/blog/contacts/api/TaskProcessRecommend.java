package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 处理推荐好友请求
 */
public class TaskProcessRecommend extends TaskBase<Object, Object> {

	// 忽略推荐好友
	public static final int INGORE_RECOMMEND = 0;
	// 同意推荐好友
	public static final int AGREE_RECOMMEND = 1;

	private long targetUid;
	private int mark;

	public TaskProcessRecommend(Object context, long targetUid, int mark,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.targetUid = targetUid;
		this.mark = mark;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "targetUid", targetUid);
		params = this.put(params, "mark", mark);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/search/recommend/operation";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}
