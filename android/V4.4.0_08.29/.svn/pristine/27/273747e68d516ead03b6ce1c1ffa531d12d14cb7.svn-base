package com.zhisland.android.blog.common.dto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.eb.EBUser;
import com.zhisland.android.blog.common.util.DBUtil;
import com.zhisland.im.data.ContactGroup;
import com.zhisland.im.data.IMChat;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * User Dao。user对象的数据库操作。 所有数据库操作查询到User后应该首先执行makeEntityByJson方法。
 * 所有数据库创建或更新User前应该执行makeJsonByEntity方法。
 * */
public class UserDao extends BaseDaoImpl<User, Long> {

	public User getUserById(long uid) {
		try {
			User user = queryForId(uid);
			user = User.makeEntityByJson(user.jsonBody);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将User对象中不为空的属性，保存到数据库。 注：会根据user的属性字段生成jsonBody字段，user原本的jsonBody不起作用。
	 * 如果想直接保存user的jsonBody请调用createOrUpdate方法。
	 * */
	public void creatOrUpdateUserNotNull(User user) {
		if (user == null) {
			return;
		}
		QueryBuilder<User, Long> qb = this.queryBuilder();
		try {
			User query = getUserById(user.uid);
			if (query != null) {
				DBUtil.updateFieldNotNull(query, user);
				query.jsonBody = User.makeJsonByEntity(query);
				update(query);
			} else {
				user.jsonBody = User.makeJsonByEntity(user);
				this.create(user);
			}
			if (user.uid == PrefUtil.Instance().getUserId()) {
				// 如果更新的是登录用户，发送EventBus。通知更新user信息。
				EventBus.getDefault().post(
						new EBUser(EBUser.TYPE_USER_SELF_INFO_CHANGED,
								getSelfUser()));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void createOrUpdateJson(long uid, String jsonStr) {
		try {
			User user = new User();
			user.uid = uid;
			user.jsonBody = jsonStr;
			createOrUpdate(user);
			if (user.uid == PrefUtil.Instance().getUserId()) {
				// 如果更新的是登录用户，发送EventBus。通知更新user信息。
				EventBus.getDefault().post(
						new EBUser(EBUser.TYPE_USER_SELF_INFO_CHANGED,
								getSelfUser()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询数据库中当前User对象，如果为空，则使用PrefUtil中当前用户的关键字段，并向接口查询更新user。 如果PrefUtil
	 * 中用户字段为空，则踢出用户
	 */
	public User getSelfUser() {
		long uid = PrefUtil.Instance().getUserId();
		if (uid > 0) {
			if (PrefUtil.Instance().needRefreshSelf()) {
				ZHApis.getUserApi().getUserDetail(null, uid, null);
			}
			User user = getUserById(uid);
			if (user == null) {
				String userJsonString = PrefUtil.Instance().getUserJsonString();
				if (!StringUtil.isNullOrEmpty(userJsonString)) {
					// 反序列号 user json串
					try {
						user = GsonHelper.GetCommonGson().fromJson(
								userJsonString, User.class);
					} catch (Exception e) {
						e.printStackTrace();
						user = User.combinationUser();
					}
				} else {
					user = User.combinationUser();
				}
				// 请求接口获取当前user
				ZHApis.getUserApi().getUserDetail(null, uid, null);
			}
			return user;
		} else {
			// 本地已没有用户信息，需踢出登录
			HomeUtil.logout();
		}
		return null;
	}

	public HashMap<String, User> getIconIdMap(
			ArrayList<ContactGroup> contactGroups) {
		HashMap<String, User> jidUserMap = new HashMap<String, User>();
		if (contactGroups != null) {
			User user;
			for (ContactGroup contactGroup : contactGroups) {
				ArrayList<IMContact> contacts = contactGroup.getChildren();
				if (contacts != null) {
					for (IMContact contact : contacts) {
						long uid = IMContact.parseUid(contact.jid);
						user = getUserById(uid);
						if (user != null) {
							jidUserMap.put(contact.jid, user);
						}
					}
				}
			}
		}
		return jidUserMap;
	}

	public void addIconIdMap(HashMap<String, User> jidUserMap,
			List<IMContact> data) {
		if (data != null && jidUserMap != null) {
			User user;
			for (IMContact contact : data) {
				long uid = IMContact.parseUid(contact.jid);
				user = getUserById(uid);
				if (user != null) {
					jidUserMap.put(contact.jid, user);
				}
			}
		}
	}

	public void addIconIdMap(HashMap<String, User> jidUserMap, IMContact data) {
		if (data != null && jidUserMap != null) {
			User user;
			long uid = IMContact.parseUid(data.jid);
			user = getUserById(uid);
			if (user != null) {
				jidUserMap.put(data.jid, user);
			}
		}
	}

	public void addIconIdMapOfChat(HashMap<String, User> jidUserMap,
			List<IMChat> chats) {
		if (chats != null && jidUserMap != null) {
			User user;
			for (IMChat chat : chats) {
				long uid = IMContact.parseUid(chat.contact);
				user = getUserById(uid);
				if (user != null) {
					jidUserMap.put(chat.contact, user);
				}
			}
		}
	}

	public void addIconIdMapOfChat(HashMap<String, User> jidUserMap, IMChat chat) {
		if (chat != null && jidUserMap != null) {
			User user;
			long uid = IMContact.parseUid(chat.contact);
			user = getUserById(uid);
			if (user != null) {
				jidUserMap.put(chat.contact, user);
			}
		}
	}

	public UserDao(Class<User> dataClass) throws SQLException {
		super(dataClass);
	}

	public UserDao(ConnectionSource connectionSource, Class<User> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	public UserDao(ConnectionSource connectionSource,
			DatabaseTableConfig<User> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

}
