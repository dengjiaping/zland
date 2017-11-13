package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.ReportTypeModel;
import com.zhisland.android.blog.info.view.IReportType;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.MLog;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class ReportTypePresenter extends BasePresenter<ReportTypeModel, IReportType> {

    private ArrayList<ReportType> data;

    private ZHInfo info;

    public void setInfo(ZHInfo info) {
        this.info = info;
    }

    @Override
    protected void updateView() {
        super.updateView();
        data = getReportTypeLocal();
        view().refreshData(data);
        refreshReportType();
    }

    public void refreshReportType() {
        model().getReportType()
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ArrayList<ReportType>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ArrayList<ReportType>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("refreshReportType", "", e);
                    }

                    @Override
                    public void onNext(ArrayList<ReportType> data) {
                        model().cacheReportType(data);
                        ReportTypePresenter.this.data = data;
                        view().refreshData(data);
                    }
                });
    }

    public void onReportTypeClick(ReportType reportType) {
        view().gotoReportReason(info, reportType);
    }

    private ArrayList<ReportType> getReportTypeLocal() {
        return model().getReportTypeLocal();
    }

}
