package com.zhisland.android.blog.common.util;

import android.content.Context;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.im.BeemApplication;
import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.util.MLog;

public class IMUtil {


    /**
     * 设置xmpp的账号信息
     */
    public static synchronized void setXMppConfig() {
        MLog.d(ZhislandApplication.TAG, "start set xmpp account");
        long uid = PrefUtil.Instance().getUserId();
        MLog.d(ZhislandApplication.TAG, "set xmpp account ok");
        BeemApplication.getInstance().setXmppAccount(uid,
                PrefUtil.Instance().getUserName(),
                PrefUtil.Instance().getToken(),
                PrefUtil.Instance().getUserAvatar());
    }

    /**
     * 启动xmpp
     */
    public static synchronized void startXmpp(final Context context) {
        ThreadManager.instance().execute(
                new PriorityFutureTask<Void>(new Runnable() {
                    @Override
                    public void run() {
                        BeemApplication.getInstance().startXmpp(context);
                    }
                }, null, ThreadManager.THREAD_PRIORITY_HIGH));
    }

    /**
     * 检查 XmppAccount是否为空，如果为空就重新配置并启动BeemService
     */
    public static synchronized void checkIM(Context context) {
        if (BeemApplication.getInstance() == null
                || BeemApplication.getInstance().getXmppAccount() == null) {

            if (PrefUtil.Instance().getUserId() > 0) {
                setXMppConfig();
                startXmpp(context);
            }
        }
    }
}
