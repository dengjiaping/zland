package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.CommentSubject;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.comment.eb.EBComment;
import com.zhisland.android.blog.common.comment.eb.EBReply;
import com.zhisland.android.blog.common.comment.presenter.OnCommentListener;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.IFeedCommentDetailModel;
import com.zhisland.android.blog.feed.view.IFeedCommentDetail;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;

import rx.Subscriber;

/**
 * 评论详情，查看更多回复页面，Presenter
 */
public class FeedCommentDetailPresenter extends BasePresenter<IFeedCommentDetailModel, IFeedCommentDetail> implements OnCommentListener {

    public static final String TAG = "FeedCommentDetailPresenter";

    private Comment comment;
    private Feed feed;
    private ZHPageData<Reply> replyData;

    @Override
    protected void updateView() {
        super.updateView();
        if (comment != null) {
            view().fillComment(comment);
            getReplyData(null);
        }
    }

    public void initData(Feed feed, Comment comment) {
        this.comment = comment;
        this.feed = feed;
        if (setupDone()) {
            updateView();
        }
    }

    public void getReplyData(String nextId) {
        model().getReplyData(comment.commentId, nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<Reply>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<Reply>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.d(TAG, "getReplyData Error", e);
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<Reply> data) {
                        replyData = data;
                        view().onLoadSucessfully(data);
                    }
                });
    }

    @Override
    public void onReplyContentClick(Comment comment, Reply reply) {
        if (reply == null || reply.fromUser == null) {
            return;
        }
        if (reply.fromUser.uid == PrefUtil.Instance().getUserId()) {
            view().showDeleteDialog(reply);
        } else {
            view().showSendCommentView(SendCommentView.ToType.reply, reply.fromUser.name, feed.feedId, comment.commentId, reply.replyId);
        }
    }

    @Override
    public void wantSendComment(SendCommentView.ToType toType, String toName, Long commentId, Long replyId) {
        view().showSendCommentView(SendCommentView.ToType.comment, comment.publisher.name, feed.feedId, comment.commentId, null);
    }

    @Override
    public void onCommentLikeClick(final Comment comment) {
        if (comment.likeCustomIcon.clickState == 1) {
            likeCommentCancel();
        } else {
            likeComment();
        }
    }

    /**
     * 取消对评论的赞
     */
    private void likeCommentCancel() {
        model().commentLikeCancel(comment.commentId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Void data) {
                        comment.likeCustomIcon.clickState = 0;
                        comment.likeCustomIcon.quantity--;
                        view().fillComment(comment);
                        EBComment eb = new EBComment(CommentSubject.feed, EBComment.ACTION_UPDATA_COUNT, feed.feedId, comment);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 对评论点赞
     */
    private void likeComment() {
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
                    }

                    @Override
                    public void onNext(Void data) {
                        comment.likeCustomIcon.clickState = 1;
                        comment.likeCustomIcon.quantity++;
                        view().fillComment(comment);
                        EBComment eb = new EBComment(CommentSubject.feed, EBComment.ACTION_UPDATA_COUNT, feed.feedId, comment);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    @Override
    public void onDeleteCommentClick(Comment comment) {
        view().showDeleteDialog(null);
    }

    @Override
    public void lookMoreReply(Comment comment) {
        //评论详情不会触发该回调
    }

    /**
     * 删除回复
     */
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
                        if (comment != null && comment.replyList != null) {
                            comment.replyCount--;
                            if (comment.replyCount < 0) {
                                comment.replyCount = 0;
                            }
                            if(replyData != null && replyData.data != null){
                                replyData.data.remove(reply);
                            }
                        }
                        view().hideProgressDlg();
                        view().showToast("删除成功");
                        view().refreshReplyView(replyData.data);
                        EBReply eb = new EBReply(CommentSubject.feed, EBReply.ACTION_DELETE, comment.commentId, reply);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 删除评论
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
                        view().showToast("删除成功");
                        EBComment eb = new EBComment(CommentSubject.feed, EBComment.ACTION_DELETE, feed.feedId, comment);
                        RxBus.getDefault().post(eb);

                        if(feed.comment != null){
                            feed.comment.quantity--;
                            feed.action = EbAction.UPDATE;
                            RxBus.getDefault().post(feed);
                        }
                        view().finishSelf();
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

                        if (replyData == null || replyData.data == null || replyData.data.size() == 0) {
                            getReplyData(null);
                        } else {
                            replyData.data.add(0, data);
                            view().refreshReplyView(replyData.data);
                        }

                        comment.replyCount++;

                        EBReply eb = new EBReply(CommentSubject.feed, EBReply.ACTION_ADD, comment.commentId, data);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

}
