package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.ReportCommitModel;
import com.zhisland.android.blog.info.view.IReportCommit;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class ReportCommitPresenter extends BasePresenter<ReportCommitModel, IReportCommit> {

    private ZHInfo info;

    private ReportType reportType;

    public void setInfo(ZHInfo info) {
        this.info = info;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    @Override
    protected void updateView() {
        super.updateView();
    }

    private void reportInfo(final String reasonDesc) {
        model().reportInfo(info.newsId, reportType.code, reasonDesc)
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
                        view().gotoReportOk(info);
                    }
                });
    }

    public void onCommitClick() {
        view().showProgressDlg();
        String reasonDesc = view().getReason();
        reportInfo(reasonDesc);
    }
}
