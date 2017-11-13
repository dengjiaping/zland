package com.zhisland.android.blog.message.presenter;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.IInteractionModel;
import com.zhisland.android.blog.message.view.IInteractionView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * 互动消息主导器
 * Created by arthur on 2016/9/13.
 */
public class InteractionMessagePresenter extends BasePresenter<IInteractionModel, IInteractionView> {



    public void loadNormal() {
        loadData(null);
    }

    public void loadMore(String nextId) {
        loadData(nextId);
    }


    //请求列表数据
    private void loadData(final String nextId) {
        model().getInteractionMessageList(nextId, 20)
                .subscribeOn(getSchedulerSubscribe())
                .observeOn(getSchedulerObserver())
                .compose(this.<ZHPageData<Message>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<Message>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<Message> messages) {
                        view().onLoadSucessfully(messages);

                    }
                });
    }

    //用户信息被点击
    public void onUserClicked(User user) {
        view().gotoUser(user);
    }

    public void onFeedClicked(Feed feed) {
        view().gotoFeed(feed);
    }

}
