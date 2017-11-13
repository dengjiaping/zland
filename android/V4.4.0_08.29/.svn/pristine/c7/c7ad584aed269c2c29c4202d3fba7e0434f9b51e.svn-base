package com.zhisland.android.blog.feed.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.IFeedListModel;
import com.zhisland.android.blog.feed.view.IMiniFeedListView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * 新鲜事列表主导器
 * Created by arthur on 2016/8/31.
 */
public class MiniFeedListPresenter<V extends IMiniFeedListView> extends BasePresenter<IFeedListModel, V> {


    //下拉刷新
    public void onPullDown() {
        loadData(null);
    }

    //上拉加载更多
    public void onPullUp(String nextId) {
        loadData(nextId);
    }

    //请求列表数据
    protected void loadData(final String nextId) {
        model().getMyPubFeedList(nextId, 20)
                .subscribeOn(getSchedulerSubscribe())
                .observeOn(getSchedulerObserver())
                .compose(this.<ZHPageData<Feed>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<Feed>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<Feed> timelineData) {
                        view().onLoadSucessfully(timelineData);
                    }
                });
    }


    @Override
    public void bindView(@NonNull final V view) {
        super.bindView(view);
        RxBus.getDefault().toObservable(Feed.class)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(Feed feed) {
                        if (feed.action == null)
                            return;

                        switch (feed.action) {
                            case ADD: {
                                view().insertItem(feed);
                                break;
                            }
                            case UPDATE: {
                                updateFeed(feed);
                                break;
                            }
                            case DELETE: {
                                deleteFeed(feed);
                                break;
                            }
                        }
                    }
                });
    }

    //更新feed
    private void updateFeed(Feed feed) {
        if (feed == null || feed.feedId == null)
            return;
        List<Feed> feeds = view().getDatas();
        Feed hittedItem = null;
        for (Feed item : feeds) {
            if (item.feedId.equals(feed.feedId)) {
                item.like = feed.like;
                item.forward = feed.forward;
                item.comment = feed.comment;
                item.forwardUser = feed.forwardUser;
                hittedItem = item;
                break;
            }
        }
        if (hittedItem != null) {
            view().updateItem(hittedItem);
        }

    }

    //更新feed
    private void deleteFeed(Feed feed) {
        if (feed == null || feed.feedId == null)
            return;
        List<Feed> feeds = view().getDatas();
        Feed hittedItem = null;
        for (Feed item : feeds) {
            if (item.feedId.equals(feed.feedId)) {
                hittedItem = item;
                break;
            }
        }
        if (hittedItem != null) {
            view().deleteItem(hittedItem);
        }

    }


}
