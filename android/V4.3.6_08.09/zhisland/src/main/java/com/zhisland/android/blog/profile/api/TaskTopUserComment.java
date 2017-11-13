package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 置顶神评论
 */
public class TaskTopUserComment extends TaskBase<Object, Object> {

	/**
	 * 1：置顶， 0：取消置顶
	 */
	public static final int TOP_COMMENT = 1;
	public static final int CANCEL_TOP_COMMENT = 0;

	private long commentId;
	private int status;

	public TaskTopUserComment(Object context, long commentId, int status,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.commentId = commentId;
		this.status = status;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "commentId", commentId);
		params = this.put(params, "status", status);
		this.put(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/comment/top";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

}
