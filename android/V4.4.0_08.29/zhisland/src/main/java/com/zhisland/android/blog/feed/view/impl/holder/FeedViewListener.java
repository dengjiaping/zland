package com.zhisland.android.blog.feed.view.impl.holder;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.Feed;

/**
 * Created by arthur on 2016/9/1.
 */
public interface FeedViewListener {

    //用户信息被点击
    void onUserClicked(FeedHolder feedHolder, User user);

    //赞被点击
    void onPraiseClicked(FeedHolder feedHolder, Feed curFeed);

    //评论被点击
    void onCommentClicked(FeedHolder feedHolder, Feed curFeed);

    //转播被点击
    void onTransemitClicked(FeedHolder feedHolder, Feed curFeed);

    //附件被点击
    void onAttachClicked(Feed feed, Object arg);

    //feed被点击
    void onFeedCicked(Feed curFeed);
}
