package com.zhisland.android.blog.profilemvp.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.android.blog.profilemvp.model.IMyPhotoModel;
import com.zhisland.android.blog.profilemvp.model.remote.ProfileApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 个人主页  相册Model
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyPhotoModel implements IMyPhotoModel {

    private ProfileApi profileApi;

    public MyPhotoModel() {
        profileApi = RetrofitFactory.getInstance().getHttpsApi(ProfileApi.class);
    }

    /**
     * 分页获取个人相册
     */
    @Override
    public Observable<ZHPageData<UserPhoto>> getPhoto(final long userId, final String nextId) {
        return Observable.create(new AppCall<ZHPageData<UserPhoto>>() {
            @Override
            protected Response<ZHPageData<UserPhoto>> doRemoteCall() throws Exception {
                Call<ZHPageData<UserPhoto>> call = profileApi.getPhoto(userId, nextId, 18);
                return call.execute();
            }
        });
    }

    /**
     * 删除相册中的图片
     */
    @Override
    public Observable<Void> deletePhoto(final long photoId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = profileApi.deletePhoto(photoId);
                return call.execute();
            }
        });
    }

}
