package com.zhisland.android.blog.info.presenter;

import android.webkit.URLUtil;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.impl.AddLinkModel;
import com.zhisland.android.blog.info.view.IAddLink;
import com.zhisland.android.blog.info.view.impl.FragLinkEdit;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.util.StringUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class AddLinkPresenter extends BasePresenter<AddLinkModel, IAddLink> {

    private Subscription hideDelayOb;

    @Override
    protected void updateView() {
        super.updateView();
    }

    @Override
    public void unbindView() {
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
        super.unbindView();
    }

    private void refreshReportType(final String url) {
        model().checkUrl(url)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<RecommendInfo>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<RecommendInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        String promptStr = "url检查失败";
                        if (e != null && e instanceof ApiError) {
                            promptStr = ((ApiError) e).message;
                        }
                        view().showErrorPrompt(promptStr);
                        hidePromptDelay(2000);
                    }

                    @Override
                    public void onNext(RecommendInfo data) {
                        data.url = url;
                        view().hideLoading();
                        view().gotoRecommendReason(data);
                    }
                });
    }

    private void hidePromptDelay(long delay) {
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
        hideDelayOb = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().hidePrompt();
                        hideDelayOb = null;
                    }
                });
    }

    public void onViewResume() {
        String clipText = view().getClipText();
        if (StringUtil.isNullOrEmpty(clipText) || !URLUtil.isNetworkUrl(clipText.trim())) {
            return;
        }
        view().setEditLinkText(clipText);
        PrefUtil.Instance().setKeyValue(FragLinkEdit.KEY_LAST_CLIP, clipText);
    }

    public void onClearClick() {
        view().setEditLinkText("");
    }

    public void onCommitClick() {
        if (view().isLoadingShowing()) {
            return;
        }
        view().showLoading();
        String url = view().getLinkEditText();
        refreshReportType(url);
    }

    public void checkCommitContent() {
        String content = view().getLinkEditText().trim();
        if (URLUtil.isNetworkUrl(content)) {
            view().setCommitBtnEnabled(true);
        } else {
            view().setCommitBtnEnabled(false);
        }
    }

}
