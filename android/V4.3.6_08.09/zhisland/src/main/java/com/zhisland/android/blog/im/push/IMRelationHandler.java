package com.zhisland.android.blog.im.push;

import com.beem.project.beem.service.pcklistener.FriendRelationJson;
import com.zhisland.android.blog.common.eb.EBXGPush;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.util.MLog;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * IM 好友关系 push handler
 */
public class IMRelationHandler extends BasePushHandler {

	private static final String TAG = "IMRelationHandler";

	@Override
	protected void handler(Map<String, String> maps) {
		int ask;
		try {
			ask = Integer.parseInt(maps.get(ASK_KEY));
		} catch (Exception ex) {
			MLog.e(TAG, "信鸽出错" + ex.getMessage());
			return;
		}
		long uid;
		try {
			uid = Long.parseLong(maps.get(FROM_UID_KEY));
		} catch (Exception ex) {
			MLog.e(TAG, "信鸽出错" + ex.getMessage());
			return;
		}

		if (uid > 0 && ask > 0) {

			FriendRelationJson fit = new FriendRelationJson();
			fit.fromJid = IMContact.buildJid(uid);
			fit.name = maps.get(NAME_KEY);
			fit.avatar = maps.get(AVATAR_KEY);
			fit.ask = ask;
			DBMgr.getHelper().getContactDao().saveInviteMsg(fit);

			if (ask == FriendRelationJson.ASK_ACCEPTED) {
				EBXGPush eBXGPush = new EBXGPush();
				eBXGPush.ask = ask;
				eBXGPush.type = NotifyTypeConstants.IMRelation;
				eBXGPush.uid = uid;
				eBXGPush.title = maps.get(CONTENT);
				EventBus.getDefault().post(eBXGPush);
			} else if (ask == FriendRelationJson.ASK_REQUEST) {
				String msg = maps.get(MSG_KEY);
				EBXGPush eBXGPush = new EBXGPush();
				eBXGPush.ask = ask;
				eBXGPush.msg = msg;
				eBXGPush.type = NotifyTypeConstants.IMRelation;
				eBXGPush.uid = uid;
				eBXGPush.title = maps.get(CONTENT);
				EventBus.getDefault().post(eBXGPush);
			}
		}

	}

	@Override
	protected boolean isNeedToSendNotify() {
		return false;
	}

    @Override
    protected String getUriString() {
        return null;
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.IMRelation;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return false;
    }

}
