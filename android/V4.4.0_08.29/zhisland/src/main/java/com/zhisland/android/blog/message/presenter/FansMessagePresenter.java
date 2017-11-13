package com.zhisland.android.blog.message.presenter;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.message.model.IFansMessageModel;
import com.zhisland.android.blog.message.view.IFansMessageView;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import rx.Subscriber;

/**
 * Created by arthur on 2016/9/13.
 */
public class FansMessagePresenter extends BasePresenter<IFansMessageModel, IFansMessageView> {

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
                .compose(this.<ZHPageData<InviteUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<InviteUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<InviteUser> inviteUserZHPageData) {
                        view().onLoadSucessfully(inviteUserZHPageData);
                    }
                });
    }

    public void onRightClicked(final InviteUser inviteUser) {
        view().showProgressDlg();
        model().follow(inviteUser.user.uid, "NewlyFansList")
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
                        view().showToast("");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        view().hideProgressDlg();
                        inviteUser.state.setState(RelationConstants.FOLLOWED_TA);
                        inviteUser.state.setIsOperable(0);
                        inviteUser.state.setStateName("已关注");
                        view().updateItem(inviteUser);
                        view().showToast("");
                        EBRelation ebRelation = new EBRelation(EBRelation.TYPE_FOLLOW_TO, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }
}
