package com.zhisland.android.blog.invitation.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.impl.SearchModel;
import com.zhisland.android.blog.invitation.view.ISearchInvite;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Mr.Tui on 2016/8/9.
 */
public class SearchPresenter extends BasePresenter<SearchModel, ISearchInvite> {

    String curSearchKey;

    Subscription subscription;

    private Subscription showKeyboardDelayOb;

    boolean hasSetEmptyView = false;

    @Override
    public void bindView(@NonNull ISearchInvite view) {
        super.bindView(view);
        showKeyboardDelay(500);
    }

    @Override
    public void unbindView() {
        if (showKeyboardDelayOb != null && (!showKeyboardDelayOb.isUnsubscribed())) {
            showKeyboardDelayOb.unsubscribe();
        }
        super.unbindView();
    }

    public void onSearchClick(String searchKey) {
        view().showProgressDlg();
        search(searchKey, null);
    }

    public void loadResultMore(String nextId) {
        search(curSearchKey, nextId);
    }

    private void search(final String searchKey, String nextId) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = model().search(searchKey, nextId)
                .compose(this.<ZHPageData<InviteUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribeOn(getSchedulerSubscribe())
                .observeOn(getSchedulerSubscribe())
                .doOnNext(new Action1<ZHPageData<InviteUser>>() {
                    @Override
                    public void call(ZHPageData<InviteUser> inviteUserZHPageData) {
                        List<String> allNum = PhoneContactUtil.getAllPhoneNum();
                        if (inviteUserZHPageData != null && inviteUserZHPageData.data != null && allNum != null) {
                            for (InviteUser inviteUser : inviteUserZHPageData.data) {
                                if(allNum.contains(inviteUser.user.account)){
                                    inviteUser.inviteRegister = InviteUser.INVITE_FRIEND_OF_CONTACT;
                                }
                            }
                        }
                    }
                })
                .observeOn(getSchedulerObserver())
                .subscribe(new Subscriber<ZHPageData<InviteUser>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onSearchFailed(e);
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(ZHPageData<InviteUser> data) {
                        curSearchKey = searchKey;
                        view().onSearchSuccess(data);
                        view().hideProgressDlg();
                        if (!hasSetEmptyView) {
                            hasSetEmptyView = true;
                            view().setEmptyView();
                        }
                    }
                });
    }

    public void onItemRightBtnClick(final InviteUser inviteUser) {
        model().preConfirm(inviteUser.user.uid)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Object>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                        view().goToApproveHaiKe(inviteUser);
                    }
                });
    }

    private void showKeyboardDelay(long delay) {
        if (showKeyboardDelayOb != null && (!showKeyboardDelayOb.isUnsubscribed())) {
            showKeyboardDelayOb.unsubscribe();
        }
        showKeyboardDelayOb = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().showKeyboard();
                        showKeyboardDelayOb = null;
                    }
                });
    }

}
