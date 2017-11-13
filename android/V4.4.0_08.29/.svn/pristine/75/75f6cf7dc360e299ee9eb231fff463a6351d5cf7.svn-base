package com.zhisland.android.blog.profile.push;

import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;

import java.util.Locale;
import java.util.Map;

/**
 * 收到对自己的评论Handler
 */
public class CommentReceivedHandler extends BasePushHandler {

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
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_COMMENT_LIST);
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.COMMENT_RECEIVED;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return false;
    }

}
