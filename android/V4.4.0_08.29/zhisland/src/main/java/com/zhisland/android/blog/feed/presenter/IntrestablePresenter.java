package com.zhisland.android.blog.feed.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.feed.model.IIntrestableModel;
import com.zhisland.android.blog.feed.view.IIntrestableView;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * 可能感兴趣的人主导器
 * Created by arthur on 2016/8/31.
 */
public class IntrestablePresenter extends BasePresenter<IIntrestableModel, IIntrestableView> {

    private List<RecommendUser> users;


    public void setData(List<RecommendUser> users) {
        this.users = users;
        if (setupDone()) {
            updateView();
        }
    }

    @Override
    public void bindView(@NonNull IIntrestableView view) {
        super.bindView(view);
        RxBus.getDefault().toObservable(RecommendUser.class)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<RecommendUser>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<RecommendUser>() {
                    @Override
                    public void call(RecommendUser user) {
                        if (user.action == null || user.action != EbAction.DELETE)
                            return;

                        deleteRecommendUser(user);
                    }
                });
    }

    //更新feed
    private void deleteRecommendUser(RecommendUser user) {
        if (user == null || user.user == null || users == null)
            return;


        for (RecommendUser item : users) {
            if (item.user.uid == user.user.uid) {
                view().removeRecommend(item);
                break;
            }
        }

    }

    @Override
    protected void updateView() {
        super.updateView();
        if (users != null) {
            if (users.size() > 0) {
                view().showIntrestView();
            } else {
                view().hideIntrestView();
            }
            view().updateIntrestable(users);
        }
    }

    //用户信息被点击
    public void onUserClicked(User user) {
        view().gotoUserDetail(user);
    }

    //用户被忽略
    public void onIgnoreClicked(final RecommendUser inviteUser) {
        model().IgnoreRecommend(inviteUser.user)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().showToast("忽略失败");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        view().removeRecommend(inviteUser);
                        notifyRemove(inviteUser);
                    }
                });
    }

    //用户被关注
    public void onFollowClicked(final RecommendUser inviteUser) {
        model().follow(inviteUser.user, view().getPageName())
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
                        view().removeRecommend(inviteUser);
                        notifyRemove(inviteUser);
                        EBRelation ebRelation = new EBRelation(EBRelation.TYPE_FOLLOW_TO, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }

    public void onRemoveAll() {
        view().hideIntrestView();
    }

    //查看全部被点击
    public void onViewAllClicked() {
        view().gotoAllIntrest(users);
    }

    private void notifyRemove(RecommendUser user) {
        user.action = EbAction.DELETE;
        RxBus.getDefault().post(user);
    }

    public void loadNormal() {

        model().getIntrestPeople()
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<List<RecommendUser>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<List<RecommendUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(List<RecommendUser> recommendUsers) {
                        view().onLoadSucess(recommendUsers);
                    }
                });
    }
}
