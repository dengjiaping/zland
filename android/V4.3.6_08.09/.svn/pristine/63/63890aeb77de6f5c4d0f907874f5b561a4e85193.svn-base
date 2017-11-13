package com.zhisland.android.blog.invitation.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.IReqApproveFriendsModel;
import com.zhisland.android.blog.invitation.view.IReqApproveFriendsView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by lipengju on 16/8/10.
 */
public class ReqApproveFriendsPresenter extends BasePresenter<IReqApproveFriendsModel, IReqApproveFriendsView> {
    @Override
    protected void updateView() {
        super.updateView();
        model().loadData()
                .subscribeOn(getSchedulerSubscribe())
                .subscribe(new Subscriber<List<InviteUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<InviteUser> inviteUsers) {
                        view().onLoadSucessfully(inviteUsers);
                    }
                });

    }

    //点击忽略
    public void onIgnoreClicked(final InviteUser user) {
        model().ignoreHaikeRequest(user.user)
                .compose(this.<Object>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        view().removeRequest(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    //点击批准
    public void onAllowClicked(InviteUser request) {
        view().gotoAllowHaike(request);
    }

}
