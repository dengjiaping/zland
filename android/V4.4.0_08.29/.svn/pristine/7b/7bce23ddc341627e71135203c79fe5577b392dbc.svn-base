package com.zhisland.android.blog.contacts.push;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.contacts.api.TaskGetFriendListByUid;
import com.zhisland.lib.util.StringUtil;

import java.util.Locale;
import java.util.Map;

/**
 * 邀请成功 自动成为好友 push handler
 */
public class InviteSuccessHandler extends BasePushHandler {

    public static final String INVITE_USER_ID = "invite_user_id";

    @Override
    protected void handler(Map<String, String> maps) {
        // 插入默认消息
        try {
            long fromUid = Long.parseLong(maps.get(FROM_UID_KEY));
            String value = PrefUtil.Instance().getByKey(INVITE_USER_ID, "");
            if (StringUtil.isNullOrEmpty(value)) {
                value = String.valueOf(fromUid);
            } else {
                value += "," + fromUid;
            }
            PrefUtil.Instance().setKeyValue(INVITE_USER_ID, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 拉取好友列表
        TaskGetFriendListByUid task = new TaskGetFriendListByUid(ZhislandApplication.APP_CONTEXT, null);
        task.execute();
    }

    @Override
    protected boolean isNeedToSendNotify() {
        return true;
    }

    @Override
    protected String getUriString() {
        return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_FRIEND_LIST);
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.INVITE_SUCCESS_TO_FRIEND;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return false;
    }
}
