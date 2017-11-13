package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.UserAssistant;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 更新我的助理
 */
public class TaskUpdateAssistant extends TaskBase<Object, Object> {

	private UserAssistant userAssistant;

	public TaskUpdateAssistant(Object context, UserAssistant userAssistant,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.userAssistant = userAssistant;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "assistant", GsonHelper.GetCommonGson()
				.toJson(userAssistant));
		this.put(params, null);
	}
	
	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user/contact/assistant";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_ASSISTANT, userAssistant);
		EventBus.getDefault().post(eBProfile);
		return super.handleStringProxy(response);
	}
}
