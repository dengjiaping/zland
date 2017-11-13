package com.zhisland.android.blog.event.push;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;

import java.util.Locale;
import java.util.Map;

/**
 * 活动有人报名 push handler
 */
public class EventApplicantHandler extends BasePushHandler {

	@Override
	protected void handler(Map<String, String> maps) {
		// 我发起的活动红点
		PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_MY_EVENT, 1);
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
        return NotifyTypeConstants.EventApplicant;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return true;
    }

}
