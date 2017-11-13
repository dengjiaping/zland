package com.zhisland.android.blog.freshtask.push;

import com.zhisland.android.blog.common.push.BasePushHandler;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.lib.component.application.ZHApplication;

import java.util.Map;

/**
 * 新手任务 push handler
 */
public class FreshTaskHandler extends BasePushHandler {

    @Override
    protected void handler(Map<String, String> maps) {
    }

    @Override
    protected boolean isNeedToSendNotify() {
        if (ZHApplication.getCurrentActivity() == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected String getUriString() {
        return null;
    }

    @Override
    protected int getNotifyId() {
        return NotifyTypeConstants.FRESH_TASK_FINISH;
    }

    @Override
    protected boolean isSendEventBusByType() {
        return true;
    }
}
