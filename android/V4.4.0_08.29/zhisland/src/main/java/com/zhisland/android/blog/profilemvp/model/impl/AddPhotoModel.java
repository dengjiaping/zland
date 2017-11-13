package com.zhisland.android.blog.profilemvp.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.android.blog.profilemvp.model.IAddPhotoModel;
import com.zhisland.android.blog.profilemvp.model.remote.ProfileApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Field;
import rx.Observable;

/**
 * 个人主页添加照片 model
 */
public class AddPhotoModel implements IAddPhotoModel {

    private ProfileApi profileApi;

    public AddPhotoModel() {
        profileApi = RetrofitFactory.getInstance().getHttpsApi(ProfileApi.class);
    }

    /**
     * 个人主页添加照片
     */
    @Override
    public Observable<Void> addPhoto(final String desc,final String data) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = profileApi.addPhoto(desc, data);
                return call.execute();
            }
        });
    }

}
