package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.Loc;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.async.http.task.ZHResponse;

/**
 * 上传位置
 */
public class TaskUpdateLoc extends TaskBase<Object, Object> {

	private Loc loc;

	public TaskUpdateLoc(Object context, Loc loc,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.loc = loc;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "location", GsonHelper.GetCommonGson()
				.toJson(loc));
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/lbs/position/report";
	}

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHResponse<Object>>() {
		}.getType();
	}

}
