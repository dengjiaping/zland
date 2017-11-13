package com.zhisland.android.blog.event.push;

import java.util.Locale;
import java.util.Map;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.util.ZHNotifyManager;
import com.zhisland.android.blog.event.eb.EBEvent;

import android.app.PendingIntent;

import de.greenrobot.event.EventBus;

/**
 * 活动被官方下线 push handler
 */
public class EventCancelByOfficialToSignerHandler extends BasePushHandler {

	@Override
	protected void handler(Map<String, String> maps) {
		// 我报名活动红点
		PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_SIGN_EVENT, 1);
		EventBus.getDefault().post(new EBEvent(EBEvent.TYPE_EVENT_STATUS_CHANGED, null));
	}

	@Override
	protected boolean isNeedToSendNotify() {
		return true;
	}

    @Override
    protected String getUriString() {
        return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_EVENT_SIGNED_LIST);
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.EventCancelByOfficialToSigner;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return true;
    }

}
