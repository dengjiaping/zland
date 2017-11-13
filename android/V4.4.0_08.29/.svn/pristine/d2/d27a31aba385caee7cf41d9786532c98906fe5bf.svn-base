package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 分享视图的Model
 * Created by arthur on 2016/9/6.
 */
public interface IShareFeedModel extends IMvpModel {

    //发布分享的feed
    Observable<Feed> shareEventFeed(long eventId,String reason);

    Observable<Feed> shareInfoFeed(long attachId, String content);
}
