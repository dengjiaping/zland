package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.TimelineData;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * Created by arthur on 2016/8/31.
 */
public interface IFeedListModel extends IMvpModel {

    //获取新鲜事列表
    Observable<TimelineData> getFeedList(String nextId);

    //获取我发布的新鲜事列表
    Observable<ZHPageData<Feed>> getMyPubFeedList( final String nextId, final int count);
}
