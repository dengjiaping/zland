package com.zhisland.android.blog.setting.api;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.upapp.ZHUpgrade;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 检查新版本
 */
public class TaskCheckVersion extends TaskBase<ZHUpgrade, Object> {

    private boolean isFromSplash = false;

	public TaskCheckVersion(Object context,
                            boolean isFromSplash, TaskCallback<ZHUpgrade> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
        this.isFromSplash = isFromSplash;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/settings/version";
	}

    @Override
    protected boolean isBackgroundTask() {
        return isFromSplash;
    }

    @Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHUpgrade>() {
		}.getType();
	}

}
