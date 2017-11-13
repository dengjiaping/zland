package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.info.bean.RInfoPageData;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.eb.EBInfo;
import com.zhisland.android.blog.info.model.impl.RecommendInfoModel;
import com.zhisland.android.blog.info.view.IRecommendInfo;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class RecommendInfoPresenter extends BasePresenter<RecommendInfoModel, IRecommendInfo> {

    private static final String KEY_LAST_MAXID = "Recommend_Info_maxid";

    private long lastRefreshTiem;

    private String maxId;

    private Subscription hideDelayOb;
    private Subscription refreshOb;

    private Subscription infoSubscription;

    public RecommendInfoPresenter() {
        Object obj = DBMgr.getMgr().getCacheDao().get(KEY_LAST_MAXID + PrefUtil.Instance().getUserId());
        if (obj != null && obj instanceof String) {
            maxId = (String) obj;
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        registerInfoRxBus();
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (infoSubscription != null && (!infoSubscription.isUnsubscribed())) {
            infoSubscription.unsubscribe();
        }
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
        if (refreshOb != null && (!refreshOb.isUnsubscribed())) {
            refreshOb.unsubscribe();
        }
    }

    public void loadNormal() {
        getDataFromInternet(RInfoPageData.Type.top, maxId);
    }

    public void loadMore(String nextId) {
        getDataFromInternet(RInfoPageData.Type.next, nextId);
    }

    public void onFocused() {
        if (System.currentTimeMillis() - lastRefreshTiem > 1000 * 60 * 30) {
            refreshDelay(700);
        }
    }

    public void cacheData() {
        DBMgr.getMgr().getCacheDao().set(KEY_LAST_MAXID + PrefUtil.Instance().getUserId(), maxId);
        view().cacheListData();
    }

    private void registerInfoRxBus() {
        infoSubscription = RxBus.getDefault().toObservable(EBInfo.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBInfo>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBInfo>() {
                    @Override
                    public void call(EBInfo eb) {
                        if (eb.action == EBInfo.ACTION_UPDATA_COUNT && eb.info != null && eb.info.countCollect != null) {
                            List<ZHInfo> list = view().getCurListData();
                            if (list != null) {
                                for (ZHInfo info : list) {
                                    if (info.newsId == eb.info.newsId) {
                                        info.countCollect = eb.info.countCollect;
                                        view().refreshListView();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void getDataFromInternet(final RInfoPageData.Type type, String nextId) {
        lastRefreshTiem = System.currentTimeMillis();
        model().getRecommendList(type.name(), nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<RInfoPageData<ZHInfo>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<RInfoPageData<ZHInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("getRecommendList", "", e);
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(RInfoPageData<ZHInfo> data) {
                        data.nextId = data.minId;
                        if (type == RInfoPageData.Type.top) {
                            if(data.data == null || data.data.size() == 0){
                                maxId = null;
                                cacheData();
                            }else{
                                maxId = data.maxId;
                                if (data.unReadedCount > 0) {
                                    view().showPrompt(String.format("为您找到%d篇好文", data.unReadedCount));
                                } else {
                                    view().showPrompt(String.format("暂无内容更新", data.unReadedCount));
                                }
                                hidePromptDelay(2000);
                            }
                        }
                        view().onloadSuccess(data);
                    }
                });
    }

    private void hidePromptDelay(long delay) {
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
        hideDelayOb = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().hidePrompt();
                        hideDelayOb = null;
                    }
                });
    }

    private void refreshDelay(int delay) {
        if (refreshOb != null && (!refreshOb.isUnsubscribed())) {
            refreshOb.unsubscribe();
        }
        refreshOb = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().pullDownToRefresh(true);
                        refreshOb = null;
                    }
                });
    }
}
