package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 更新荣誉
 */
public class TaskUpdateHonor extends TaskBase<Object, Object> {

	private Honor honor;

	public TaskUpdateHonor(Object context, Honor honor,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.honor = honor;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "honor",
				GsonHelper.GetCommonGson().toJson(honor));
		this.put(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/honor";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		Object obj = super.handleStringProxy(response);
		EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_HONOR, honor);
		EventBus.getDefault().post(eBProfile);
		return obj;
	}
}
