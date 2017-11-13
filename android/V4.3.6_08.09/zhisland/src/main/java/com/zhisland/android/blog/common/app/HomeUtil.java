package com.zhisland.android.blog.common.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.igexin.sdk.PushManager;
import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.aa.controller.GuideRegister;
import com.zhisland.android.blog.aa.controller.KillSelfMgr;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.util.ZHNotifyManager;
import com.zhisland.android.blog.setting.eb.EBLogout;
import com.zhisland.im.BeemApplication;
import com.zhisland.lib.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * 通用的、易变的intent创建类
 *
 * @author arthur
 */
public class HomeUtil {

    public static final String INK_FEATURE_URI = "ink_feature_uri";
    public static final String URI_LOGOUT = "logout";
    public static final String URI_CLOSE_APP = "uri_close_app";

    /**
     * 跳转注册
     */
    public static void toRegisterAndFinishSelf(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ActGuide.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 创建到主页的intent
     */
    public static Intent createHomeIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TabHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    /**
     * 回到应用的首页
     *
     * @param context
     */
    public static void NavToHome(Context context) {
        Intent intent = HomeUtil.createHomeIntent(context);
        context.startActivity(intent);
    }

    /**
     * 清理缓存，关闭数据库
     */
    public static void ClearDataAndCache() {
        DBMgr.release();
        com.zhisland.im.data.DBMgr.release();
        PrefUtil.Instance().clearAll();
    }

    /**
     * 注册个推
     */
    public static void registerGeTui() {
        PushManager.getInstance().initialize(ZhislandApplication.APP_CONTEXT);
        boolean bindAlias = PushManager.getInstance().bindAlias(
                ZhislandApplication.APP_CONTEXT,
                PrefUtil.Instance().getUserId() + "");
        PushManager.getInstance().turnOnPush(ZhislandApplication.APP_CONTEXT);
    }

    /**
     * 将应用登出
     */
    public static void logout() {
        HomeUtil.ClearDataAndCache();
        BeemApplication.getInstance().stopXmpp(ZhislandApplication.APP_CONTEXT);
        PushManager.getInstance().turnOffPush(ZhislandApplication.APP_CONTEXT);
        EventBus.getDefault().post(new EBLogout());
        ZHNotifyManager.getInstance().cancelAll();
    }

    /**
     * 关闭app
     */
    public static void closeApp(Activity activity) {
        BeemApplication.getInstance().stopXmpp(activity);
        PushManager.getInstance().turnOffPush(ZhislandApplication.APP_CONTEXT);
        Intent intent = HomeUtil.createHomeIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HomeUtil.INK_FEATURE_URI, HomeUtil.URI_CLOSE_APP);
        activity.startActivity(intent);
    }

    /**
     * 进入APP 分发跳转
     */
    public static void dispatchJump(Activity activity) {
        // 判断是否显示过注册引导
        if (PrefUtil.Instance().getShowGuideRegister()) {
            boolean hasLogin = PrefUtil.Instance().hasLogin();
            if (hasLogin) {
                // 跳转到主页面
                Intent intent = HomeUtil.createHomeIntent(activity);
                Intent uriIntent = activity.getIntent();
                if (uriIntent != null) {
                    String uriBrowse = uriIntent.getStringExtra(AUriMgr.URI_BROWSE);
                    if (!StringUtil.isNullOrEmpty(uriBrowse)) {
                        intent.putExtra(AUriMgr.URI_BROWSE, uriBrowse);
                    }
                }
                activity.startActivity(intent);
                activity.finish();
            } else {
                // 判断杀死逻辑
                KillSelfMgr.getInstance().dispatch(activity);
                activity.finish();
            }
        } else {
            GuideRegister.invoke(activity);
            activity.finish();
        }

    }
}
