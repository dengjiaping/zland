package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 创建供给
 */
public class TaskCreateResource extends TaskBase<Long, Object> {

	private Resource resource;

	public TaskCreateResource(Object context, Resource resource,
			TaskCallback<Long> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.resource = resource;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "resource", GsonHelper.GetCommonGson()
				.toJson(resource));
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/resource";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Long>() {
		}.getType();
	}

	@Override
	protected Long handleStringProxy(HttpResponse response) throws Exception {
		resource.id = super.handleStringProxy(response);
		Integer type = EBProfile.TYPE_CHANGE_SUPPLY;
		if (resource.type != null && resource.type == Resource.TYPE_DEMAND) {
			type = EBProfile.TYPE_CHANGE_DEMAND;
		}
		EBProfile eBProfile = new EBProfile(type, resource);
		EventBus.getDefault().post(eBProfile);
		return resource.id;
	}

}
