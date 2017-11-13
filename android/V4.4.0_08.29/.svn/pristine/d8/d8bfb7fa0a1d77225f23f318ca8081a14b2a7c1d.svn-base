package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 删除供给
 */
public class TaskDeleteResource extends TaskBase<Object, Object> {

	private Resource resource;

	public TaskDeleteResource(Object context, Resource resource,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.resource = resource;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "urId", resource.id);
		this.delete(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/resource/" + resource.id;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		Object obj = super.handleStringProxy(response);
		Integer type = EBProfile.TYPE_CHANGE_SUPPLY;
		if (resource.type != null && resource.type == Resource.TYPE_DEMAND) {
			type = EBProfile.TYPE_CHANGE_DEMAND;
		}
		EBProfile eBProfile = new EBProfile(type, resource);
		EventBus.getDefault().post(eBProfile);
		return obj;
	}

}
