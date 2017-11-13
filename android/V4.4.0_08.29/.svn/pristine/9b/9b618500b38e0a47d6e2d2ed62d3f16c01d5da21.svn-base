package com.zhisland.android.blog.invitation.presenter;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.android.blog.invitation.model.IInvitationDealedModel;
import com.zhisland.android.blog.invitation.view.IInvitationDealedView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;


import java.util.List;

import rx.Subscriber;

/**
 * 已批准列表
 * Created by arthur on 2016/8/9.
 */
public class InvitationDealedListPresenter extends BasePresenter<IInvitationDealedModel, IInvitationDealedView> {

    //region 生命周期事件

    @Override
    protected void updateView() {
        super.updateView();
    }


    @Override
    public void unbindView() {
        super.unbindView();
    }

    //endregion

    //region view 的交互事件

    public void loadData() {
        model().loadData()
                .compose(this.<InvitationData>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .subscribe(new Subscriber<InvitationData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(InvitationData userList) {
                        updateView(userList);
                    }
                });
    }

    private void updateView(InvitationData inviteStructure) {
        view().onLoadSucessfully(inviteStructure.users);
        int cost = inviteStructure.useTotal;
        String titleString = String.format("消耗批准名额%d人", cost);
        view().updateTitleString(titleString);
    }

    /**
     * 单条数据点击
     *
     * @param user
     */
    public void onItemClicked(InviteUser user) {
        view().gotoUserDetail(user.user);
    }


    //endregion


}
