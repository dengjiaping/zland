package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.util.MLog;

/**
 * 获取现场活动签到的用户列表
 */
public class TaskSceneSignInUser extends TaskBase<ZHPageData<User>, Object> {

	private long eventId;
	private String nextId;

	public TaskSceneSignInUser(Object context, long eventId, String nextId,
			TaskCallback<ZHPageData<User>> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.nextId = nextId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		params = this.put(params, "eventId", eventId);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/scene/" + eventId + "/users";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<User>>() {
		}.getType();
	}
	
	@Override
	protected ZHPageData<User> handleStringProxy(HttpResponse response)
			throws Exception {
		String responseBody = convertToString(response);
		MLog.d("zhapp", responseBody);
		Type listType = this.getDeserializeType();
		ZHPageData<User> spg = GsonHelper.GetCommonGson().fromJson(responseBody,
				listType);
		if (spg != null) {
			ArrayList<User> users = spg.data;
			if (users != null) {
				List<IMContact> contacts = com.zhisland.im.data.DBMgr.getHelper()
						.getContactDao().getFriendRequests();
				
				for (User user : users) {
					boolean isFriend = com.zhisland.im.data.DBMgr.getHelper()
							.getContactDao().isFriend(user.uid);
					user.isFriend = isFriend ? IMContact.FRIEND_YES
							: IMContact.FRIEND_NO;
					
					if (contacts != null && contacts.size() > 0) {
						for (IMContact ic : contacts) {
							long requetUid = IMContact.parseUid(ic.jid);
							if (user.uid == requetUid) {
								user.isAddFriend = true;
								break;
							}
						}
					}
					
				}
			}
		}
		return spg;
	}
}
