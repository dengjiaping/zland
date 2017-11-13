package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import de.greenrobot.event.EventBus;

/**
 * 更新我的点滴
 */
public class TaskUpdateDrip extends TaskBase<Object, Object> {

	private List<CustomDict> dicts;

	public TaskUpdateDrip(Object context, List<CustomDict> dicts,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.dicts = dicts;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "tips",
				GsonHelper.GetCommonGson().toJson(dicts));
		this.put(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/tips";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		PrefUtil.Instance().setIsGetDripTask(true);
		EventBus.getDefault().post(
				new EBProfile(EBProfile.TYPE_CHANGE_DRIP));
		return super.handleStringProxy(response);
	}
}