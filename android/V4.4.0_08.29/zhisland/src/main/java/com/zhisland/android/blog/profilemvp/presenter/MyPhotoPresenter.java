package com.zhisland.android.blog.profilemvp.presenter;

import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.android.blog.profilemvp.model.IMyPhotoModel;
import com.zhisland.android.blog.profilemvp.view.IMyPhotoView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * 个人主页  相册Presenter
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyPhotoPresenter extends BasePresenter<IMyPhotoModel, IMyPhotoView> {

    long userId;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void onAddPhotoClick() {
        view().goToAddPhoto();
    }

    public void getPhoto(String nextId) {
        model().getPhoto(userId, nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<UserPhoto>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<UserPhoto>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<UserPhoto> data) {
                        view().onLoadSucessfully(data);
                    }
                });
    }

    public void deletePhoto(long photoId) {
        model().deletePhoto(photoId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(Void data) {
                    }
                });
    }

}
