package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 对于Feed的
 * Created by arthur on 2016/8/31.
 */
public interface IFeedModel extends IMvpModel {

    //赞
    Observable<Void> praiseFeed(String feedId);

    //取消赞
    Observable<Void> cancelPraiseFeed(String feedId);

    //举报某一条feed
    Observable<Void> reportFeed(String feedId, String reason);

    //转播某一条feed
    Observable<Feed> transmitFeed(String feedId);

    //取消转播某一条feed
    Observable<Feed> cancelTransmitFeed(String feedId);

    //跳转到feed详情
    Observable<Feed> getFeedDetail(String feedId);
}
