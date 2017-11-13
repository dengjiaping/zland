package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.feed.model.IIntrestableModel;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;

import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/6.
 */
public class IntrestableModel implements IIntrestableModel {

    private RelationApi relationApi;

    public IntrestableModel() {
        relationApi = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class);
    }


    @Override
    public Observable<Void> IgnoreRecommend(final User user) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = relationApi.ignoreRecommend(user.uid);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Void> follow(final User user, final String source) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = relationApi.follow(user.uid, source);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Void> unfollow(final User user) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = relationApi.unFollow(user.uid);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<List<RecommendUser>> getIntrestPeople() {
        return Observable.create(new AppCall<List<RecommendUser>>() {
            @Override
            protected Response<List<RecommendUser>> doRemoteCall() throws Exception {
                Call<List<RecommendUser>> call = relationApi.getIntrestPeople();
                return call.execute();
            }
        });
    }
}
