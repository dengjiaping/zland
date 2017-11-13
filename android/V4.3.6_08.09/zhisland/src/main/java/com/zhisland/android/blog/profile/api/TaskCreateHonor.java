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
 * 创建荣誉
 */
public class TaskCreateHonor extends TaskBase<String, Object> {

	private Honor honor;

	public TaskCreateHonor(Object context, Honor honor,
			TaskCallback<String> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.honor = honor;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "honor",
				GsonHelper.GetCommonGson().toJson(honor));
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/honor";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<String>() {
		}.getType();
	}

	@Override
	protected String handleStringProxy(HttpResponse response) throws Exception {
		honor.id = super.handleStringProxy(response);
		EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_HONOR, honor);
		EventBus.getDefault().post(eBProfile);
		return honor.id;
	}

}
