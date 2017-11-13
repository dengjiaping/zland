package com.zhisland.android.blog.common.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.analytics.MobclickAgent;
import com.zhisland.android.blog.BuildConfig;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.ActRegisterAndLogin;
import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.aa.controller.SplashActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.gson.GsonCreater;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.view.dlg.ConfirmDlgMgr;
import com.zhisland.android.blog.contacts.dto.Loc;
import com.zhisland.android.blog.contacts.eb.EBLoc;
import com.zhisland.android.blog.im.authority.AuthChatDetail;
import com.zhisland.android.blog.im.controller.ActChat;
import com.zhisland.android.blog.im.eb.IMEventHandler;
import com.zhisland.android.blog.setting.controller.FragSettings;
import com.zhisland.android.blog.tracker.presenter.TrackerPresenter;
import com.zhisland.android.blog.tracker.view.ITrackerView;
import com.zhisland.im.BeemApplication;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.authority.AuthorityMgr;
import com.zhisland.lib.component.application.AppConfig;
import com.zhisland.lib.component.application.EBAppBackAndFore;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.uri.IUriMgr;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.dialog.IConfirmDlgMgr;
import com.zhisland.lib.view.pulltorefresh.cache.IPullCache;
import com.zhisland.lib.view.pulltorefresh.cache.PullToRefreshCache;
import com.zxinsight.MLink;
import com.zxinsight.MWConfiguration;
import com.zxinsight.MagicWindowSDK;
import com.zxinsight.Session;

