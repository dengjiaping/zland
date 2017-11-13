package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.widget.TextView;

import com.zhisland.android.blog.R;

import butterknife.InjectView;

/**
 * 好友神评论
 */
public class FriendCommentHolder {

    @InjectView(R.id.tvFinishCount)
    public TextView tvFinishCount;

    @InjectView(R.id.tvTotalCount)
    public TextView tvTotalCount;

    @InjectView(R.id.tvInviteFriend)
    public TextView tvInviteFriend;

    @InjectView(R.id.tvGoToCommentList)
    public TextView tvGoToCommentList;

}
