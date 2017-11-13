package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.CountCollect;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.eb.EBComment;
import com.zhisland.android.blog.info.eb.EBInfo;
import com.zhisland.android.blog.info.eb.EBReply;
import com.zhisland.android.blog.info.model.impl.CommentDetailModel;
import com.zhisland.android.blog.info.view.ICommentDetail;
import com.zhisland.android.blog.info.view.impl.holder.SendCommentView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * 观点详情 业务逻辑的主导器
 * Created by Mr.Tui on 2016/6/29.
 */
public class CommentDetailPresenter extends BasePresenter<CommentDetailModel, ICommentDetail> implements IReply {

    private Comment comment;
    private ZHInfo info;

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setInfo(ZHInfo info) {
        this.info = info;
    }

    @Override
    protected void updateView() {
        super.updateView();
        view().refreshInfoView(info);
        refreshCommentView();
        getCommentDetail();
    }

    /**
     * 对资讯评论
     */
    public void comment(final long newsId, final String content) {
        view().showProgressDlg("观点发表中");
        model().comment(newsId, content)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Comment>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("comment", "", e);
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Comment data) {
                        view().hideProgressDlg();
                    }
                });
    }

    /**
     * 删除资讯评论
     */
    public void deleteComment() {
        view().showProgressDlg("观点删除中");
        model().deleteComment(comment.commentId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgressDlg();
                        MLog.e("deleteComment", "", e);
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().toastString("删除成功");
                        EBComment eb = new EBComment(EBComment.ACTION_DELETE, info.newsId, comment);
                        RxBus.getDefault().post(eb);
                        if (info.countCollect != null && info.countCollect.viewpointCount > 0) {
                            info.countCollect.viewpointCount--;
                            EBInfo ebInfo = new EBInfo(EBInfo.ACTION_UPDATA_COUNT, info);
                            RxBus.getDefault().post(ebInfo);
                        }
                        view().refreshReplyView(comment.replys);
                        CommentDetailPresenter.this.comment = null;
                        refreshCommentView();
                    }
                });
    }

    /**
     * 对资讯评论点赞
     */
    public void commentLike() {
        model().commentLike(comment.commentId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("commentLike", "", e);
                    }

                    @Override
                    public void onNext(Void data) {
                        comment.countCollect.likedState = CountCollect.LIKE_STATE_DONE;
                        comment.countCollect.likeCount++;
                        view().setLikeCount(comment.countCollect.likeCount);
                        view().setLikeEnabled(comment.countCollect.likedState == CountCollect.LIKE_STATE_NULL);
                        EBComment eb = new EBComment(EBComment.ACTION_UPDATA_COUNT, info.newsId, comment);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 对资讯评论或资讯评论的回复，进行回复
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    public void commentReply(final long viewpointId, final Long replyId, final String content) {
        view().showProgressDlg("观点发表中");
        model().commentReply(viewpointId, replyId, content)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Reply>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Reply>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("commentReply", "", e);
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Reply data) {
                        view().hideProgressDlg();
                        view().hideSendCommentView();
                        view().toastReplySuccess();
                        if (comment.replys == null) {
                            comment.replys = new ArrayList<>();
                        }
                        comment.replys.add(0, data);
                        if (comment.countCollect != null) {
                            comment.countCollect.replyCount++;
                        }
                        view().refreshReplyView(comment.replys);

                        EBReply eb = new EBReply(EBReply.ACTION_ADD, comment.commentId, data);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 删除回复
     * */
    public void deleteReply(final Reply reply) {
        view().showProgressDlg("回复删除中");
        model().deleteReply(reply.replyId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("deleteReply", "", e);
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        if (comment != null && comment.replys != null) {
                            if (comment.countCollect != null) {
                                comment.countCollect.replyCount--;
                            }
                            comment.replys.remove(reply);
                        }
                        view().hideProgressDlg();
                        view().toastString("删除成功");
                        view().refreshReplyView(comment.replys);
                        EBReply eb = new EBReply(EBReply.ACTION_DELETE, comment.commentId, reply);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 获取评论详情
     */
    public void getCommentDetail() {
        model().getCommentDetail(comment.commentId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Comment>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e("getCommentDetail", "", e);
                    }

                    @Override
                    public void onNext(Comment data) {
                        comment = data;
                        refreshCommentView();
                    }
                });
    }

    /**
     * 刷新评论view
     */
    private void refreshCommentView() {
        if (comment != null) {
            view().showCommentView();
            view().refreshCommentView(comment);
            view().setLikeCount(comment.countCollect.likeCount);
            view().setLikeEnabled(comment.countCollect.likedState == CountCollect.LIKE_STATE_NULL);
            if (comment.publisher.uid == PrefUtil.Instance().getUserId()) {
                view().showDeleteBtn();
            } else {
                view().hideDeleteBtn();
            }
            view().refreshReplyView(comment.replys);
            if (comment.replys == null || comment.replys.size() == 0) {
                view().hideLineView();
            } else {
                view().showLineView();
            }
        } else {
            view().refreshReplyView(null);
            view().hideCommentView();
            view().hideLineView();
        }
    }

    /**
     * 分享按钮被点击
     */
    public void onShareClicked() {
        if (info.infoShareUrl != null) {
            view().showShareView(info);
        }
    }

    public void onCommentClick() {
        view().showSendCommentView(SendCommentView.ToType.comment, comment.publisher.name, info.newsId, comment.commentId, null);
    }

    public void onCommentDeleteClick() {
        view().showDeleteDialog(null);
    }

    public void onInfoContentClick() {
        view().gotoInfoDetail(info.newsId);
    }

    public void onAvatarClick() {
        view().gotoProfileDetail(comment.publisher.uid);
    }

    public void onNameClick() {
        view().gotoProfileDetail(comment.publisher.uid);
    }

    public void onPositionClick() {
        view().gotoProfileDetail(comment.publisher.uid);
    }

    @Override
    public void onReplyContentClick(Comment comment, Reply reply) {
        if (reply == null || reply.publisher == null) {
            return;
        }
        if (reply.publisher.uid == PrefUtil.Instance().getUserId()) {
            view().showDeleteDialog(reply);
        } else {
            view().showSendCommentView(SendCommentView.ToType.reply, reply.publisher.name, info.newsId, comment.commentId, reply.replyId);
        }
    }
}
