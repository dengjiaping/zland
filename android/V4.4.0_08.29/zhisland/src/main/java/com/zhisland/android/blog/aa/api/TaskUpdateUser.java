package com.zhisland.android.blog.aa.api;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 更新User信息 Task
 * */
public class TaskUpdateUser extends TaskBase<Object, Object> {

	/**
	 * 只用于登录注册 完善基本信息
	 */
	public static final int TYPE_UPDATE_ONE = 1;
	/**
	 * 只用于登录注册 上传形象照
	 */
	public static final int TYPE_UPDATE_THREE = 3;
	/**
	 * 其它场景通用type
	 */
	public static final int TYPE_UPDATE_OTHER = 2;

	private User user;
	private int from;

	public TaskUpdateUser(Object context, User user, int from,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.user = user;
		this.from = from;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		if (user.vipAvatarService != null) {
			params = this
					.put(params, "vipAvatarService", user.vipAvatarService);
			user.vipAvatarService = null;
		}
		if (user.assistantMobile != null) {
			params = this.put(params, "assistantMobile", user.assistantMobile);
			user.assistantMobile = null;
		}
		params = this.put(params, "user",
				GsonHelper.GetCommonGson().toJson(user));
		params = this.put(params, "isRegister", from);
		this.put(params, null);
	}

	@Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected String getPartureUrl() {
		return "/user";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
		if (user.name != null) {
			PrefUtil.Instance().setUserName(user.name);
		}
		if (user.userAvatar != null) {
			PrefUtil.Instance().setUserAvatar(user.userAvatar);
		}
		return super.handleStringProxy(response);
	}
}
