package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.lib.mvp.view.IMvpListView;

import java.util.List;

/**
 * 评论详情页面
 */
public interface IFeedCommentDetail extends IMvpListView<Reply> {

    public void fillComment(Comment comment);

    /**
     * 显示删除对话框
     *
     * @param reply 要删除的回复。如果为空，则代表删除观点。
     */
    public void showDeleteDialog(final Reply reply);

    public void hideSendCommentView();

    public void showSendCommentView(SendCommentView.ToType toType, String toName, String feedId, Long commentId, Long replyId);

    public void toastReplySuccess();

    public void refreshReplyView(List<Reply> replies);

}
