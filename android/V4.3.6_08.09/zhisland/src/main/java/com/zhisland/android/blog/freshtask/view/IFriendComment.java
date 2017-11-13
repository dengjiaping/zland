package com.zhisland.android.blog.freshtask.view;


import com.zhisland.lib.mvp.view.IMvpView;

/**
 * 好友神评论
 */
public interface IFriendComment extends IMvpView {

    void finish();

    void showProgress();

    void hideProgress();

    void setFinishCount(Integer finishedCount);

    void setTotalCount(Integer taskCount);

    void setBtnText(String stateName);

    void setBtnEnable(boolean operable);

    void showGoToCommentList();

    void hiGoToCommentList();
}
