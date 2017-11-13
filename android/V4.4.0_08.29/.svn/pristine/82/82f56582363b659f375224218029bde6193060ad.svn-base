package com.zhisland.android.blog.profilemvp.presenter;

import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.android.blog.profilemvp.model.IMyHotModel;
import com.zhisland.android.blog.profilemvp.view.IMyHotView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的热度Presenter
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyHotPresenter extends BasePresenter<IMyHotModel, IMyHotView> {

    UserHeatReport heatReport;

    User self;

    @Override
    protected void updateView() {
        super.updateView();
        Object obj = DBMgr.getMgr().getCacheDao().get(UserHeatReport.CACHE_KEY + PrefUtil.Instance().getUserId());
        if (obj != null && obj instanceof UserHeatReport) {
            heatReport = (UserHeatReport) obj;
        }
        self = DBMgr.getMgr().getUserDao().getSelfUser();
        fillView();
    }

    private void fillView() {
        String userTypeStr = "";
        if (self != null && self.userType != null) {
            if (self.userType == User.VALUE_TYPE_VIP) {
                userTypeStr = "岛邻";
            } else {
                userTypeStr = self.getUserTypeDesc();
            }
        }
        view().fillUserRelation(heatReport, userTypeStr);
    }

    private void getUserHeatReport() {
        model().getUserHeatReport(PrefUtil.Instance().getUserId())
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<UserHeatReport>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<UserHeatReport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserHeatReport userHeatReport) {
                        DBMgr.getMgr().getCacheDao().set(UserHeatReport.CACHE_KEY + PrefUtil.Instance().getUserId(), userHeatReport);
                        heatReport = userHeatReport;
                        fillView();
                    }
                });
    }
}
