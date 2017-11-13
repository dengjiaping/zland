package com.zhisland.android.blog.invitation.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * Created by arthur on 2016/8/10.
 */
public interface IInvitatoinConfirmView extends IMvpListView<InviteUser> {

    /**
     * 导航到可认证搜索页
     */
    void gotoConfirmSearch();

    /**
     * 导航到批准海客页
     *
     * @param user
     */
    void gotoAllowHaike(InviteUser user);

    /**
     * 开始启用List的空数据view
     */
    void showListEmptyView();

    /**
     * 显示通讯录拦截页
     */
    void showContactDenyView();

    /**
     * 隐藏通讯录权限页
     */
    void hideContactDenyView();

    void hideEmptyView();

    void disablePullUp();

    void enablePullUp();

    void showListHeaderView();
}
