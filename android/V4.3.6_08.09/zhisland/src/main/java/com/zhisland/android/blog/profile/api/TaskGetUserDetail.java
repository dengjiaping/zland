package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.sql.SQLException;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.CacheDto;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.common.eb.EBUser;
import com.zhisland.im.data.IMChat;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.Pinyin4jUtil;

import de.greenrobot.event.EventBus;

/**
 * 获取用户信息
 * */
public class TaskGetUserDetail extends TaskBase<UserDetail, Object> {

	private long userId;

	public TaskGetUserDetail(Object context, long userId,
			TaskCallback<UserDetail> responseCallback) {
		super(context, responseCallback);
		this.userId = userId;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.get(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/center/" + userId;
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<UserDetail>() {
		}.getType();
	}

    @Override
    protected String getApiVersion() {
        return "1.1";
    }

    @Override
	protected String getBaseUrl() {
		return Config.getHttpsHostUrl();
	}

	@Override
	protected UserDetail handleStringProxy(HttpResponse response)
			throws Exception {
		
		handleHeaderInfo(response);
		String responseBody = convertToString(response);
		Type listType = this.getDeserializeType();
		UserDetail result = null;
		MLog.e("zhapp", "deserialize start");
		MLog.d("zhapp", "responseBody ： " + responseBody);
		result = UserDetail.getUserGson().fromJson(responseBody, listType);
		
		boolean isFrined = false;
		IMContact contact = null;
		User user = result.user;
		
		try {
			contact = com.zhisland.im.data.DBMgr.getHelper().getContactDao()
					.getByJid(IMContact.buildJid(user.uid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (contact != null && contact.isFriend == IMContact.FRIEND_YES) {
			isFrined = true;
		}
		if (isFrined) {
			user.isFriend = IMContact.FRIEND_YES;
			user.relationLevel = contact.relation;
		} else {
			user.isFriend = IMContact.FRIEND_NO;
		}
		// 保存当前用户信息
		if (user.uid == PrefUtil.Instance().getUserId()) {
			PrefUtil.Instance().setNeedRefrshSelf(false);
			User.saveSelfUserToPrefUtil(user);
			try {
				String body = GsonHelper.GetCommonGson().toJson(user);
				PrefUtil.Instance().setUserJsonString(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 插入数据库
		DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
		if (contact != null) {
			contact.name = user.name;
			contact.avatar = user.userAvatar;
			contact.c = Pinyin4jUtil.getStringHeadChar((user.name == null ? ""
					: user.name).trim());
			com.zhisland.im.data.DBMgr.getHelper().getContactDao()
					.update(contact);
		}

		IMChat chat = null;
		try {
			chat = com.zhisland.im.data.DBMgr.getHelper().getChatDao()
					.getChatByJid(IMContact.buildJid(user.uid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (chat != null) {
			chat.name = user.name;
			chat.avatar = user.userAvatar;
			com.zhisland.im.data.DBMgr.getHelper().getChatDao().update(chat);
		}
		// 保存公司信息
		for (int i = 0; i < user.companies.size(); i++) {
			CacheDto companyCache = new CacheDto();
			companyCache.key = user.companies.get(i).companyId + Company.POSTFIX;
			companyCache.value = user.companies.get(i);
			try {
				DBMgr.getMgr().getCacheDao().createOrUpdate(companyCache);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		EventBus.getDefault().post(
				new EBUser(EBUser.TYPE_GET_USER_DETAIL, user));
		return result;
	}
}
