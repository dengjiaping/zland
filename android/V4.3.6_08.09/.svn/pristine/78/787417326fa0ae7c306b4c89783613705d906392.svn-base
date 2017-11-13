package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.view.impl.holder.SendCommentView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * 资讯详情页面的阅读功能view接口
 */
public interface IInfoSocialView extends IMvpView {

    /**
     * 成功加载评论数据
     */
    void onloadSuccess(ZHPageData<Comment> data);

    /**
     * 加载评论数据失败
     *
     * @param error
     */
    void onLoadFailed(Throwable error);

    /**
     * 显示加载对话框
     */
    void showProgressDlg(String text);

    /**
     * 隐藏加载对话框
     */
    void hideProgressDlg();

    /**
     * toast一条string
     */
    void toastString(String str);

    /**
     * 刷新评论列表
     */
    void refreshCommentList();

    /**
     * 显示评论编辑框
     */
    void showSendCommentView(SendCommentView.ToType toType, String toName, Long newsId, Long commentId, Long replyId);

    /**
     * 隐藏评论编辑框
     */
    void hideSendCommentView();

    /**
     * toast观点发表成功
     */
    void toastCommentSuccess();

    /**
     * 设置评论数量View
     */
    void setCommentCount(int count);

    /**
     * 设置评论View显示
     */
    void showComment();

    /**
     * 设置顶、踩View显示状态
     */
    void showUpDownView(int upCount, int downCount);

    /**
     * 设置顶、踩View的Enabled状态
     */
    void setUpDownSelect(boolean upSelect, boolean downSelect);

    /**
     * 查看更多回复
     */
    void gotoCommentDetail(Comment comment, ZHInfo info);

    /**
     * 展示分享view
     *
     * @param info
     */
    void showShareView(ZHInfo info);

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

    /**
     * 显示删除对话框，reply不为空：删除回复；reply为空：删除评论
     */
    void showDeleteDialog(final Comment comment, final Reply reply);

    /**
     * 显示顶踩动画
     */
    void showUpDownAnim(int upCount, int downCount, boolean hide);

    /**
     * 显示下方发表评论的view
     * */
    void hideSendBottomView();

    /**
     * 隐藏下方发表评论的view
     * */
    void showSendBottomView();

}
