package com.zhisland.android.blog.profilemvp.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.android.blog.profilemvp.model.IMyAttentionModel;
import com.zhisland.android.blog.profilemvp.view.IMyAttentionView;
import com.zhisland.android.blog.profilemvp.view.IMyFansView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 我关注的人Presenter
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyAttentionPresenter extends BasePresenter<IMyAttentionModel, IMyAttentionView> {

    Subscription relationSubscription;

    @Override
    public void bindView(@NonNull IMyAttentionView view) {
        super.bindView(view);
        registerRelationRxBus();
    }

    @Override
    public void unbindView() {
        if (relationSubscription != null && (!relationSubscription.isUnsubscribed())) {
            relationSubscription.unsubscribe();
        }
        super.unbindView();
    }

    public void loadAttentionUser(String nextId) {
        model().getAttentionList(nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<InviteUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<InviteUser>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<InviteUser> data) {
                        view().onLoadSucessfully(data);
                    }
                });
    }

    public void registerRelationRxBus() {
        relationSubscription = RxBus.getDefault().toObservable(EBRelation.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBRelation>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBRelation>() {
                    @Override
                    public void call(EBRelation eb) {
                        if (eb.getType() == EBRelation.TYPE_FOLLOW_TO || eb.getType() == EBRelation.TYPE_FOLLOW_CANCEL) {
                            view().pullDownToRefresh(true);
                        }
                    }
                });
    }

}
