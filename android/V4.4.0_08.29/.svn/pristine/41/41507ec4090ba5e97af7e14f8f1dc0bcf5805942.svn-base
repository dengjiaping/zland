package com.zhisland.android.blog.common.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.SplashActivity;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.push.PushDispatchActivity;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.im.data.IMUser;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.StringUtil;

import java.util.Locale;

/**
 * 通知管理类
 */
public class ZHNotifyManager {

    public static final int NOTIFY_ID_IM_RELATION = 1;
    public static final int NOTIFY_ID_IM_MSG = 2;
    public static final int NOTIFY_ID_PROFILE = 3;

    private static final Object lockObj = new Object();
    private static ZHNotifyManager instance;

    private NotificationManager notifyManager;

    public static ZHNotifyManager getInstance() {
        if (instance == null) {
            synchronized (lockObj) {
                if (instance == null) {
                    instance = new ZHNotifyManager();
                }
            }
        }
        return instance;
    }

    private ZHNotifyManager() {
        notifyManager = (NotificationManager) ZHApplication.APP_CONTEXT
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void cancelAll() {
        notifyManager.cancelAll();
    }

    public void cancel(int id) {
        notifyManager.cancel(id);
    }

    public void notify(String title, String msg, PendingIntent notifIntent,
                       int id) {
        NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(
                ZHApplication.APP_CONTEXT);
        ntfBuilder.setTicker(title).setContentTitle(title);
        ntfBuilder.setContentText(msg);
        ntfBuilder.setSmallIcon(R.drawable.icon);
        ntfBuilder.setAutoCancel(true).setWhen(System.currentTimeMillis());
        ntfBuilder.setContentIntent(notifIntent);
        Notification notif = ntfBuilder.getNotification();
        notif.ledARGB = 0xff0000ff; // Blue color
        notif.ledOnMS = 1000;
        notif.ledOffMS = 1000;
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.defaults = Notification.DEFAULT_SOUND;
        notifyManager.notify(id, notif);
    }

    public void notify(String title, String msg, Bitmap avatar, int number,
                       PendingIntent notifIntent, int id) {
        NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(
                ZHApplication.APP_CONTEXT);
        if (avatar != null) {
            ntfBuilder.setLargeIcon(avatar);
        }
        ntfBuilder.setTicker(title).setContentTitle(title);
        ntfBuilder.setContentText(msg);
        ntfBuilder.setSmallIcon(R.drawable.icon);
        ntfBuilder.setAutoCancel(true).setWhen(System.currentTimeMillis());
        ntfBuilder.setContentIntent(notifIntent);
        if (number > 1) {
            ntfBuilder.setNumber(number);
        }

        Notification notif = ntfBuilder.getNotification();
        notif.ledARGB = 0xff0000ff; // Blue color
        notif.ledOnMS = 1000;
        notif.ledOffMS = 1000;
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.defaults = Notification.DEFAULT_SOUND;
        notifyManager.notify(NOTIFY_ID_IM_MSG, notif);
    }

    /**
     * PendingIntent 通用uri点击时：APP在运行状态时，直接跳转对应界面。APP没有运行时先启动主页，再跳转对应界面
     */
    public PendingIntent createPendingIntent(String uriString, int flag) {
        PendingIntent notifyIntent;
        Intent intent;
        if (AppUtil.appIsRunning("")) {
            intent = new Intent(ZhislandApplication.APP_CONTEXT, PushDispatchActivity.class);
        } else {
            intent = new Intent(ZhislandApplication.APP_CONTEXT, SplashActivity.class);
        }
        if (!StringUtil.isNullOrEmpty(uriString)) {
            intent.putExtra(AUriMgr.URI_BROWSE, uriString);
        }
        notifyIntent = PendingIntent.getActivity(ZhislandApplication.APP_CONTEXT, 0,
                intent, flag);
        return notifyIntent;
    }

    /**
     * 创建到想认识我的人PendingIntent
     */
    public PendingIntent createAddFriendIntent() {
        String uriString = String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_ADD_FRIEND_REQUEST);
        return createPendingIntent(uriString, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * 创建IM聊天PendingIntent
     */
    public PendingIntent createChatIntent(Context context, String jid) {
        long uid = IMUser.parseUid(jid);
        // 格式： zhisland://com.zhisland/chat/single/10086
        String uriString = String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
                + "://%s/%s/%s", AUriMgr.AUTHORITY, AUriMgr.PATH_CHAT_SINGLE, uid);
        return createPendingIntent(uriString, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
