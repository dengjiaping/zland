package com.zhisland.android.blog.tracker.presenter;

import com.umeng.analytics.MobclickAgent;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.tracker.view.ITrackerView;
import com.zhisland.android.blog.tracker.bean.TrackerEvent;
import com.zhisland.android.blog.tracker.bean.TrackerLevel;
import com.zhisland.android.blog.tracker.bean.TrackerType;
import com.zhisland.android.blog.tracker.model.TrackerModel;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.MLog;

import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 统计类
 * Created by 向飞 on 2016/5/24.
 */
public class TrackerPresenter extends BasePresenter<TrackerModel, ITrackerView> {

    private static final String TAG = "tracker";
    private static final int MAX_UPLOADS = 10;
    private String sessionId;//每次APP启动或者从后台切到前台都是一个新的session

    public TrackerPresenter() {
        this.setModel(new TrackerModel());
    }

    /**
     * 应用切到前台开始新session
     */
    public void switchToForeground() {
        startNewSession();
    }

    /**
     * 应用切到后台准备上传统计数据
     */
    public void switchToBackground() {
        startUpload();
    }

    /**
     * 进入某个页面
     *
     * @param pageName 页面名字
     */
    public void logPageStart(String pageName) {
        TrackerEvent event = new TrackerEvent(sessionId, TrackerLevel.PAGE, TrackerType.PAGE_IN, pageName, System.currentTimeMillis());
        model().logEvent(event);
    }

    /**
     * 退出某个页面
     *
     * @param pageName 页面名字
     */
    public void logPageEnd(String pageName) {
        TrackerEvent event = new TrackerEvent(sessionId, TrackerLevel.PAGE, TrackerType.PAGE_OUT, pageName, System.currentTimeMillis());
        model().logEvent(event);
    }

    /**
     * 记录点击事件
     *
     * @param pageName 页面名字
     * @param alias    点击事件别名
     */
    public void logShareEvent(String pageName, String alias) {
        TrackerEvent event = new TrackerEvent(sessionId, TrackerLevel.EVENT, null, pageName, System.currentTimeMillis());
        event.alias = alias;
        model().logEvent(event);
    }

    /**
     * 开始上传本地存储的统计日志，并在上传成功后删除本地缓存，否则保留日志等待下次上传
     */
    public void startUpload() {
        final List<TrackerEvent> ses = model().getUploadEvents(MAX_UPLOADS);
        if (ses == null || ses.size() < 1)
            return;

        final long maxId = ses.get(ses.size() - 1).time;
        model()
                .uploadEvent(ses, System.currentTimeMillis())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        MLog.d(TAG, "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.d(TAG, "");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        MLog.d(TAG, "上传成功" + maxId);
                        model().finishUpload(maxId);
                        startUpload();
                    }
                });

    }

    /**
     * 开始新的统计session
     */
    private void startNewSession() {
        sessionId = String.format("ts_%s_%d", AppUtil.Instance().getDeviceId(), System.currentTimeMillis());
    }

}
