package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 搜索用户
 */
public class TaskSearchUsers extends TaskBase<ZHPageData<User>, Object> {

	private String keyword;
	private String nextId;

	public TaskSearchUsers(String keyword, Object context, String nextId,
			TaskCallback<ZHPageData<User>> responseCallback) {
		super(context, responseCallback);
		this.keyword = keyword;
		this.nextId = nextId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "nextId", nextId);
		params = this.put(params, "keyword", keyword);
		params = this.put(params, "count", 20);
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/search";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ZHPageData<User>>() {
		}.getType();
	}

	@Override
	protected ZHPageData<User> handleStringProxy(HttpResponse response)
			throws Exception {
		ZHPageData<User> result = super.handleStringProxy(response);
		if (result != null && result.data != null && result.data.size() > 0) {
			for (int i = 0; i < result.data.size(); i++) {
				IMContact contact = DBMgr.getHelper().getContactDao().getIMContactByUid(result.data.get(i).uid);
				if (contact != null && contact.isFriend == IMContact.FRIEND_YES) {
					result.data.get(i).isFriend = IMContact.FRIEND_YES;
					result.data.get(i).relationLevel = contact.relation;
				} else {
					result.data.get(i).isFriend = IMContact.FRIEND_NO;
				}
			}
		}
		return result;
	}
}
