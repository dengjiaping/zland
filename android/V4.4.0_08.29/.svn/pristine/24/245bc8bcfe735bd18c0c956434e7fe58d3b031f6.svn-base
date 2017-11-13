package com.zhisland.android.blog.message.presenter;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.ISystemMessageModel;
import com.zhisland.android.blog.message.view.ISystemMessageView;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * Created by arthur on 2016/9/13.
 */
public class SystemMessagePresenter extends BasePresenter<ISystemMessageModel, ISystemMessageView> {

    @Override
    protected void updateView() {
        super.updateView();
        //TODO 消去消息提示数
    }

    public void onLoadNormal() {
        loadData(null);
    }

    public void onLoadMore(String nextId) {
        loadData(nextId);
    }

    private void loadData(String nextId) {

        model().loadData(nextId, 20)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
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
                    public void onNext(ZHPageData<Message> inviteUserZHPageData) {
                        view().onLoadSucessfully(inviteUserZHPageData);
                    }
                });
    }

    public void onItemClicked(final Message item) {
        view().gotoUri(item.uri);
    }
}
