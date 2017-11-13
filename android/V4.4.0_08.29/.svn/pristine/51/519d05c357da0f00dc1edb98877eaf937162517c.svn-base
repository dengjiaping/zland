package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 创建新鲜事 model
 */
public class CreateFeedModel implements IMvpModel {

    private FeedApi feedApi;

    public CreateFeedModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }

    /**
     * 发布新鲜事
     */
    public Observable<Feed> createFeed(final String feedGson) {
        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.createsFeed(feedGson);
                return call.execute();
            }
        });
    }

}
