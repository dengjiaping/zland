package com.zhisland.android.blog.common.push;

import java.util.Map;

/**
 * 通用推送 push handler
 */
public class CommonPushHandler extends BasePushHandler {

    private String uriString;

    @Override
    protected void handler(Map<String, String> maps) {
        uriString = maps.get(BasePushHandler.URI);
    }

    @Override
    protected boolean isNeedToSendNotify() {
        return true;
    }

    @Override
    protected String getUriString() {
        return uriString;
    }

    @Override
    protected int getNotifyId() {
        return (int) Math.random();
    }

    @Override
    protected boolean isSendEventBusByType() {
        return false;
    }


}
