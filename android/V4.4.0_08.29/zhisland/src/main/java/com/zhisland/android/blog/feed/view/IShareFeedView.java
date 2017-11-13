package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * 分享feed的视图
 * Created by arthur on 2016/9/6.
 */
public interface IShareFeedView extends IMvpView {

    //更新新鲜事view
    void updateView(Feed feed);

    //获取输入框内容
    String getText();
}
