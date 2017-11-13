package com.zhisland.android.blog.info.presenter;

import android.os.Bundle;

import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.InfoDetailModel;
import com.zhisland.android.blog.info.view.IInfoReadView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.StringUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 资讯阅读业务逻辑的主导器
 * Created by Mr.Tui on 2016/6/29.
 */
public class InfoReadPresenter extends BasePresenter<InfoDetailModel, IInfoReadView> {

    public static final int MODE_READ = 0;
    public static final int MODE_URL = 1;

    /**
     * 当前的模式，0：阅读模式，1：原网页模式
     */
    private int curMode = MODE_READ;

    private ZHInfo info;

    private Subscription loadFinishOb;
    private Subscription setBodyDelayOb;

    public void initData(ZHInfo defInfo) {
        this.info = defInfo;
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (info == null) {
            return;
        }
        ZHInfo infoLocal = getInfoDetailFromLocal(info.newsId);
        if (infoLocal != null) {
            info = infoLocal;
            refreshBody();
            refreshRecommendUserView();
            view().shareData(info);
        }
        getInfoDetailFromInternet(info.newsId);
    }

    @Override
    public void saveState(Bundle outState) {
        super.saveState(outState);
    }

    @Override
    public void unbindView() {
        cacheInfo();
        if (loadFinishOb != null && (!loadFinishOb.isUnsubscribed())) {
            loadFinishOb.unsubscribe();
        }
        if (setBodyDelayOb != null && (!setBodyDelayOb.isUnsubscribed())) {
            setBodyDelayOb.unsubscribe();
        }
        super.unbindView();
    }


    //=========public methods==========

    /**
     * 资讯内容加载完成。延迟0.5秒后显示并更新评论view。
     */
    public void onContentLoadFinish() {
        if (loadFinishOb != null && (!loadFinishOb.isUnsubscribed())) {
            loadFinishOb.unsubscribe();
        }
        loadFinishOb = Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().hideModeDlg();
                        view().onReadConentloaded();
                        loadFinishOb = null;
                    }
                });
    }


    /**
     * 点击切换阅读模式按钮
     */
    public void onReadModeClick() {
        if (curMode != MODE_READ) {
            curMode = MODE_READ;
            refreshBody();
        }
    }

    /**
     * 点击切换原网页模式按钮
     */
    public void onUrlModeClick() {
        if (curMode != MODE_URL) {
            curMode = MODE_URL;
            switchToUrlMode();
        }
    }

    /**
     * 取消切换到原网页模式。
     */
    public void onSwitchDlgBtnClicked() {
        if (setBodyDelayOb != null && (!setBodyDelayOb.isUnsubscribed())) {
            setBodyDelayOb.unsubscribe();
            setBodyDelayOb = null;
        }
        this.curMode = MODE_READ;
        refreshBody();
    }

    /**
     * 举报按钮被点击
     */
    public void onReportClicked() {
        view().gotoReport(info);
    }

    public void onFromClick() {
        if (info != null && info.recommendUser != null) {
            view().gotoProfileAct(info.recommendUser.uid);
        }
    }


    //=====================================

    /**
     * 从服务器获取资讯详情，成功后fill到view
     */
    private void getInfoDetailFromInternet(final long newsId) {
        model().getInfoDetail(newsId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHInfo>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ZHInfo data) {
                        info = data;

                        cacheInfo();
                        refreshBody();
                        refreshRecommendUserView();
                        view().shareData(data);

                    }
                });
    }

    /**
     * 从本地数据库获取资讯详情
     */
    private ZHInfo getInfoDetailFromLocal(long newsId) {
        return model().getCacheInfo(newsId);
    }

    /**
     * 保存info到本地数据库
     */
    private void cacheInfo() {
        model().cacheInfo(info);
    }


    /**
     * 切换到原网页模式
     */
    private void switchToUrlMode() {
        view().setSwitchUrl();
        view().hideListView();
//        view().showToReadDlg();
        refreshBody();
        if (setBodyDelayOb != null && (!setBodyDelayOb.isUnsubscribed())) {
            setBodyDelayOb.unsubscribe();
        }
        setBodyDelayOb = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        refreshBody();
                        setBodyDelayOb = null;
                    }
                });
    }


    /**
     * 根据当前info和curMode，更新 资讯内容view、阅读模式按钮view、官方原创View。
     */
    private void refreshBody() {
        if (info != null) {
            if (info.contentCollect != null) {
                if (!StringUtil.isNullOrEmpty(info.contentCollect.contentRead) && !StringUtil.isNullOrEmpty(info.srcUrl)) {
                    view().setSwitchShowStatue(true);
                    if (curMode == MODE_READ) {
                        view().setSwitchRead();
                        view().loadContentRead(info.contentCollect.contentRead);
                    } else {
                        view().setSwitchUrl();
                        view().loadContentUrl(info.srcUrl);
                    }
                } else {
                    view().setSwitchShowStatue(false);
                    if (!StringUtil.isNullOrEmpty(info.contentCollect.contentRead)) {
                        curMode = MODE_READ;
                        view().loadContentRead(info.contentCollect.contentRead);
                    }
                    if (!StringUtil.isNullOrEmpty(info.srcUrl)) {
                        curMode = MODE_URL;
                        view().loadContentUrl(info.srcUrl);
                    }
                }
            }
        }
    }

    /**
     * 设置推荐人view
     */
    private void refreshRecommendUserView() {
        if (info != null && info.recommendUser != null && !StringUtil.isNullOrEmpty(info.recommendUser.name)) {
            view().setFromName("来自 " + info.recommendUser.name + " 的推荐");
        } else {
            view().setFromName("");
        }
    }

}
