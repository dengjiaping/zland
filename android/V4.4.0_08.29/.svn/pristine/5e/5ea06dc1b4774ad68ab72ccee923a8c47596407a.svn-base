package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface ICommentDetail extends IMvpView {

    /**
     * 刷新资讯相关view
     */
    public void refreshInfoView(ZHInfo info);

    /**
     * 显示评论view
     */
    public void showCommentView();

    /**
     * 隐藏评论view
     */
    public void hideCommentView();

    /**
     * 显示评论回复上方的三角。
     */
    public void showLineView();

    /**
     * 隐藏评论回复上方的三角。
     */
    public void hideLineView();

    /**
     * 刷新评论相关view
     */
    public void refreshCommentView(Comment comment);

    /**
     * 设置评论赞数量
     */
    public void setLikeCount(int count);

    /**
     * 设置评论赞按钮是否可用
     */
    public void setLikeEnabled(boolean enabled);

    /**
     * 显示评论删除按钮
     */
    public void showDeleteBtn();

    /**
     * 隐藏评论删除按钮
     */
    public void hideDeleteBtn();

    /**
     * 刷新评论回复列表
     */
    public void refreshReplyView(List<Reply> replies);

    /**
     * 显示加载对话框
     */
    public void showCommentDlg(String text);

    /**
     * 隐藏加载对话框
     */
    public void hideCommentDlg();

    /**
     * toast回复成功对话框
     */
    public void toastReplySuccess();

    /**
     * 显示发表回复view
     */
    public void showSendCommentView(SendCommentView.ToType toType, String toName, Long newsId, Long commentId, Long replyId);

    /**
     * 隐藏发表回复view
     */
    public void hideSendCommentView();

    /**
     * 显示分享view
     */
    public void showShareView(ZHInfo info);

    /**
     * 显示删除对话框
     *
     * @param reply 要删除的回复。如果为空，则代表删除观点。
     */
    public void showDeleteDialog(final Reply reply);

    /**
     * 跳转到个人详情页
     */
    public void gotoProfileDetail(long uid);

    /**
     * 跳转到资讯详情页
     */
    public void gotoInfoDetail(long newsId);

}
