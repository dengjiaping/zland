package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.mvp.model.IMvpModel;
import com.zhisland.lib.util.StringUtil;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 添加链接（推荐资讯）Model
 * Created by Mr.Tui on 2016/7/6.
 */
public class AddLinkModel implements IMvpModel {

    private NewsApi api;

    public AddLinkModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    public Observable<RecommendInfo> checkUrl(final String url) {
        AppCall appCall = new AppCall<RecommendInfo>() {
            @Override
            protected Response<RecommendInfo> doRemoteCall() throws Exception {
                Call<RecommendInfo> call = api.checkUrl(url);
                return call.execute();
            }

            @Override
            protected void handlerError(int code, String message) {
                //屏蔽掉super.handlerError，不弹toast
            }
        };
        appCall.setIsBackgroundTask();
        return Observable.create(appCall);
    }

}