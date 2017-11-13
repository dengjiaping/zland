package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.impl.RecommendCommitModel;
import com.zhisland.android.blog.info.view.IRecommendCommit;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class RecommendCommitPresenter extends BasePresenter<RecommendCommitModel, IRecommendCommit> {

    private RecommendInfo recommendInfo;

    public void setRecommendInfo(RecommendInfo recommendInfo) {
        this.recommendInfo = recommendInfo;
    }

    @Override
    protected void updateView() {
        super.updateView();
        view().setInfoTitle(recommendInfo.title);
    }

    private void recommendInfo() {
        model().recommendInfo(recommendInfo)
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

                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().gotoInfoPreview(recommendInfo.url);
                    }
                });
    }

    public void onCommitClick() {
        view().showProgressDlg();
        recommendInfo.desc = view().getReason();
        recommendInfo();
    }
}
