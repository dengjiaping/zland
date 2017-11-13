package com.zhisland.android.blog.contacts.push;

import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;

import java.util.Locale;
import java.util.Map;

/**
 * 用户求邀请，发送给被求人 push handler
 */
public class InvitationRequestHandler extends BasePushHandler {

	@Override
	protected void handler(Map<String, String> maps) {
	}

	@Override
	protected boolean isNeedToSendNotify() {
		return true;
	}

    @Override
    protected String getUriString() {
        return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_ACTIVE_INVITE);
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.INVITATION_REQUEST;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return true;
    }
}
