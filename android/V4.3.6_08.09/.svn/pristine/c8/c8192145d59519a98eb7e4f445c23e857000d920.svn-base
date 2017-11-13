package com.zhisland.android.blog.info.presenter;

import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.info.bean.SectionInfo;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.eb.EBInfo;
import com.zhisland.android.blog.info.model.impl.FeaturedInfoModel;
import com.zhisland.android.blog.info.view.IFeaturedInfo;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;

import java.util.ArrayList;
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
public class FeaturedInfoPresenter extends BasePresenter<FeaturedInfoModel, IFeaturedInfo> {

    /**
     * 上次刷新时间
     */
    private long lastRefreshTiem;

    /**
     * 自动刷新时，延迟700毫秒刷新
     */
    private Subscription refreshOb;

    /**
     * 资讯属性发生变化的观察者
     */
    private Subscription infoSubscription;

    @Override
    protected void updateView() {
        super.updateView();
        registerInfoRxBus();
    }

    @Override
    public void unbindView() {
        if (infoSubscription != null && (!infoSubscription.isUnsubscribed())) {
            infoSubscription.unsubscribe();
        }
        if (refreshOb != null && (!refreshOb.isUnsubscribed())) {
            refreshOb.unsubscribe();
        }
        super.unbindView();
    }

    public void getDataFromInternet(final String nextId) {
        lastRefreshTiem = System.currentTimeMillis();
        model().getFeaturedList(nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<SectionInfo>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<SectionInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<SectionInfo> data) {
                        ZHPageData<ZHInfo> pd = new ZHPageData<ZHInfo>();
                        pd.nextId = data.nextId;
                        pd.page_is_last = data.page_is_last;
                        if (data.data != null) {
                            ArrayList<SectionInfo> sectionInfos = data.data;
                            ArrayList<ZHInfo> infoDatas = new ArrayList<ZHInfo>();
                            for (SectionInfo si : sectionInfos) {
                                ArrayList<ZHInfo> infos = si.infos;
                                if (infos != null && infos.size() > 0) {
                                    infos.get(0).isFirst = true;
                                    infos.get(0).day = si.day;
                                    infos.get(infos.size() - 1).isLast = true;
                                    infoDatas.addAll(infos);
                                }
                            }
                            pd.data = infoDatas;
                        }
                        view().onloadSuccess(pd);
                    }
                });
    }

    /**
     * 当页面从不可见变为可见
     */
    public void onFocused() {
        if (System.currentTimeMillis() - lastRefreshTiem > 1000 * 60 * 30) {
            if (refreshOb != null && (!refreshOb.isUnsubscribed())) {
                refreshOb.unsubscribe();
            }
            refreshOb = Observable.timer(700, TimeUnit.MILLISECONDS)
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
}
