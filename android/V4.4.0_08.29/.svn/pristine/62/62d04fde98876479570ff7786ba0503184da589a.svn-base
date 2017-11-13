package com.zhisland.android.blog.freshtask.view;

import android.content.Context;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendCallItemHolder;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 建立好友圈 召唤好友
 */
public interface IFriendCallView extends IMvpView {

    /**
     * 更新数据
     */
    void setData(List<User> task);

    /**
     * 设置底部text view 文案
     */
    void setJumpText(String text);

    /**
     * 完成所有任务
     */
    void finish();

    /**
     * 刷新UI
     */
    void refreshHolder(FriendCallItemHolder holder);

    /**
     * 刷新UI
     */
    void notifyDataChange();

    Context getAppContext();
}
