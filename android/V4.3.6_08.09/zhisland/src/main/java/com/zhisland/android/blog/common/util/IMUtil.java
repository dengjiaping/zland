package com.zhisland.android.blog.common.util;

import java.util.List;

import android.content.Context;

import com.beem.project.beem.service.pcklistener.FriendRelationJson;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.contacts.api.TaskGetFriendListByUid;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.MLog;

public class IMUtil {

	/**
	 * 加好友逻辑
	 * 
	 * @param uidTo
	 *            对方的uid
	 * @return 操作是否成功
	 */
	public static boolean addFriend(long uidTo) {
		String content = null;
		String toJid = IMContact.buildJid(uidTo);
		String fromJid = IMContact.buildJid(PrefUtil.Instance().getUserId());
		FriendRelationJson fit = new FriendRelationJson();
		fit.toJid = toJid;
		fit.fromJid = fromJid;
		fit.message = "";
		fit.name = PrefUtil.Instance().getUserName();
		fit.avatar = PrefUtil.Instance().getUserAvatar();
		fit.atk = PrefUtil.Instance().getToken();
		fit.ask = FriendRelationJson.ASK_REQUEST;

		try {
			content = GsonHelper.GetCommonGson().toJson(fit);
			boolean result = BeemApplication.getInstance().getXmppFacade()
					.addFriend(content);
			return result;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 解除好友关系
	 * 
	 * @param uidTo
	 *            对方的uid
	 * @return 操作是否成功
	 */
	public static boolean blockFriend(long uidTo) {
		String jid = IMContact.buildJid(uidTo);
		boolean result = false;
		try {
			result = BeemApplication.getInstance().getXmppFacade()
					.blockFriend(jid);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 标记为普通好友
	 * 
	 * @param uidTo
	 *            对方的uid
	 * @return 操作是否成功
	 */
	public static boolean downgradeFriend(long uidTo) {
		String jid = IMContact.buildJid(uidTo);
		boolean result = false;
		try {
			result = BeemApplication.getInstance().getXmppFacade()
					.downgradeFriend(jid);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 标记为信任好友
	 * 
	 * @param uidTo
	 *            对方的uid
	 * @return 操作是否成功
	 */
	public static boolean promoteFriend(long uidTo) {
		String jid = IMContact.buildJid(uidTo);
		boolean result = false;
		try {
			result = BeemApplication.getInstance().getXmppFacade()
					.promoteFriend(jid);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 接受对方成为好友
	 * 
	 * @param uidTo
	 *            对方的uid
	 * @return 操作是否成功
	 */
	public static boolean acceptFriend(long uidTo) {
		String jid = IMContact.buildJid(uidTo);
		boolean result = false;
		try {
			result = BeemApplication.getInstance().getXmppFacade()
					.acceptFriend(jid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result) {
			TaskGetFriendListByUid task = new TaskGetFriendListByUid(
					ZhislandApplication.APP_CONTEXT, null);
			task.execute();
		}
		return result;
	}

	// 是否是请求好友
	public static boolean isRequestFriend(long uid) {
		List<IMContact> contacts = com.zhisland.im.data.DBMgr.getHelper()
				.getContactDao().getFriendRequests();
		if (contacts != null && contacts.size() > 0) {
			for (IMContact ic : contacts) {
				long requetUid = IMContact.parseUid(ic.jid);
				if (requetUid == uid) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 设置xmpp的账号信息
	 */
	public static synchronized void setXMppConfig() {
		MLog.d(ZhislandApplication.TAG, "start set xmpp account");
		long uid = PrefUtil.Instance().getUserId();
		MLog.d(ZhislandApplication.TAG, "set xmpp account ok");
		BeemApplication.getInstance().setXmppAccount(uid,
				PrefUtil.Instance().getUserName(),
				PrefUtil.Instance().getToken(),
				PrefUtil.Instance().getUserAvatar());
	}

	/**
	 * 启动xmpp
	 */
	public static synchronized void startXmpp(final Context context) {
		ThreadManager.instance().execute(
				new PriorityFutureTask<Void>(new Runnable() {
					@Override
					public void run() {
						BeemApplication.getInstance().startXmpp(context);
					}
				}, null, ThreadManager.THREAD_PRIORITY_HIGH));
	}

	/**
	 * 检查 XmppAccount是否为空，如果为空就重新配置并启动BeemService
	 */
	public static synchronized void checkIM(Context context) {
		if (BeemApplication.getInstance() == null
				|| BeemApplication.getInstance().getXmppAccount() == null) {

			if (PrefUtil.Instance().getUserId() > 0) {
				setXMppConfig();
				startXmpp(context);
			}
		}
	}
}
