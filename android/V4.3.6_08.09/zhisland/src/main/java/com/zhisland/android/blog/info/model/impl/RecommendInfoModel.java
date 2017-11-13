package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.RInfoPageData;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 推荐资讯列表model
 * Created by Mr.Tui on 2016/6/29.
 */
public class RecommendInfoModel implements IMvpModel {

    private NewsApi api;

    public RecommendInfoModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    /**
     * 获取资讯推荐列表
     */
    public Observable<RInfoPageData<ZHInfo>> getRecommendList(final String type, final String nextId) {
        return Observable.create(new AppCall<RInfoPageData<ZHInfo>>() {
            @Override
            protected Response<RInfoPageData<ZHInfo>> doRemoteCall() throws Exception {
                Call<RInfoPageData<ZHInfo>> call = api.getRecommendInfo(type, 20, nextId);
                return call.execute();
            }
        });
    }
}
