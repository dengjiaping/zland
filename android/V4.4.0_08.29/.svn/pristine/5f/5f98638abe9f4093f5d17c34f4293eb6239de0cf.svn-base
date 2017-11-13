package com.zhisland.android.blog.common.comment.presenter;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.view.SendCommentView;

/**
 *
 * Created by Mr.Tui on 2016/8/1.
 */
public interface OnCommentListener extends OnReplyListener {

    /**
     * 请求发表评论（观点）或回复
     */
    public void wantSendComment(SendCommentView.ToType toType, String toName, Long commentId, Long replyId);

    /**
     * 点击事件，对评论点赞
     * */
    public void onCommentLikeClick(final Comment comment);

    /**
     * 点击事件，删除评论
     * */
    public void onDeleteCommentClick(Comment comment);

    /**
     * 点击事件，查看更多回复
     * */
    public void lookMoreReply(Comment comment);

}
