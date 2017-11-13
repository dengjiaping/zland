package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 删除荣誉
 */
public class TaskDeleteHonor extends TaskBase<Object, Object> {

	private String honorId;

	public TaskDeleteHonor(Object context, String honorId,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.honorId = honorId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.delete(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/honor/" + honorId;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		Object obj = super.handleStringProxy(response);
		EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_HONOR, null);
		EventBus.getDefault().post(eBProfile);
		return obj;
	}

}
