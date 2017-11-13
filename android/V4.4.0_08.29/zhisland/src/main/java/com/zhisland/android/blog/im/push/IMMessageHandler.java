package com.zhisland.android.blog.im.push;

import java.util.Map;

import com.zhisland.android.blog.common.eb.EBXGPush;
import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.lib.util.MLog;

import de.greenrobot.event.EventBus;


/**
 * IM 消息 push handler
 */
public class IMMessageHandler extends BasePushHandler {

	private static final String TAG = "IMMessageHandler";

	@Override
	protected void handler(Map<String, String> maps) {
		long uid;
		try {
			uid = Long.parseLong(maps.get(FROM_UID_KEY));
		} catch (Exception ex) {
			MLog.e(TAG, "信鸽出错" + ex.getMessage());
			return;
		}
		EBXGPush eBXGPush = new EBXGPush();
		eBXGPush.msg = maps.get(CONTENT);
		eBXGPush.type = NotifyTypeConstants.IMMessage;
		eBXGPush.uid = uid;
		eBXGPush.title = maps.get(TITLE);
		EventBus.getDefault().post(eBXGPush);
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
        return NotifyTypeConstants.IMMessage;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return false;
    }
}
