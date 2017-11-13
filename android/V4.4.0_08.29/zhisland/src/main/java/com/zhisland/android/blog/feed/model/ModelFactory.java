package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.feed.model.impl.FeedDetailCommentModel;
import com.zhisland.android.blog.feed.model.impl.FeedDetailModel;
import com.zhisland.android.blog.feed.model.impl.FeedListModel;
import com.zhisland.android.blog.feed.model.impl.FeedModel;
import com.zhisland.android.blog.feed.model.impl.IntrestableModel;
import com.zhisland.android.blog.feed.model.impl.RecommendModel;
import com.zhisland.android.blog.feed.model.impl.ShareFeedModel;

/**
 * Created by arthur on 2016/8/31.
 */
public class ModelFactory {

    public static IFeedListModel CreateFeedListModel() {
        return new FeedListModel();
    }

    public static IFeedModel CreateFeedModel() {
        return new FeedModel();
    }

    public static IRecommendModel createRecommendModel() {
        return new RecommendModel();
    }

    public static IIntrestableModel createIntrestableModel() {
        return new IntrestableModel();
    }

    public static IShareFeedModel createShareFeedModel() {
        return new ShareFeedModel();
    }

    public static IFeedDetailCommentModel CreateFeedDetailCommentModel() {
        return new FeedDetailCommentModel();
    }

    public static IFeedDetailModel CreateFeedDetailModel() {
        return new FeedDetailModel();
    }
}
