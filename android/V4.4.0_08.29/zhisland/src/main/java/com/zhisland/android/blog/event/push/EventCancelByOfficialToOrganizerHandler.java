package com.zhisland.android.blog.event.push;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.event.eb.EBEvent;

import java.util.Locale;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 活动被官方强制下线，发送给发起人 push handler
 */
public class EventCancelByOfficialToOrganizerHandler extends BasePushHandler {

	@Override
	protected void handler(Map<String, String> maps) {
		// 我发起的活动红点
		PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_MY_EVENT, 1);
		EventBus.getDefault().post(new EBEvent(EBEvent.TYPE_EVENT_STATUS_CHANGED, null));
	}

	@Override
	protected boolean isNeedToSendNotify() {
		return true;
	}

    @Override
    protected String getUriString() {
        return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_EVENT_CREATED_LIST);
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.EventCancelByOfficialToOrganizer;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return true;
    }

}
