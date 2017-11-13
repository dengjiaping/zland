package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.InvitationNotice;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 获取邀请须知内容 
 */
public class TaskGetInvitationNotice extends TaskBase<InvitationNotice, Object> {

	public TaskGetInvitationNotice(Object context,
			TaskCallback<InvitationNotice> responseCallback) {
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
		return "/message/notice/invitation";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<InvitationNotice>() {
		}.getType();
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}
}
