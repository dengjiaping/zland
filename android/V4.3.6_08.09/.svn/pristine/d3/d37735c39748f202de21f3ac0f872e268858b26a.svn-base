package com.zhisland.android.blog.freshtask.view;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 建立好友圈 加好友
 */
public interface IFriendAddView extends IMvpView {

    /**
     * 更新数据
     */
    void setData(List<User> task);

    /**
     * 设置描述
     */
    void setDesc(String desc);

    /**
     * 设置按钮文案
     */
    void setBtnText(String text);

    /**
     * 设置底部text view 文案
     */
    void setJumpText(String text);

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
}
