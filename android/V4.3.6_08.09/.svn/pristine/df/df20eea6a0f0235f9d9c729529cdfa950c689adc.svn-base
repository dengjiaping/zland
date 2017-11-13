package com.zhisland.android.blog.freshtask.presenter;

import android.content.Context;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.model.impl.CommentRecommendModel;
import com.zhisland.android.blog.freshtask.view.ICommentRecommendView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * 神评精选
 */
public class CommentRecommendPresenter extends BasePresenter<CommentRecommendModel, ICommentRecommendView> {

    private List<UserComment> allTasks;
    private int current = 0;

    /**
     * 设置数据
     */
    public void setData(List<UserComment> allTasks) {
        this.allTasks = allTasks;
        if (setupDone()) {
            view().setData(this.allTasks);
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (this.allTasks != null) {
            view().setData(this.allTasks);
            switchTo(current);
        }
    }

    /**
     * 更新view到第几步
     */
    public void switchTo(int index) {
        if (index >= allTasks.size()) {
            // 不应该出现这种情况
            return;
        }
        this.current = index;
        String nextTxt = (index == (allTasks.size() - 1)) ? "关闭" : "下一条";
        view().updateNext(nextTxt);
        view().updateHolder(index);
    }

    /**
     * 下一步点击
     */
    public void nextClicked() {
        if (current == allTasks.size() - 1) {
            view().finish();
        } else {
            switchTo(current + 1);
        }
    }

    /**
     * item 点击
     */
    public void onCardItemClicked(Context context, User user) {
        ActProfileDetail.invokeNoHistory(context, user.uid);
    }

}
