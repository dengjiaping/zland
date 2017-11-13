package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.presenter.FeedImageAdapter;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by arthur on 2016/8/31.
 */
public interface IFeedView extends IMvpView {

    //跳转到用户详情页
    void gotoUserDetail(User user);

    // 更新某一个feed的界面
    void updateFeed(Feed feed);

    /**
     * 打开feed详情
     *
     * @param curFeed
     * @param startComment 是否开始评论
     */
    void gotoFeedDetail(Feed curFeed, boolean startComment);

    /**
     * 浏览图片
     *
     * @param feedImageAdapter
     * @param index            从第几张开始浏览
     */
    void browseImages(FeedImageAdapter feedImageAdapter, int index);
}
