package com.zhisland.android.blog.invitation.view;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * 以处理的列表
 * Created by arthur on 2016/8/9.
 */
public interface IInvitationDealedView extends IMvpListView<InviteUser> {

    /**
     * 更新标题文案
     *
     * @param titleString
     */
    void updateTitleString(String titleString);

    /**
     * 跳转到用户的详情页面
     *
     * @param user
     */
    void gotoUserDetail(User user);
}
