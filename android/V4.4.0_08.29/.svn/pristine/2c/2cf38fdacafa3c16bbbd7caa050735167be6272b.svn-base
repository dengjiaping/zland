package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 更新我的联系方式
 */
public class TaskUpdateContactInfo extends TaskBase<Object, Object> {

	private UserContactInfo userContactInfo;

	public TaskUpdateContactInfo(Object context,
			UserContactInfo userContactInfo,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		isPureRest = true;
		this.userContactInfo = userContactInfo;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "contact",
				GsonHelper.GetCommonGson().toJson(userContactInfo));
		this.put(params, null);
	}
	
	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user/contact";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		User self = DBMgr.getMgr().getUserDao().getSelfUser();
		self.name = userContactInfo.userName;
		self.userAvatar = userContactInfo.userAvatar;
		self.countryCodeShow = userContactInfo.countryCode;
		self.email = userContactInfo.email;
		self.userMobile = userContactInfo.mobile;
		DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(self);
		EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_CONTACT_INFO, userContactInfo);
		EventBus.getDefault().post(eBProfile);
		return super.handleStringProxy(response);
	}
}
