package com.zhisland.android.blog.profilemvp.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * 我的关注view
 * Created by Mr.Tui on 2016/9/6.
 */
public interface IMyAttentionView extends IMvpListView<InviteUser> {

    void pullDownToRefresh(boolean show);

}
