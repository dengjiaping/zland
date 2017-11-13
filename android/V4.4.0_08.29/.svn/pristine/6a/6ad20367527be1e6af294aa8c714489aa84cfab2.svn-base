package com.zhisland.android.blog.feed.presenter;

import android.support.annotation.NonNull;

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
import com.zhisland.android.blog.feed.model.IFeedDetailCommentModel;
import com.zhisland.android.blog.feed.view.IFeedDetailCommentView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * feed详情中的评论Presenter
 */
public class FeedDetailCommentPresenter extends BasePresenter<IFeedDetailCommentModel, IFeedDetailCommentView> implements OnCommentListener {

    public static final String TAG = "FeedDetailCommentPresenter";

    private Feed feed;

    private String feedId;

    Subscription feedSubscription;
    Subscription replySubscription;
    Subscription commentSubscription;

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
        if (feed != null) {
            this.feedId = feed.feedId;
        }
        if (setupDone()) {
            updateView();
        }
    }

    @Override
    public void bindView(@NonNull IFeedDetailCommentView view) {
        super.bindView(view);
        registerCommentRxBus();
        registerFeedRxBus();
        registerReplyRxBus();
    }

    @Override
    public void unbindView() {
        if (feedSubscription != null && (!feedSubscription.isUnsubscribed())) {
            feedSubscription.unsubscribe();
        }
        if (replySubscription != null && (!replySubscription.isUnsubscribed())) {
            replySubscription.unsubscribe();
        }
        if (commentSubscription != null && (!commentSubscription.isUnsubscribed())) {
            commentSubscription.unsubscribe();
        }
        super.unbindView();
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (feed != null)
            loadComment(null);
    }

    public void loadComment(String nextId) {
        model().getCommentList(feedId, nextId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<Comment>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<Comment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().onLoadFailed(e);
                    }

                    @Override
                    public void onNext(ZHPageData<Comment> data) {
                        view().onLoadSucessfully(data);
                        if (data != null && data.data != null && data.data.size() > 0) {
                            view().showCommentTag();
                        }
                    }
                });
    }

    @Override
    public void wantSendComment(SendCommentView.ToType toType, String toName, Long commentId, Long replyId) {
        view().showSendCommentView(toType, toName, feedId, commentId, replyId);
    }

    @Override
    public void onCommentLikeClick(final Comment comment) {
        if (comment.likeCustomIcon != null && comment.likeCustomIcon.clickState == 1) {
            likeCommentCancel(comment);
        } else {
            likeComment(comment);
        }
    }

    /**
     * 取消对评论的赞
     */
    private void likeCommentCancel(final Comment comment) {
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
                        if (comment.likeCustomIcon != null) {
                            comment.likeCustomIcon.clickState = 0;
                            comment.likeCustomIcon.quantity--;
                            if (comment.likeCustomIcon.quantity < 0) {
                                comment.likeCustomIcon.quantity = 0;
                            }
                        }
                        view().refreshCommentList();
                    }
                });
    }

    /**
     * 对评论点赞
     */
    private void likeComment(final Comment comment) {
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
                        if (comment.likeCustomIcon != null) {
                            comment.likeCustomIcon.clickState = 1;
                            comment.likeCustomIcon.quantity++;
                        }
                        view().refreshCommentList();
                    }
                });
    }

    @Override
    public void onDeleteCommentClick(Comment comment) {
        view().showDeleteDialog(comment, null);
    }

    @Override
    public void lookMoreReply(Comment comment) {
        view().gotoCommentDetail(feed, comment);
    }

    @Override
    public void onReplyContentClick(Comment comment, Reply reply) {
        if (reply == null || reply.fromUser == null) {
            return;
        }
        if (reply.fromUser.uid == PrefUtil.Instance().getUserId()) {
            view().showDeleteDialog(comment, reply);
        } else {
            view().showSendCommentView(SendCommentView.ToType.reply, reply.fromUser.name, feedId, comment.commentId, reply.replyId);
        }
    }

    /**
     * 删除资讯评论
     */
    public void deleteComment(final Comment comment) {
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
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().showToast("删除成功");
                        EBComment eb = new EBComment(CommentSubject.feed, EBComment.ACTION_DELETE, feedId, comment);
                        RxBus.getDefault().post(eb);
                        if(feed.comment != null){
                            feed.comment.quantity--;
                            feed.action = EbAction.UPDATE;
                            RxBus.getDefault().post(feed);
                        }
                    }
                });
    }

    /**
     * 对资讯评论
     */
    public void comment(final String feedId, final String content) {
        view().showProgressDlg("观点发表中");
        model().comment(feedId, content)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Comment>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Comment data) {
                        view().hideProgressDlg();
                        view().hideSendCommentView();
                        view().toastCommentSuccess();
                        EBComment eb = new EBComment(CommentSubject.feed, EBComment.ACTION_ADD, feedId, data);
                        RxBus.getDefault().post(eb);
                        if(feed.comment != null){
                            feed.comment.quantity++;
                            feed.action = EbAction.UPDATE;
                            RxBus.getDefault().post(feed);
                        }
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
                        view().hideProgressDlg();
                        MLog.e("asd", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Reply data) {
                        view().hideProgressDlg();
                        view().hideSendCommentView();
                        view().toastCommentSuccess();
                        EBReply eb = new EBReply(CommentSubject.feed, EBReply.ACTION_ADD, viewpointId, data);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    public void deleteReply(final Comment comment, final Reply reply) {
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
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideProgressDlg();
                        view().showToast("删除成功");
                        EBReply eb = new EBReply(CommentSubject.feed, EBReply.ACTION_DELETE, comment.commentId, reply);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    public void registerFeedRxBus() {
        feedSubscription = RxBus.getDefault().toObservable(Feed.class)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(Feed feed) {
                        if (feed.action != null && feed.action == EbAction.UPDATE && feed.feedId.equals(feedId)) {
                            FeedDetailCommentPresenter.this.feed = feed;
                        }
                    }
                });
    }

    public void registerReplyRxBus() {
        replySubscription = RxBus.getDefault().toObservable(EBReply.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBReply>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBReply>() {
                    @Override
                    public void call(EBReply eb) {
                        if (eb.subjectType != CommentSubject.feed) {
                            return;
                        }
                        Comment comment = view().getCommentItem(eb.commentId);
                        if (comment != null) {
                            if (eb.action == EBReply.ACTION_DELETE) {
                                if (comment.replyList != null) {
                                    for (Reply reply : comment.replyList) {
                                        if (reply.replyId == eb.reply.replyId) {
                                            comment.replyCount--;
                                            comment.replyList.remove(reply);
                                            view().refreshCommentList();
                                            return;
                                        }
                                    }
                                }
                            } else if (eb.action == EBReply.ACTION_ADD) {
                                if (comment.replyList == null) {
                                    comment.replyList = new ArrayList<>();
                                }
                                comment.replyList.add(0, eb.reply);
                                comment.replyCount++;
                                view().refreshCommentList();
                            }
                        }
                    }
                });
    }

    public void registerCommentRxBus() {
        commentSubscription = RxBus.getDefault().toObservable(EBComment.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBComment>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBComment>() {
                    @Override
                    public void call(EBComment eb) {
                        if (eb.subjectType == CommentSubject.feed && eb.subjectId != null && eb.subjectId.equals(String.valueOf(feedId)) && eb.comment != null) {
                            if (eb.action == EBComment.ACTION_ADD) {
                                view().addCommentItemAtFirst(eb.comment);
                            } else if (eb.action == EBComment.ACTION_DELETE) {
                                Comment comment = view().getCommentItem(eb.comment.commentId);
                                if (comment != null) {
                                    view().removeCommentItem(comment);
                                }
                            } else if (eb.action == EBComment.ACTION_UPDATA_COUNT) {
                                Comment comment = view().getCommentItem(eb.comment.commentId);
                                if (comment != null) {
                                    comment.likeCustomIcon = eb.comment.likeCustomIcon;
                                    comment.replyCount = eb.comment.replyCount;
                                    view().refreshCommentList();
                                }
                            }
                        }
                    }
                });
    }

}
