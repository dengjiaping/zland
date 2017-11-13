package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;
import java.net.HttpRetryException;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.apache.http.client.HttpResponseException;

/**
 * 用户收到的邀请请求列表
 */
public class TaskGetInvitationRequests extends
		TaskBase<ZHPageData<User>, Object> {

	private String nextId;

	public TaskGetInvitationRequests(Object context, String nextId,
			TaskCallback<ZHPageData<User>> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.nextId = nextId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/invitation/list";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<User>>() {
		}.getType();
	}

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

	@Override
	protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        if (e instanceof HttpResponseException){
            HttpResponseException exception = (HttpResponseException) e;
            if (exception.getStatusCode() != CodeUtil.CODE_NO_PERMISSION){
                return super.handleFailureMessage(e, responseBody);
            }
        }
        return null;
	}
}
