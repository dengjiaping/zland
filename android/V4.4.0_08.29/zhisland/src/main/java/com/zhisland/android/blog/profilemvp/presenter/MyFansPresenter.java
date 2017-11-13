package com.zhisland.android.blog.profilemvp.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.CommentSubject;
import com.zhisland.android.blog.common.comment.eb.EBComment;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.android.blog.profilemvp.model.IMyFansModel;
import com.zhisland.android.blog.profilemvp.view.IMyFansView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 我的粉丝Presenter
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyFansPresenter extends BasePresenter<IMyFansModel, IMyFansView> {

    //未显示的访客粉丝，在本地缓存
    private ZHPageData<InviteUser> visitorsList;
    //是否已经显示访客粉丝
    private boolean visitorsHasShow = false;

    Subscription relationSubscription;

    @Override
    public void bindView(@NonNull IMyFansView view) {
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

    public void loadFansUser(final String nextId) {
        if (nextId != null && visitorsList != null && visitorsList.data != null && visitorsList.pageIsLast) {
            view().onLoadSucessfully(visitorsList, false);
            visitorsList = null;
            visitorsHasShow = true;
            return;
        }
        model().getFansList(nextId)
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
                        if (nextId == null) {
                            //如果是下拉刷新，则重置visitorsList，visitorsShow
                            visitorsList = null;
                            visitorsHasShow = false;
                            if (data.data != null && data.data.size() > 0 && data.data.get(0).user.isYuZhuCe()) {
                                visitorsHasShow = true;
                            }
                        }
                        if (visitorsList != null && visitorsList.data != null) {
                            if (data.data == null) {
                                data.data = new ArrayList<InviteUser>();
                            }
                            data.data.addAll(0, visitorsList.data);
                            visitorsList = null;
                            visitorsHasShow = true;
                        }
                        if (!visitorsHasShow && hasVisitors(data.data)) {
                            //如果访客还没有被显示，且该次返回的数据有访客。则显示“查看访客粉丝”
                            visitorsList = new ZHPageData<InviteUser>();
                            visitorsList.pageIsLast = data.pageIsLast;
                            visitorsList.data = new ArrayList<InviteUser>();
                            visitorsList.nextId = data.nextId;
                            for (int i = data.data.size() - 1; i >= 0; i--) {
                                if (data.data.get(i).user.isYuZhuCe()) {
                                    InviteUser user = data.data.remove(i);
                                    visitorsList.data.add(0, user);
                                }
                            }
                            data.pageIsLast = true;
                            view().onLoadSucessfully(data, true);
                            return;
                        }
                        view().onLoadSucessfully(data, false);
                    }
                });
    }

    private boolean hasVisitors(ArrayList<InviteUser> data) {
        if (data != null && data.size() > 0) {
            int size = data.size();
            return data.get(size - 1).user.isYuZhuCe();
        }
        return false;
    }

    //用户被关注
    public void onFollowClicked(final InviteUser relationUser) {
        if (relationUser == null || relationUser.user == null) {
            return;
        }
        model().follow(relationUser.user, view().getPageName())
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().showToast("关注失败");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        view().showToast("已成功关注");
                        if (relationUser.state != null) {
                            relationUser.state.setIsOperable(CustomState.CAN_NOT_OPERABLE);
                            relationUser.state.setStateName("已关注");
                        }
                        view().refreshList();
                        EBRelation ebRelation = new EBRelation(EBRelation.TYPE_FOLLOW_TO, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }

    public void registerRelationRxBus() {
        relationSubscription = RxBus.getDefault().toObservable(EBRelation.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBRelation>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBRelation>() {
                    @Override
                    public void call(EBRelation eb) {
                        if (eb.getType() == EBRelation.TYPE_REMOVE_FANS || eb.getType() == EBRelation.TYPE_ADD_FANS) {
                            view().pullDownToRefresh(true);
                        }
                    }
                });
    }

}
