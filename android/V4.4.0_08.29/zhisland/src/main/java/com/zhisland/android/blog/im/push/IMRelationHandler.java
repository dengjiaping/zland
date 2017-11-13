package com.zhisland.android.blog.im.push;

import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.lib.util.MLog;

import java.util.Map;

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
            //TODO IM
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
