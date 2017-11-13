package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.IFeedModel;
import com.zhisland.android.blog.feed.model.remote.FeedApi;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/2.
 */
public class FeedModel implements IFeedModel {

    protected FeedApi feedApi;

    public FeedModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }

    @Override
    public Observable<Void> praiseFeed(final String feedId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.feedLike(feedId);
                return call.execute();
            }
        });
    }


    @Override
    public Observable<Void> cancelPraiseFeed(final String feedId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.feedCancelLike(feedId);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Void> reportFeed(final String feedId, final String reason) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.reportFeed(feedId, reason);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Feed> transmitFeed(final String feedId) {
        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.feedTransmit(feedId);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Feed> cancelTransmitFeed(final String feedId) {
        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.feedCancelTransmit(feedId);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Feed> getFeedDetail(final String feedId) {
        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.getFeedDetail(feedId);
                return call.execute();
            }
        });
    }
}
