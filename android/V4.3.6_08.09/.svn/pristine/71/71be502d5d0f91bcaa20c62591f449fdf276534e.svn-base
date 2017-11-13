package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.MLog;

/**
 * 现场活动想认识我的人
 */
public class TaskGetFriendRequest extends TaskBase<ArrayList<User>, Object> {

	private long eventId;

	public TaskGetFriendRequest(Object context, long eventId,
			TaskCallback<ArrayList<User>> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/scene/" + eventId + "/request";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<User>>() {
		}.getType();
	}

	@Override
	protected ArrayList<User> handleStringProxy(HttpResponse response)
			throws Exception {
		String responseBody = convertToString(response);
		MLog.d("zhapp", responseBody);
		Type listType = this.getDeserializeType();
		ArrayList<User> res = GsonHelper.GetCommonGson().fromJson(responseBody,
				listType);
		if (res != null) {
			for (User user : res) {
				user.isAddFriend = true;
			}
		}
		return res;
	}

}
