package com.zhisland.android.blog.freshtask.view;

import android.content.Context;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 新手任务 求邀请 升级
 */
public interface IInviteRequestView extends IMvpView {

    /**
     * 更新数据
     */
    void setData(ZHPageData<InviteUser> task);

    /**
     * 完成所有任务
     */
    void finish();

    /**
     * show content view
     */
    void showContentView();

    /**
     * show empty view
     */
    void showEmptyView();

    /**
     * 刷新UI
     */
    void notifyDataChange();

    /**
     * show short toast
     */
    void showShortToast(String toast);

    /**
     * 跳转召唤好友
     */
    void goToCallFriend();

    /**
     * 显示 loading progress
     */
    void showProgress();

    /**
     * 隐藏 loading progress
     */
    void hideProgress();

    /**
     * 跳转无权限
     */
    void goToNoPower();

    /**
     * 获取Context
     */
    Context getViewContext();

    /**
     * 加载数据
     */
    void loadDataSuccess(ZHPageData<InviteUser> datas);

    /**
     * load fail
     */
    void onLoadError(Throwable e);

    /**
     * 显示留言dialog
     */
    void showLeavingMsgDlg(InviteUser item);

    /**
     * 隐藏留言dialog
     */
    void hideLeavingDlg();
}
