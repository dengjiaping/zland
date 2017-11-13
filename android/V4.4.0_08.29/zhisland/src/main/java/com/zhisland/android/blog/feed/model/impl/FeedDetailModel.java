package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.feed.model.IFeedDetailModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * feed详情model
 */
public class FeedDetailModel extends FeedModel implements IFeedDetailModel {

    private static final String CACH_REPORT_REASON_LIST = "CACH_REPORT_FEED_REASON_LIST";

    /**
     * 删除feed
     */
    @Override
    public Observable<Void> deleteFeed(final String feedId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.deleteFeed(feedId);
                return call.execute();
            }
        });
    }

    /**
     * 举报feed
     */
    @Override
    public Observable<Void> reportFeed(final String feedId, final String reasonId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.reportFeed(feedId, reasonId);
                return call.execute();
            }
        });
    }

    @Override
    public ArrayList<Country> getReportReasonCache() {
        final ArrayList<Country> datas = (ArrayList<Country>) DBMgr.getMgr().getCacheDao().get(CACH_REPORT_REASON_LIST);
        return datas;
    }

    @Override
    public void saveReportReasonCache(ArrayList<Country> data) {
        if (data != null) {
            DBMgr.getMgr().getCacheDao().set(CACH_REPORT_REASON_LIST, data);
        }
    }

    @Override
    public Observable<ArrayList<Country>> getReportReasonInternet() {
        return Observable.create(new AppCall<ArrayList<Country>>() {
            @Override
            protected Response<ArrayList<Country>> doRemoteCall() throws Exception {
                Call<ArrayList<Country>> call = feedApi.getReportReason();
                return call.execute();
            }
        });
    }

}
