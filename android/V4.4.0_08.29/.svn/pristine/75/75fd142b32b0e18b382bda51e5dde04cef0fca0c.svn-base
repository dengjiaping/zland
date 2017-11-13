package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.IntroductionModel;
import com.zhisland.android.blog.freshtask.view.IIntroductionView;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

public class IntroductionPresenter extends BasePresenter<IntroductionModel, IIntroductionView> {

    @Override
    protected void updateView() {
        super.updateView();
        view().initViews();
    }

    /**
     * 保存个人简介
     */
    public void saveIntroduction() {
        final String introduction = view().getContent();
        if (StringUtil.isNullOrEmpty(introduction)) {
            view().showToast("请输入您的个人简介");
        } else {
            view().showProgressDlg("正在提交。。。");

            model().updateUserIntroduction(introduction).
                    observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view().hideProgressDlg();
                        }

                        @Override
                        public void onNext(Void content) {
                            model().cachIntroduction(introduction);
                            view().hideProgressDlg();
                            EventBus.getDefault().post(new EBProfile(
                                    EBProfile.TYPE_CHANGE_INTRODUCTION));
                            view().showToast("提交成功。");
                            sendOkStatusEventBus();
                            view().finishSelf();
                        }
                    });
        }
    }

    private void sendOkStatusEventBus() {
        EventFreshTask EventFreshTask = new EventFreshTask(TaskType.INTRODUCTION, TaskStatus.FINISHED);
        BusFreshTask.Bus().post(EventFreshTask);
    }
}
