package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 推荐资讯model
 * Created by Mr.Tui on 2016/7/6.
 */
public class RecommendCommitModel implements IMvpModel {

    private NewsApi api;

    public RecommendCommitModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    /**
     * 推荐资讯
     * */
    public Observable<Void> recommendInfo(final RecommendInfo recommendInfo) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.recommendInfo(recommendInfo.url, recommendInfo.title, recommendInfo.desc);
                return call.execute();
            }
        });
    }

}