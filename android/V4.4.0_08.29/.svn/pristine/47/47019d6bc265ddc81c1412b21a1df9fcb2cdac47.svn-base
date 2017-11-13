package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.aa.dto.Country;

import java.util.ArrayList;

import rx.Observable;

/**
 * feed详情model
 * Created by arthur on 2016/8/31.
 */
public interface IFeedDetailModel extends IFeedModel {

    public Observable<Void> deleteFeed(final String feedId);

    public Observable<Void> reportFeed(final String feedId,final String reasonId);

    public ArrayList<Country> getReportReasonCache();

    public void saveReportReasonCache(ArrayList<Country> data);

    public Observable<ArrayList<Country>> getReportReasonInternet();
}
