package com.zhisland.android.blog.freshtask.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.freshtask.bean.TaskCommentDetail;
import com.zhisland.android.blog.freshtask.model.impl.FriendCommentModel;
import com.zhisland.android.blog.freshtask.presenter.FriendCommentPresenter;
import com.zhisland.android.blog.freshtask.view.IFriendComment;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendCommentHolder;
import com.zhisland.android.blog.profile.controller.comment.ActUserCommentList;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友神评论
 */
public class FragFriendComment extends FragBaseMvp<FriendCommentPresenter>
        implements IFriendComment {

    private FriendCommentHolder holder = new FriendCommentHolder();

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragFriendComment.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "沉淀好友关系";
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_friend_comment, container, false);
        ButterKnife.inject(holder, view);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter().getFriendCommentData();
    }

    @Override
    public String getPageName() {
        return "RookieTaskComment";
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setFinishCount(Integer finishedCount) {
        if (finishedCount != null) {
            holder.tvFinishCount.setText(String.valueOf(finishedCount));
        }
    }

    @Override
    public void setTotalCount(Integer taskCount) {
        if (taskCount != null) {
            holder.tvTotalCount.setText(String.valueOf(taskCount));
        }
    }

    @Override
    public void setBtnText(String stateName) {
    }

    @Override
    public void setBtnEnable(boolean operable) {
        holder.tvInviteFriend.setEnabled(operable);
    }

    @Override
    public void showGoToCommentList() {
        holder.tvGoToCommentList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hiGoToCommentList() {
        holder.tvGoToCommentList.setVisibility(View.GONE);
    }

    //================UI event start================

    /**
     * 邀请好友为我写神评
     */
    @OnClick(R.id.tvInviteFriend)
    public void onClickInviteFriend() {
        TaskCommentDetail data = presenter().getData();
        if (data != null) {
            DialogUtil.getInstatnce().showInviteFriendComment(getActivity(), data);
        }
    }

    /**
     * 查看已得到的评论
     */
    @OnClick(R.id.tvGoToCommentList)
    public void onClickGoToCommentList() {
        ActUserCommentList.invoke(getActivity(), DBMgr.getMgr().getUserDao().getSelfUser());
    }

    @Override
    protected FriendCommentPresenter createPresenter() {
        FriendCommentPresenter presenter = new FriendCommentPresenter();
        FriendCommentModel model = new FriendCommentModel();
        presenter.setModel(model);
        return presenter;
    }
    //===============UI event end=============

}
