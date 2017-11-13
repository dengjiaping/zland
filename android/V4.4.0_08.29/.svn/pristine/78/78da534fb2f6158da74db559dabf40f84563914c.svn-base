package com.zhisland.android.blog.feed.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.IFeedDetailModel;
import com.zhisland.android.blog.feed.view.IFeedDetailView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.rxjava.RxBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by arthur on 2016/9/6.
 */
public class FeedDetailPresenter extends FeedPresenter<IFeedDetailModel, IFeedDetailView> {

    private String feedId;
    private Feed feed;
    private boolean showSendCommentOnStart;
    private Subscription showSendCommentViewDelayOb;

    @Override
    public void bindView(@NonNull IFeedDetailView view) {
        super.bindView(view);
        RxBus.getDefault().toObservable(Feed.class)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(Feed feed) {
                        if (feed.action != null && feed.action == EbAction.UPDATE && feed.feedId.equals(feedId)) {
                            FeedDetailPresenter.this.feed = feed;
                            view().updateFeed(feed);
                        }
                    }
                });
    }

    public void setFeed(Feed feed, boolean showSendCommentOnStart) {
        if (feed == null)
            return;

        this.feedId = feed.feedId;
        this.showSendCommentOnStart = showSendCommentOnStart;
        this.feed = feed;
        if (setupDone()) {
            updateView();
        }
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
        if (setupDone()) {
            updateView();
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (feed != null) {
            view().updateFeed(feed);
        }
        if (feedId != null) {
            loadRemote();
        }
    }

    @Override
    public void onFeedClicked(Feed curFeed) {
        // avoiding goto another detail
    }

    @Override
    public void onCommentClicked(Feed curFeed) {
        view().showSendCommentView(feedId);
    }

    public void loadRemote() {
        //TODO 从服务器获取评论数据
        model().getFeedDetail(feedId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Feed>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiError) {
                            ApiError error = (ApiError) e;
                            boolean isDeleted = error.code == CodeUtil.CODE_RESOURCE_UNEXIST;
                            if (isDeleted) {
                                view().showDeletedView();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onNext(Feed data) {
                        FeedDetailPresenter.this.feed = data;
                        view().updateFeed(data);
                        view().onLoadFeedOk(data);
                        if (showSendCommentOnStart) {
                            showSendCommentOnStart = false;
                            showSendCommentViewDelay();
                        }
                    }
                });
    }

    //region 多功能按钮
    public void onMoreActionClick() {
        if (feed != null) {
            if (feed.user.uid == PrefUtil.Instance().getUserId()) {
                view().showDeteleFeedAction();
            } else {
                view().showReportFeedAction();
                getReportReasonInternet();
            }
        }
    }

    private void getReportReasonInternet() {
        model().getReportReasonInternet()
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ArrayList<Country>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ArrayList<Country>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<Country> data) {
                        model().saveReportReasonCache(data);
                    }
                });
    }

    public void onDeleteFeedClick() {
        view().showDeleteFeedDlg();
    }

    public void onReportFeedClick() {
        if (feed != null && feed.user != null) {
            ArrayList<Country> datas = model().getReportReasonCache();
            if (datas == null) {
                view().showToast("举报原因拉取失败");
                return;
            }
            view().showReportFeedDlg(feed.user, datas);
        }
    }

    public void onDeleteClicked() {
        view().showProgressDlg("删除中");
        model().deleteFeed(feed.feedId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().showToast("删除成功");
                        view().finishSelf();

                        feed.action = EbAction.DELETE;
                        RxBus.getDefault().post(feed);
                    }
                });
    }

    public void report(String reasonId) {
        view().showProgressDlg("正在提交");
        model().reportFeed(feed.feedId, reasonId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().showToast("举报成功");
                    }
                });
    }
    //endregion

    /**
     * 延迟delay毫秒显示评论编辑view。
     */
    private void showSendCommentViewDelay() {
        if (showSendCommentViewDelayOb != null && (!showSendCommentViewDelayOb.isUnsubscribed())) {
            showSendCommentViewDelayOb.unsubscribe();
        }
        showSendCommentViewDelayOb = Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().showSendCommentView(feedId);
                        showSendCommentViewDelayOb = null;
                    }
                });
    }

}
