package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.VoteTo;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取活动日程
 */
public class TaskGetEventVoteTo extends TaskBase<ArrayList<VoteTo>, Object> {

	private long eventId;

	public TaskGetEventVoteTo(Object context, long eventId,
			TaskCallback<ArrayList<VoteTo>> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		this.get(null, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/vote/" + eventId;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<ArrayList<VoteTo>>() {
		}.getType();
	}

	@Override
	protected ArrayList<VoteTo> handleStringProxy(HttpResponse response) throws Exception {
		ArrayList<VoteTo> result = super.handleStringProxy(response);
		return result;
	}

}
