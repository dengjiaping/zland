package com.zhisland.android.blog.event.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 报名Task
 * */
public class TaskSignUp extends TaskBase<PayData, Object> {

	private long eventId;
	private String userMobile;
	private String choice;
	private Integer invoiceOption;
	private String invoiceContacts;

	public TaskSignUp(Object context, long eventId, String userMobile,
			String choice, Integer invoiceOption,String invoiceContacts, TaskCallback<PayData> responseCallback) {
		super(context, responseCallback);
		this.eventId = eventId;
		this.userMobile = userMobile;
		this.choice = choice;
		this.invoiceOption = invoiceOption;
		this.invoiceContacts = invoiceContacts;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "eventId", eventId);
		params = this.put(params, "userMobile", userMobile);
		params = this.put(params, "choice", choice);
		if(invoiceOption != null){
			params = this.put(params, "invoiceOption", invoiceOption);
		}
		if(invoiceContacts != null){
			params = this.put(params, "invoiceContacts", invoiceContacts);
		}
		this.post(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/event/" + eventId + "/sign";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<PayData>() {
		}.getType();
	}

	@Override
	protected PayData handleStringProxy(HttpResponse response) throws Exception {
		return super.handleStringProxy(response);
	}
}
