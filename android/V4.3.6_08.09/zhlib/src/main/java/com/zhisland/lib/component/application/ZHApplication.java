/**
 *
 */
package com.zhisland.lib.component.application;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.umeng.analytics.MobclickAgent;
import com.zhisland.lib.async.http.AsyncHttpClient;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.application.UEHandler.ExceptionSender;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.ToastUtil;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * application基类，使用该库的项目中的application类必须从此类继承
 */
public abstract class ZHApplication extends MultiDexApplication implements
        ExceptionSender {

    public static AppConfig APP_CONFIG;
    public static Resources APP_RESOURCE = null;
    public static SharedPreferences APP_PREFERENCE;
    public static Context APP_CONTEXT = null;
    private Handler handler = new Handler();

    public static final String TAG = "zhapp";

    private UEHandler ueHandler;
    private static WeakReference<Activity> CUR_ACTIVITY = null;

    private int startedActivity = -1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Resources res = this.getResources();
        ZHApplication.APP_RESOURCE = res;
        APP_CONTEXT = this;
        APP_CONFIG = getAppConfig();

        ueHandler = new UEHandler((Context) this, (ExceptionSender) this);
        // 设置异常处理实例
        Thread.setDefaultUncaughtExceptionHandler(ueHandler);
        ImageWorkFactory.initFetchers();

        createPreference();

        // MobclickAgent.setDebugMode(!StaticWrapper.isRelease());
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        // 发送策略
        MobclickAgent.updateOnlineConfig(this);
    }

    /**
     * 仅仅用于统计，后期会将统计重构的library'中
     *
     * @param pageName
     */
    @Deprecated
    public abstract void fragOnCreate(String pageName);

    /**
     * 仅仅用于统计，后期会将统计重构的library'中 * @param pageName
     */
    @Deprecated
    public abstract void fragOnDestory(String pageName);


    public abstract AppConfig getAppConfig();

    public static Activity getCurrentActivity() {
        if (CUR_ACTIVITY != null) {
            return CUR_ACTIVITY.get();
        }

        return null;
    }

    /**
     * 从后台线程显示toast
     *
     * @param message
     */
    public static void ShowToastFromBackground(String message) {
        if (APP_CONTEXT instanceof ZHApplication) {
            ((ZHApplication) APP_CONTEXT).showToast(message);
        }
    }

    private void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(message);
            }
        });
    }

    public static void setCurrentActivity(Activity activity) {

        if (activity != null) {
            MLog.d(TAG, "set to " + activity.getClass().getName());
            CUR_ACTIVITY = new WeakReference<Activity>(activity);
        } else {
            MLog.d(TAG, "set to null");
            CUR_ACTIVITY = null;
        }
    }

    @Override
    public void onTerminate() {
        AsyncHttpClient.Factory.getSingletonInstance().getHttpClient()
                .getConnectionManager().shutdown();
        super.onTerminate();
    }

    private void createPreference() {
        ZHApplication.APP_PREFERENCE = this.getApplicationContext()
                .getSharedPreferences("zhisland-app", Context.MODE_PRIVATE);
    }

    public void startedActivityAdd() {
        if (startedActivity < 0) {
            //初始值为负数，小于0代表应用启动
            startedActivity = 1;
        } else if (startedActivity == 0) {
            //等于0代表从后台到前台。
            startedActivity = 1;
            EventBus.getDefault().post(new EBAppBackAndFore(EBAppBackAndFore.TYPE_CUT_FOREGROUND, null));
        } else {
            startedActivity = startedActivity + 1;
        }
    }

    public void startedActivityCut() {
        startedActivity = startedActivity - 1;
        if (startedActivity == 0) {
            //等于0代表从前台切到后台
            EventBus.getDefault().post(new EBAppBackAndFore(EBAppBackAndFore.TYPE_CUT_BACKGROUND, null));
        }
    }

    public int getStartedActivityCount() {
        return startedActivity;
    }
}
