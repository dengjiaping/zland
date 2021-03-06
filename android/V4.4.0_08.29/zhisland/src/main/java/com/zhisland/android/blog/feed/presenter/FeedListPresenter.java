package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.TimelineData;
import com.zhisland.android.blog.feed.view.IFeedListView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.util.StringUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 新鲜事列表主导器
 * Created by arthur on 2016/8/31.
 */
public class FeedListPresenter extends MiniFeedListPresenter<IFeedListView> {



    //请求列表数据
    @Override
    protected void loadData(final String nextId) {
        model().getFeedList(nextId)
                .subscribeOn(getSchedulerSubscribe())
                .observeOn(getSchedulerObserver())
                .compose(this.<TimelineData>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<TimelineData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(TimelineData timelineData) {
                        view().onLoadSucessfully(timelineData.feeds);
                        if (StringUtil.isNullOrEmpty(nextId)) {
                            view().setRecommendData(timelineData.recommendData);
                            view().setIntrestableUsers(timelineData.intrestableUsers);
                            if (timelineData != null && timelineData.count != null && !StringUtil.isNullOrEmpty(timelineData.count.content)) {
                                String toastText = timelineData.count.content;
                                view().showFeedToast(toastText);
                                Observable.timer(2000, TimeUnit.MILLISECONDS)
                                        .observeOn(getSchedulerMain())
                                        .compose(FeedListPresenter.this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                                        .subscribe(new Action1<Long>() {
                                            @Override
                                            public void call(Long aLong) {
                                                view().hideFeedToast();
                                            }
                                        });
                            } else {
                                view().hideFeedToast();
                            }
                        }
                    }
                });
    }

    //发布按钮嗲你就
    public void onPublishClicked() {
        User self = DBMgr.getMgr().getUserDao().getSelfUser();
        if (self.isYuZhuCe()) {
            view().showPermissionsDialog();
        } else {
            view().showPublishView();
        }
    }


}
