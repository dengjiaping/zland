package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * Created by arthur on 2016/9/14.
 */
public interface IMiniFeedListView extends IMvpListView<Feed> {


    //插入一条数据
    void insertItem(Feed feed);

    //更新一条数据
    void updateItem(Feed feed);

    //删除一条view
    void deleteItem(Feed feed);
}
