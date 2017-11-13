package com.zhisland.android.blog.common.push;

import android.app.PendingIntent;

import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.util.ZHNotifyManager;
import com.zhisland.lib.util.StringUtil;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 推送handler基类
 */
public abstract class BasePushHandler {

    protected static final String TYPE_KEY = "type";
    protected static final String ASK_KEY = "ask";
    protected static final String MSG_KEY = "msg";
    protected static final String FROM_UID_KEY = "fromUid";
    protected static final String NAME_KEY = "name";
    protected static final String AVATAR_KEY = "avatar";
    protected static final String TITLE = "title";
    protected static final String CONTENT = "content";
    protected static final String URI = "uri";

    public BasePushHandler() {
    }

    public void onReceive(Map<String, String> maps) {
        handler(maps);

        if (isNeedToSendNotify()) {
            String msg = maps.get(CONTENT);
            if (!StringUtil.isNullOrEmpty(msg)) {
                PendingIntent pendingIntent = ZHNotifyManager.getInstance().
                        createPendingIntent(getUriString(), PendingIntent.FLAG_UPDATE_CURRENT);
                ZHNotifyManager.getInstance().notify("正和岛", msg, pendingIntent, getNotifyId());
            }
        }

        if (isSendEventBusByType()){
            // 发送对应 notifyID 的 eventbus
            EBNotify notify = new EBNotify();
            notify.notifyType = Integer.parseInt(maps.get(BasePushHandler.TYPE_KEY));
            EventBus.getDefault().post(notify);
        }
    }

    /**
     * 每个handler自己处理的逻辑
     */
    protected abstract void handler(Map<String, String> maps);

    /**
     * 是否需要发送notify
     */
    protected abstract boolean isNeedToSendNotify();

    /**
     * 获取uriString
     */
    protected abstract String getUriString();

    /**
     * 获取notify id
     */
    protected abstract int getNotifyId();

    /**
     * 是否 发送EventBus
     */
    protected abstract boolean isSendEventBusByType();
}
