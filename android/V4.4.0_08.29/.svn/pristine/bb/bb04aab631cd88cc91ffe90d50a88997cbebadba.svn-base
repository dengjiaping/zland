package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.TimelineData;
import com.zhisland.android.blog.feed.model.IFeedListModel;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/8/31.
 */
public class FeedListModel implements IFeedListModel {

    private FeedApi feedApi;

    public FeedListModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }


    @Override
    public Observable<TimelineData> getFeedList(final String nextId) {
        return Observable.create(new AppCall<TimelineData>() {
            @Override
            protected Response<TimelineData> doRemoteCall() throws Exception {
                Call<TimelineData> call = feedApi.getFeedList(nextId, 20);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<ZHPageData<Feed>> getMyPubFeedList(final String nextId, final int count) {
        return Observable.create(new AppCall<ZHPageData<Feed>>() {
            @Override
            protected Response<ZHPageData<Feed>> doRemoteCall() throws Exception {
                Call<ZHPageData<Feed>> call = feedApi.getMyPubFeedList(PrefUtil.Instance().getUserId(), nextId, count);
                return call.execute();
            }
        });
    }


}
