package com.zhisland.android.blog.message.view;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.lib.mvp.view.IMvpListView;

import java.util.List;

/**
 * 可能感兴趣的人view
 * Created by arthur on 2016/8/31.
 */
public interface IInteractionView extends IMvpListView<Message> {

    void updateInteractionMessage(List<Message> users);

    //跳转到用户的详情
    void gotoUser(User user);

    void gotoFeed(Feed feed);


}
