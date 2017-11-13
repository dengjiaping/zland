package com.zhisland.android.blog.invitation.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.contacts.eb.EBContacts;
import com.zhisland.android.blog.info.eb.EBComment;
import com.zhisland.android.blog.invitation.eb.EBApproveUser;
import com.zhisland.android.blog.invitation.eb.EBInvite;
import com.zhisland.android.blog.invitation.model.impl.ApproveHaiKeModel;
import com.zhisland.android.blog.invitation.view.IApproveHaiKe;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.StringUtil;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * Created by Mr.Tui on 2016/8/9.
 */
public class ApproveHaiKePresenter extends BasePresenter<ApproveHaiKeModel, IApproveHaiKe> {

    private final String HAS_SHOW_FIRST_APPROVAL_NOTES = "has_show_first_approval_notes";

    private boolean reliableSelect = false;
    private boolean commentSelect = false;
    private InviteUser inviteUser;

    public void setInviteUser(InviteUser inviteUser) {
        this.inviteUser = inviteUser;
    }

    @Override
    public void bindView(@NonNull IApproveHaiKe view) {
        super.bindView(view);
        boolean isAlreadyShow = PrefUtil.Instance().getByKey(HAS_SHOW_FIRST_APPROVAL_NOTES + PrefUtil.Instance().getUserId(), false);
        if (!isAlreadyShow) {
            view().showAllowHaiKeDialog();
            PrefUtil.Instance().setKeyValue(HAS_SHOW_FIRST_APPROVAL_NOTES + PrefUtil.Instance().getUserId(), true);
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        view().updateView(inviteUser);
    }

    public void onReliableClick() {
        if (reliableSelect) {
            reliableSelect = false;
            view().hideWarnView();
        } else {
            reliableSelect = true;
            view().showWarnView();
        }
        view().setReliableSelect(reliableSelect);
        checkCommitBtn();
    }

    public void onCommentClick() {
        if (commentSelect) {
            commentSelect = false;
            view().hideCommentView();
        } else {
            commentSelect = true;
            view().showCommentView();
        }
        view().setCommentSelect(commentSelect);
        checkCommitBtn();
    }

    public void checkCommitBtn() {
        String comment = view().getCommentStr().trim();
        if (commentSelect && reliableSelect && !StringUtil.isNullOrEmpty(comment)) {
            view().setCommitBtnEnabled(true);
        } else {
            view().setCommitBtnEnabled(false);
        }
    }

    public void onCommitClick() {
        String comment = view().getCommentStr().trim();
        model().allowHaikeRequest(inviteUser.user.uid, comment)
                .compose(this.<Object>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                        view().toast("批准已提交");
                        view().closeSelf();
                        EBInvite eb = new EBInvite(inviteUser.user.uid, EBInvite.ACTION_ALLOW_INVITE_REQUEST);
                        RxBus.getDefault().post(eb);
                        EventBus.getDefault().post(new EBContacts(EBContacts.TYPE_INVITATION_REQUEST_CHANGE, null));
                        EventBus.getDefault().post(new EBApproveUser(inviteUser.user.uid));
                    }
                });
    }

}
