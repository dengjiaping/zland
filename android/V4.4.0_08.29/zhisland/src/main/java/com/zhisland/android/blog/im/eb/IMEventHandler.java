package com.zhisland.android.blog.im.eb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.aa.controller.SplashActivity;
import com.zhisland.android.blog.aa.controller.ActRegisterAndLogin;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.eb.EBXGPush;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.util.ZHNotifyManager;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMUser;
import com.zhisland.im.event.EventConnection;
import com.zhisland.im.event.EventMsg;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.ToastUtil;

import java.util.HashMap;

/**
 * 负责监听XMPP的各种eventbus事件,通常在Application中管理它
 *
 * @author arthur
 */
public class IMEventHandler {

    private Context context;

    private HashMap<Long, InviteNotify> inviteUids = new HashMap<Long, InviteNotify>();

    Handler handle;

    public IMEventHandler(Context context) {
        this.context = context;
        handle = new Handler();
    }

    /**
     * 监听xmpp长链接的状态通知
     *
     * @param event
     */
    public void onEventMainThread(EventConnection event) {
        switch (event.action) {
            case EventConnection.ACTION_CONFLICT: {
                // 账号在另外一个同resource设备登录了
                // 登出设备
                Activity latestActivity = ZhislandApplication.getCurrentActivity();

                if (latestActivity == null) {
                    PrefUtil.Instance().setUserID(0);
                    return;
                }

                if ((latestActivity instanceof SplashActivity)
                        || (latestActivity instanceof ActGuide)
                        || (latestActivity instanceof ActRegisterAndLogin))
                    break;

                ToastUtil.showShort("账号已在别处登录，请注意账号安全");
                HomeUtil.logout();
                break;
            }
            case EventConnection.ACTION_CONNECTED: {
                // TODO
            }
        }
    }



    /**
     * 信鸽Im相关
     */
    public void onEventMainThread(final EBXGPush eb) {
        if (eb.type == NotifyTypeConstants.IMRelation) {
            //TODO IM
        } else if (eb.type == NotifyTypeConstants.IMMessage) {
            String contactJid = IMUser.buildJid(eb.uid);
            IMUser c = DBMgr.getHelper().getUserDao()
                    .getByJid(contactJid);
            if (c == null) {
                return;
            }
            String contactName = c.name;
            Bitmap avatar = ImageWorkFactory.getCircleFetcher().getBitmap(
                    c.jid);
            PendingIntent notifIntent = ZHNotifyManager.getInstance()
                    .createChatIntent(context, c.jid);
            ZHNotifyManager.getInstance().notify(contactName, eb.msg, avatar,
                    1, notifIntent, ZHNotifyManager.NOTIFY_ID_IM_MSG);
        }
    }



    /**
     * 监听处理IM的新消息，在收到这个消息的时候，消息本身已经存储到本地数据库
     *
     * @param event
     */
    public void onEventMainThread(EventMsg event) {
        String contactJid = event.getJid();
        IMUser c = DBMgr.getHelper().getUserDao().getByJid(contactJid);
        if (c == null) {
            return;
        }
        String contactName = c.name;
        Bitmap avatar = ImageWorkFactory.getCircleFetcher().getBitmap(
                c.avatar);
        PendingIntent notifIntent = ZHNotifyManager.getInstance()
                .createChatIntent(context, c.jid);
        ZHNotifyManager.getInstance().notify(contactName, event.getMsgBody(),
                avatar, event.getUnreadCount(), notifIntent,
                ZHNotifyManager.NOTIFY_ID_IM_MSG);
    }

    private class InviteNotify {
        String title;
        String msg;

        public InviteNotify(String title, String msg) {
            this.title = title;
            this.msg = msg;
        }

    }
}
