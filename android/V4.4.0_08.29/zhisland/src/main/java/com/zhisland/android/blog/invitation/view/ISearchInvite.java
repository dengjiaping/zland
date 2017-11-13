package com.zhisland.android.blog.invitation.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by Mr.Tui on 2016/8/9.
 */
public interface ISearchInvite extends IMvpView {

    void onSearchFailed(Throwable error);

    void onSearchSuccess(ZHPageData<InviteUser> data);

    void setEmptyView();

    void goToApproveHaiKe(InviteUser inviteUser);

    void showKeyboard();
}