import java.io.Serializable;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ZhislandApplication extends ZHApplication implements
        AMapLocationListener, ITrackerView {

    public static String[] LAZY_WEB_FILES;
    public static final String LAZY_DIR = "baseweb";

    public static Loc LATEST_LOC = null;
    private int uploadTimes = 0;
    private long lastUploadTime;
    private boolean visiable = true;

    private LocationManagerProxy mLocationManagerProxy;
    private TrackerPresenter trackerPresenter;

    /**
     * 记录点击事件
     *
     * @param pageName 页面名字
     * @param alias    点击事件别名
     */
    public static void trackerClickEvent(String pageName, String alias) {
        if (APP_CONTEXT instanceof ZhislandApplication) {
            ((ZhislandApplication) APP_CONTEXT).trackerClick(pageName, alias, null);
        }
    }

    /**
     * 记录点击事件， 带参数
     *
     * @param pageName 页面名字
     * @param alias    点击事件别名
     * @param param    友盟统计参数
     */
    public static void trackerClickEvent(String pageName, String alias, String param) {
        if (APP_CONTEXT instanceof ZhislandApplication) {
            ((ZhislandApplication) APP_CONTEXT).trackerClick(pageName, alias, param);
        }
    }

    private void trackerClick(String pageName, String alias, String param) {
        if (trackerPresenter != null && StringUtil.isNullOrEmpty(pageName)) {
            trackerPresenter.logShareEvent(pageName, alias);
        }
        if (StringUtil.isNullOrEmpty(param)) {
            MobclickAgent.onEvent(APP_CONTEXT, alias);
        } else {
            MobclickAgent.onEvent(APP_CONTEXT, alias, param);
        }
    }

    /**
     * 进入某个页面
     *
     * @param pageName 页面名字
     */
    public static void trackerPageStart(String pageName) {
        if (APP_CONTEXT instanceof ZhislandApplication) {
            ((ZhislandApplication) APP_CONTEXT).fragOnCreate(pageName);
        }
    }

    /**
     * 退出某个页面
     *
     * @param pageName 页面名字
     */
    public static void trackerPageEnd(String pageName) {
        if (APP_CONTEXT instanceof ZhislandApplication) {
            ((ZhislandApplication) APP_CONTEXT).fragOnDestory(pageName);
        }
    }

    /**
     * 配置IM
     */
    public static void configXMPP() {
        if (APP_CONTEXT instanceof ZhislandApplication) {
            ((ZhislandApplication) APP_CONTEXT).configIM();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configAuth();
        configMW();
        Session.setAutoSession(this);
        configPullToRefresh();
        configIM();
        startLocation();
        configCanary();
        registerLifecycleCallbacks();
        configTracker();
        startTingYun();


        //TODO remove this when all request is sending with Retrofit
        GsonHelper.SetCommonGson(GsonCreater.CreateGson());
    }

    /**
     * 配置客户端的权限拦截
     */
    public void configAuth() {
        AuthorityMgr.Instance().register(ActChat.class.getName(), new AuthChatDetail());
    }

    /**
     * 启动听云
     */
    private void startTingYun() {
        NBSAppAgent.setLicenseKey(getString(R.string.tingyun_app_key)).start(this);
    }

    /**
     * 配置魔窗
     */
    private void configMW() {
        MWConfiguration config = new MWConfiguration(this);
        config.setSharePlatform(MWConfiguration.ORIGINAL)
                .setMLinkOpen();
        MagicWindowSDK.initSDK(config);

        MLink.getInstance(this).checkYYB();
    }

    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(this);
        super.onTerminate();
    }

    @Override
    public void fragOnCreate(String pageName) {
        if (trackerPresenter != null) {
            trackerPresenter.logPageStart(pageName);
        }
        MobclickAgent.onPageStart(pageName);
    }

    @Override
    public void fragOnDestory(String pageName) {
        if (trackerPresenter != null) {
            trackerPresenter.logPageEnd(pageName);
        }
        MobclickAgent.onPageEnd(pageName);
    }

    public void onEventMainThread(EBAppBackAndFore event) {
        if (event.getType() == EBAppBackAndFore.TYPE_CUT_FOREGROUND) {
            trackerPresenter.switchToForeground();
        } else {
            trackerPresenter.switchToBackground();
        }
    }

    private void configTracker() {
        trackerPresenter = new TrackerPresenter();
        trackerPresenter.bindView(this);
        trackerPresenter.setSchedulerSubscribe(Schedulers.io());
        trackerPresenter.setSchedulerObserver(Schedulers.io());
        trackerPresenter.setSchedulerMain(AndroidSchedulers.mainThread());
        EventBus.getDefault().register(this);
    }


    private void registerLifecycleCallbacks() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                // 踢出登录逻辑
                if (activity instanceof SplashActivity ||
                        activity instanceof ActGuide
                        || activity instanceof ActRegisterAndLogin) {
                    return;
                }
                if (PrefUtil.Instance().getUserId() < 1
                        || PrefUtil.Instance().getToken() == null) {
                    HomeUtil.logout();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    @Override
    public AppConfig getAppConfig() {
        AppConfig config = new AppConfig();
        config.setEnvType(Config.ENV_TYPE);
        config.setRootDir("zhislandapp");
        config.setSchema("zhisland");
        config.setUploadBaseUrl(Config.getUploadBaseUrl());
        return config;
    }

    @Override
    public IConfirmDlgMgr createConfirmDlgMgr(Context context) {
        return new ConfirmDlgMgr(context);
    }

    @Override
    public IUriMgr getUriMgr() {
        return AUriMgr.instance();
    }

    private void configCanary() {
        if (BuildConfig.DEBUG) {
//            BlockCanary.install(this, new AppBlockCanaryContext()).start();
//            LeakCanary.install(this);
        }
    }

    private void configPullToRefresh() {
        PullToRefreshCache.setCacheUtil(new IPullCache() {

            @Override
            public Object getCache(String cacheKey) {
                return DBMgr.getMgr().getCacheDao().get(cacheKey);
            }

            @Override
            public void cacheData(String cacheKey, Serializable data) {
                DBMgr.getMgr().getCacheDao().set(cacheKey, data);
            }
        });
    }

    /**
     * 初始化、配置IM
     */
    private void configIM() {
        BeemApplication.getInstance().setContext(this);
        IMEventHandler imEventHandler = new IMEventHandler(this);
        EventBus.getDefault().register(imEventHandler);
        BeemApplication.getInstance().setXmppConfig(Config.getIMDomain(),
                Config.getIMResource(), Config.getIMHost(), Config.getIMPort());
    }

    @Override
    public void sendException(String exception) {
        //TODO send exception to server
    }

    public void startLocation() {
        MLog.d(TAG, "start location");
        // 定时地位上传机制先去掉，代码先保留
        if (true) {
            return;
        }
        User user = DBMgr.getMgr().getUserDao().getSelfUser();
        if (user == null) {
            if (mLocationManagerProxy != null) {
                mLocationManagerProxy.removeUpdates(this);
                mLocationManagerProxy = null;
            }
            return;
        } else {
            if (user.invisible != null
                    && user.invisible == User.VALUE_INVISIBLE) {
                visiable = false;
            }
        }
        if (mLocationManagerProxy != null)
            return;
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 5, this);

        mLocationManagerProxy.setGpsEnable(true);
    }

    // ===============gps listeners start=============

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            // 获取位置信息
            Double geoLat = amapLocation.getLatitude();
            Double geoLng = amapLocation.getLongitude();
            MLog.d(TAG,
                    geoLat + " " + geoLng + " " + amapLocation.getAccuracy());

            final Loc loc = new Loc();
            loc.alt = amapLocation.getAltitude();
            loc.lat = amapLocation.getLatitude();
            loc.lon = amapLocation.getLongitude();
            loc.horAccuracy = amapLocation.getAccuracy();
            loc.verAccuracy = amapLocation.getAccuracy();

            boolean needUpload = false;
            if (visiable && PrefUtil.Instance().hasLogin()) {
                if (ZhislandApplication.LATEST_LOC == null) {
                    needUpload = true;
                } else if (System.currentTimeMillis() - lastUploadTime > Loc.UPLOAD_TIME) {
                    needUpload = true;
                } else {
                    float[] results = new float[1];
                    AMapLocation.distanceBetween(
                            ZhislandApplication.LATEST_LOC.lat,
                            ZhislandApplication.LATEST_LOC.lon, loc.lat,
                            loc.lon, results);
                    if (results[0] > Loc.UPLOAD_DISTANCE) {
                        needUpload = true;
                    }
                }
            }

            ZhislandApplication.LATEST_LOC = loc;
            PrefUtil.Instance().setLastLoc(
                    GsonHelper.GetCommonGson().toJson(loc));

            if (needUpload) {
                lastUploadTime = System.currentTimeMillis();
                ZHApis.getUserApi().updateLoc(this, ZhislandApplication.LATEST_LOC,
                        new TaskCallback<Object>() {

                            @Override
                            public void onSuccess(Object content) {
                                MLog.d(TAG, "update loc success");
                                if (uploadTimes == 0) {
                                    uploadTimes++;
                                }
                                EBLoc ebLoc = new EBLoc(loc, 0, System
                                        .currentTimeMillis());
                                DBMgr.getMgr()
                                        .getCacheDao()
                                        .set(FragSettings.KEY_DEBUG_LOC_INFO,
                                                ebLoc);
                                EventBus.getDefault().post(ebLoc);
                            }

                            @Override
                            public void onFailure(Throwable failture) {
                                MLog.d(TAG, "update loc failed");
                                EBLoc ebLoc = new EBLoc(loc, -2, System
                                        .currentTimeMillis());
                                DBMgr.getMgr()
                                        .getCacheDao()
                                        .set(FragSettings.KEY_DEBUG_LOC_INFO,
                                                ebLoc);
                                EventBus.getDefault().post(ebLoc);
                            }
                        });
            } else {
                EBLoc ebLoc = new EBLoc(loc, -3, System.currentTimeMillis());
                DBMgr.getMgr().getCacheDao()
                        .set(FragSettings.KEY_DEBUG_LOC_INFO, ebLoc);
                EventBus.getDefault().post(ebLoc);
            }
        } else {
            EBLoc ebLoc = new EBLoc(null, amapLocation != null ? amapLocation
                    .getAMapException().getErrorCode() : -1,
                    System.currentTimeMillis());
            DBMgr.getMgr().getCacheDao()
                    .set(FragSettings.KEY_DEBUG_LOC_INFO, ebLoc);
            EventBus.getDefault().post(ebLoc);
        }

    }


    // ===============gps listeners end=============

    @Override
    public void showProgressDlg() {

    }

    @Override
    public void showProgressDlg(String content) {
        //do nothing
    }

    @Override
    public void hideProgressDlg() {
//do nothing
    }

    @Override
    public void showToast(String toast) {
//do nothing
    }

    @Override
    public void gotoUri(String uri) {
        //do nothing
    }

    @Override
    public void showConfirmDlg(String cancel, String title, String okText, String noText, Object arg) {
//do nothing
    }

    @Override
    public void hideConfirmDlg(String cancel) {
//do nothing
    }

    @Override
    public void finishSelf() {
//do nothing
    }

    @Override
    public String getPageName() {
        return null;
    }
}
