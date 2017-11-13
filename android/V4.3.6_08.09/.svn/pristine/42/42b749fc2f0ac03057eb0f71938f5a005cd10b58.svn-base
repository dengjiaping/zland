package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.freshtask.model.impl.InviteRequestModel;
import com.zhisland.android.blog.freshtask.view.IInviteRequestView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * 新手任务 求邀请 升级 presenter
 */
public class InviteRequestPresenter extends BasePresenter<InviteRequestModel, IInviteRequestView> {

    @Override
    protected void updateView() {
        super.updateView();
        checkContactPower();
    }

    /**
     * 设置数据
     */
    public void setData(ZHPageData<InviteUser> datas) {
        if (setupDone()) {
            view().setData(datas);
        }
    }

    /**
     * 判断是否有通讯录权限，没有跳转无通讯录权限UI
     */
    public void checkContactPower() {
        if (PhoneContactUtil.hasContactData()) {
            loadNormal();
        } else {
            view().goToNoPower();
            view().finish();
        }
    }

    public void loadNormal() {
        final PhoneContactUtil.ContactResult<String> contacts = PhoneContactUtil.getChangeContact();
        model().requestInviteNormal(contacts.result)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<InviteUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<InviteUser>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadError(e);
                    }

                    @Override
                    public void onNext(ZHPageData<InviteUser> content) {
                        PhoneContactUtil.setLastTimestamp(contacts.timestamp);
                        InviteRequestPresenter.this.setData(content);
                        if (content != null && content.data != null && content.data.size() > 0) {
                            view().showContentView();
                        } else {
                            view().showEmptyView();
                        }
                    }
                });
    }

    public void loadMore(String nextId) {
        model().requestInviteMore(nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<InviteUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<InviteUser>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadError(e);
                    }

                    @Override
                    public void onNext(ZHPageData<InviteUser> content) {
                        view().loadDataSuccess(content);
                    }
                });
    }


    /**
     * 点击求邀请按钮，显示留言dialog
     */
    public void onClickInviteRequest(final InviteUser item) {
        view().showLeavingMsgDlg(item);
    }

    /**
     * 求邀请
     */
    public void requestInvite(final InviteUser item, String msg) {
        view().showProgress();
        model().requestInvite(item.user.uid, msg)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgress();
                    }

                    @Override
                    public void onNext(Void content) {
                        view().hideLeavingDlg();
                        view().hideProgress();
                        view().showShortToast("你的请求已发送");
                        item.state.setStateName("已发送");
                        item.state.setIsOperable(CustomState.CAN_NOT_OPERABLE);
                        view().notifyDataChange();
                    }
                });
    }

    /**
     * 点击完成按钮
     */
    public void onClickBottomBtn() {
        view().finish();
    }
}
