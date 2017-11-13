package com.zhisland.android.blog.invitation.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.contacts.eb.EBContacts;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.android.blog.invitation.eb.EBInvite;
import com.zhisland.android.blog.invitation.model.IInvitationRequestModel;
import com.zhisland.android.blog.invitation.view.IInvitationRequestView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.rxjava.RxBus;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 主动邀请页面的邀请数字、跳转以及请求的业务处理
 * Created by arthur on 2016/8/10.
 */
public class InvitationRequestPresenter extends BasePresenter<IInvitationRequestModel, IInvitationRequestView> {

    private final String HAS_SHOW_FIRST_DLG = "has_show_first_invite_permissions";

    private InvitationData invitationData;

    @Override
    public void bindView(@NonNull IInvitationRequestView view) {
        super.bindView(view);
        EventBus.getDefault().register(this);
        RxBus.getDefault().toObservable(EBInvite.class)
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<EBInvite>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<EBInvite>() {
                    @Override
                    public void call(EBInvite ebInvite) {
                        if(ebInvite.action == EBInvite.ACTION_ALLOW_INVITE_REQUEST){
                            loadData();
                        }
                    }
                });
        boolean isAlreadyShow = PrefUtil.Instance().getByKey(HAS_SHOW_FIRST_DLG + PrefUtil.Instance().getUserId(), false);
        if (!isAlreadyShow) {
            view().showFirstDlg();
            PrefUtil.Instance().setKeyValue(HAS_SHOW_FIRST_DLG + PrefUtil.Instance().getUserId(), true);
        }
    }

    /**
     * Push 的 EventBus
     */
    public void onEventMainThread(EBNotify eb) {
        if (eb.notifyType == NotifyTypeConstants.INVITATION_REQUEST) {
            loadData();
        }
    }


    @Override
    protected void updateView() {
        super.updateView();
        loadData();
    }

    @Override
    public void unbindView() {
        EventBus.getDefault().unregister(this);
        super.unbindView();
    }

    //加载数据
    private void loadData() {
        model().getInvitationData()
                .compose(this.<InvitationData>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .subscribe(new Subscriber<InvitationData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiError) {
                            ApiError apiError = (ApiError) e;
                            if(apiError.code == CodeUtil.CODE_NO_PERMISSION){
                                view().finishSelf();
                            }
                        }
                    }

                    @Override
                    public void onNext(InvitationData inviteStructure) {
                        updateView(inviteStructure);
                    }
                });
    }

    //更新view
    private void updateView(InvitationData inviteStructure) {

        this.invitationData = inviteStructure;

        view().setCount(inviteStructure.total, inviteStructure.total - inviteStructure.useTotal);
        List<InviteUser> requests = inviteStructure.users;

        if (requests == null || requests.size() < 1) {
            view().hideRequest();
        } else {
            view().setRequestData(requests);
            view().setRequestTitle(getRequestTitle(requests.size()));
        }
    }

    //设置请求的标题
    private String getRequestTitle(int size) {
        return String.format("海客升级请求 %d", size);
    }

    //点击已批准
    public void onDealedClicked() {
        view().gotoDealedList();
    }

    //点击查看全部请求
    public void onAllRequestClicked() {
        view().gotoRequestList(invitationData.users);
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
                        removeRequest(user);
                        view().showToast("请求已忽略");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().showToast("请求忽略失败");
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    //移除请求
    private void removeRequest(InviteUser request) {
        if (view().removeRequest(request)) {
            if (invitationData.users == null || invitationData.users.size() < 1) {
                view().hideRequest();
            } else {
                view().setRequestTitle(getRequestTitle(invitationData.users.size()));
            }

            EBContacts eb = new EBContacts(EBContacts.TYPE_INVITATION_REQUEST_CHANGE, null);
            EventBus.getDefault().post(eb);
        }
    }

    //点击批准
    public void onAllowClicked(final InviteUser inviteUser) {
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

    public void onCardClick(InviteUser inviteUser){
        view().gotoActProfile(inviteUser.user.uid);
    }

    //处理与批准返回的error
    private void handleError(ApiError apiError) {
        switch (apiError.code) {
            case CodeUtil.CODE_INVITE_USER_IS_DAODING:
            case CodeUtil.CODE_INVITE_USER_IS_DAOQIN:
            case CodeUtil.CODE_INVITE_USER_IS_HAIKE: {
                //重新加载数据
                loadData();
                break;
            }
            case CodeUtil.CODE_INVITE_NO_COUNT: {
                //TODO
                break;
            }
        }
    }

}
