package com.zhisland.android.blog.profilemvp.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.android.blog.profilemvp.model.IMyHotModel;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyHotModel implements IMyHotModel {

    RelationApi api;

    public MyHotModel() {
        api = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class);
    }

    @Override
    public Observable<UserHeatReport> getUserHeatReport(final long uid) {
        return Observable.create(new AppCall<UserHeatReport>() {
            @Override
            protected Response<UserHeatReport> doRemoteCall() throws Exception {
                Call<UserHeatReport> call = api.getUserHeatReport(uid);
                return call.execute();
            }
        });
    }
}
