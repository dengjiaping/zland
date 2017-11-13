package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.IShareFeedModel;
import com.zhisland.android.blog.feed.model.remote.FeedApi;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/7.
 */
public class ShareFeedModel implements IShareFeedModel {

    private FeedApi feedApi;

    public ShareFeedModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }


    @Override
    public Observable<Feed> shareEventFeed(final long eventId, final String reason) {

        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.shareEvent(eventId, reason);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Feed> shareInfoFeed(final long attachId, final String content) {
        return Observable.create(new AppCall<Feed>() {
            @Override
            protected Response<Feed> doRemoteCall() throws Exception {
                Call<Feed> call = feedApi.shareInfo(attachId, content);
                return call.execute();
            }
        });
    }
}
