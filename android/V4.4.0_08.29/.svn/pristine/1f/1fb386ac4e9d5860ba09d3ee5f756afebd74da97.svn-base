package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.Pinyin4jUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 批量获取好友邀请列表
 */
public class TaskFetchInviteListByUid extends TaskBase<List<User>, Object> {

	public TaskFetchInviteListByUid(Context context,
			TaskCallback<List<User>> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		this.get(params, null);
	}

	@Override
	protected String getUrl() {
		return Config.getHostUrl() + "/relation/request";
	}

	@Override
	protected List<User> handleStringProxy(HttpResponse response)
			throws Exception {
		String responseBody = convertToString(response);
		MLog.d("zhapp", responseBody);
		Type listType = this.getDeserializeType();
		List<User> res = null;
		MLog.e("zhapp", "deserialize start");
		res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);

		if (res != null && res.size() > 0) {
			// store user to DB
			List<IMContact> contacts = new ArrayList<IMContact>();
			for (User user : res) {
				if(StringUtil.isNullOrEmpty(user.name)){
					continue;
				}
				com.zhisland.android.blog.common.dto.DBMgr.getMgr().getUserDao()
						.creatOrUpdateUserNotNull(user);

				IMContact ic = DBMgr.getHelper().getContactDao().getIMContactByUid(user.uid);
				if(ic == null){
					ic = new IMContact();
				}
				ic.jid = IMContact.buildJid(user.uid);
				ic.name = user.name.trim();
				ic.c = Pinyin4jUtil.getStringHeadChar(user.name.trim());
				ic.status = IMContact.STATUS_WAIT;
				ic.relation = IMContact.RELATION_NON;
				ic.isFriend = IMContact.FRIEND_NO;
				ic.version = user.version;
				ic.desc = user.introduce;
				ic.time = System.currentTimeMillis();

				contacts.add(ic);
			}
			DBMgr.getHelper().getContactDao().saveInviteContacts(contacts);
		}

		return res;
	}

	@Override
	protected Throwable handleFailureMessage(Throwable e, String responseBody) {
		return super.handleFailureMessage(e, responseBody);
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<List<User>>() {
		}.getType();
	}

}
