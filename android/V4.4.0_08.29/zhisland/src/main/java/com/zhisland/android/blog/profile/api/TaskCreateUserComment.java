package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import de.greenrobot.event.EventBus;

/**
 * 创建神评论
 */
public class TaskCreateUserComment extends TaskBase<Object, Object> {

	private long toUserId;
	private String content;

	public TaskCreateUserComment(Object context, long toUserId,
			String content, TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.toUserId = toUserId;
		this.content = content;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "toUid", toUserId);
		params = this.put(params, "content", content);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/comment";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		EventBus.getDefault()
				.post(new EBProfile(
						EBProfile.TYPE_CHANGE_COMMENT));
		return super.handleStringProxy(response);
	}
}
