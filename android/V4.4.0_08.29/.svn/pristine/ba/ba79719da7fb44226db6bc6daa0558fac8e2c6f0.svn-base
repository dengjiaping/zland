package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * feed详情页面
 */
public interface IFeedDetailCommentView extends IMvpListView<Comment> {


    void gotoCommentDetail(Feed feed, Comment comment);

    /**
     * 显示评论编辑框
     */
    void showSendCommentView(SendCommentView.ToType toType, String toName, String feedId, Long commentId, Long replyId);

    /**
     * 显示删除对话框，reply不为空：删除回复；reply为空：删除评论
     */
    void showDeleteDialog(final Comment comment, final Reply reply);

    /**
     * 刷新评论列表
     */
    void refreshCommentList();

    /**
     * 隐藏评论编辑框
     */
    void hideSendCommentView();

    /**
     * toast观点发表成功
     */
    void toastCommentSuccess();

    //显示评论数目view
    void showCommentTag();

    /**
     * 获取评论列表中id为commentId的comment
     */
    Comment getCommentItem(long commentId);

    /**
     * 在评论列表的最前位置，添加comment。
     */
    void addCommentItemAtFirst(Comment comment);

    /**
     * 从评论列表中移除comment。
     */
    void removeCommentItem(Comment comment);
}
