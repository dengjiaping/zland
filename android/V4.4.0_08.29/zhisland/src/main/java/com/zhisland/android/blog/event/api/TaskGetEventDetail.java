package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.StringUtil;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取活动详情
 */
public class TaskGetEventDetail extends TaskBase<Event, Object> {

	private long eventId;

	public TaskGetEventDetail(Object context, long eventId,
			TaskCallback<Event> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		this.get(null, null);
	}

	protected String getApiVersion() {
		return "1.1";
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Event>() {
		}.getType();
	}

	@Override
	protected Event handleStringProxy(HttpResponse response) throws Exception {
		Event result = super.handleStringProxy(response);
		if (!StringUtil.isNullOrEmpty(result.honorGuest)) {
			try {
				result.honorGuestList = GsonHelper.GetCommonGson().fromJson(
						result.honorGuest, new TypeToken<ArrayList<User>>() {
						}.getType());
			} catch (Exception e) {
			}
		}
		DBMgr.getMgr().getEventCacheDao().cachEventDetail(result);
		return result;
	}

}
