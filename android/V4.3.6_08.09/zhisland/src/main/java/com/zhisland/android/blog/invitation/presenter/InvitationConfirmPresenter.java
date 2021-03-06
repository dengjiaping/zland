package com.zhisland.android.blog.invitation.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.eb.EBInvite;
import com.zhisland.android.blog.invitation.model.IInvitationConfirmModel;
import com.zhisland.android.blog.invitation.view.IInvitationRequestView;
import com.zhisland.android.blog.invitation.view.IInvitatoinConfirmView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.rxjava.RxBus;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 可认证好友列表逻辑
 * Created by arthur on 2016/8/10.
 */
public class InvitationConfirmPresenter extends BasePresenter<IInvitationConfirmModel, IInvitatoinConfirmView> {

    boolean deny = false;//通讯授权拒绝

    @Override
    public void bindView(@NonNull IInvitatoinConfirmView view) {
        super.bindView(view);
        RxBus.getDefault().toObservable(EBInvite.class)
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<EBInvite>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<EBInvite>() {
                    @Override
                    public void call(EBInvite ebInvite) {
                        if(ebInvite.action == EBInvite.ACTION_ALLOW_INVITE_REQUEST){
                            loadData(null, null);
                        }
                    }
                });
    }

    @Override
    protected void updateView() {
        super.updateView();
        uploadAndFetch();
    }

    @Override
    public void onVisible() {
        if (!deny)
            return;

        if (model().isContactAllowed()) {
            uploadAndFetch();
        }

    }

    //拿通讯录上传，并请求反馈列表
    private void uploadAndFetch() {
        PhoneContactUtil.ContactResult<String> contactData = model().getChangeContacts();
        loadData(contactData.result, null);
    }

    //加载更多
    public void loadMore(String nextId) {
        loadData(null, nextId);
    }

    //加载数据
    private void loadData(String data, final String nextId) {
        model().getConfirmableUsers(data, nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
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
                        onLoadDataOk(nextId, inviteUserZHPageData);
                    }
                });
    }

    private void onLoadDataOk(String nextId, ZHPageData<InviteUser> inviteUserZHPageData) {
        if (nextId != null
                || (inviteUserZHPageData != null && inviteUserZHPageData.data != null && inviteUserZHPageData.data.size() > 0)) {
            //获取更多，或者下拉刷新有数据返回直接填充数据
            view().hideEmptyView();
            view().hideContactDenyView();
            view().showListHeaderView();
            view().onLoadSucessfully(inviteUserZHPageData);
            if (inviteUserZHPageData.page_is_last) {
                view().disablePullUp();
            } else {
                view().enablePullUp();
            }
        } else {
            if (model().isContactAllowed()) {
                view().showListEmptyView();
            } else {
                deny = true;
                view().showContactDenyView();
            }
            view().disablePullUp();

        }
    }

    //点击可批准的搜所
    public void onSearchClicked() {
        view().gotoConfirmSearch();
    }

    //点击某一个用户的批准
    public void onConfirmClicked(final InviteUser inviteUser) {
        model().preConfirm(inviteUser.user.uid)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Object>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        view().gotoAllowHaike(inviteUser);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiError) {
                            ApiError apiError = (ApiError) e;
                            handleError(apiError);
                        }
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    private void handleError(ApiError apiError) {
        switch (apiError.code) {
            case CodeUtil.CODE_INVITE_USER_IS_DAODING:
            case CodeUtil.CODE_INVITE_USER_IS_DAOQIN:
            case CodeUtil.CODE_INVITE_USER_IS_HAIKE: {
                //重新加载数据
                loadData(null, null);
                break;
            }
            case CodeUtil.CODE_INVITE_NO_COUNT: {
                //TODO
                break;
            }
        }
    }
}
