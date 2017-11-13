package com.zhisland.android.blog.info.presenter;

import android.support.annotation.NonNull;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.comment.bean.CommentSubject;
import com.zhisland.android.blog.common.comment.presenter.OnCommentListener;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.CountCollect;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.common.comment.eb.EBComment;
import com.zhisland.android.blog.info.eb.EBInfo;
import com.zhisland.android.blog.common.comment.eb.EBReply;
import com.zhisland.android.blog.info.model.impl.InfoDetailModel;
import com.zhisland.android.blog.info.view.IInfoSocialView;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 资讯详情社交业务逻辑的主导器
 * Created by Mr.Tui on 2016/6/29.
 */
public class InfoSocialPresenter extends BasePresenter<InfoDetailModel, IInfoSocialView> implements OnCommentListener {

    private long newsId;
    private ZHInfo info;
    Subscription infoSubscription;
    Subscription replySubscription;
    Subscription commentSubscription;

    private boolean sendViewShowing = false;

    public void setInfo(ZHInfo info) {

        this.info = info;
        if (setupDone()) {
            updateView();
        }
    }

    @Override
    public void bindView(@NonNull IInfoSocialView view) {
        super.bindView(view);
        registerCommentRxBus();
        registerInfoRxBus();
        registerReplyRxBus();
    }

    @Override
    protected void updateView() {
        super.updateView();
        //1. 更新顶踩,2.更新评论数据
        if (info != null && info.countCollect != null) {
            refreshUpDownView(info.countCollect);
            view().setCommentCount(info.countCollect.viewpointCount);
        }
    }

    @Override
    public void unbindView() {
        if (infoSubscription != null && (!infoSubscription.isUnsubscribed())) {
            infoSubscription.unsubscribe();
        }
        if (replySubscription != null && (!replySubscription.isUnsubscribed())) {
            replySubscription.unsubscribe();
        }
        if (commentSubscription != null && (!commentSubscription.isUnsubscribed())) {
            commentSubscription.unsubscribe();
        }
        super.unbindView();
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public void getCommentListFromInternet(final String nextId) {
        model().getCommentList(newsId, nextId)
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
                        view().onloadSuccess(data);
                    }
                });
    }

    /**
     * 资讯 顶操作
     */
    public void infoUp() {
        if (info.countCollect.userUpDownState == CountCollect.UP_DOWN_STATE_NULL) {
            model().infoUp(newsId)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<CountCollect>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<CountCollect>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(CountCollect data) {
                            ZHInfo info = new ZHInfo();
                            info.newsId = newsId;
                            info.countCollect = data;
                            EBInfo ebInfo = new EBInfo(EBInfo.ACTION_UPDATA_COUNT, info);
                            RxBus.getDefault().post(ebInfo);
                            view().showUpDownAnim(info.countCollect.upCount, info.countCollect.downCount, false);
                        }
                    });
        } else {
            view().showUpDownAnim(info.countCollect.upCount, info.countCollect.downCount, false);
        }
    }

    /**
     * 资讯 踩操作
     */
    public void infoDown() {
        if (info.countCollect.userUpDownState == CountCollect.UP_DOWN_STATE_NULL) {
            model().infoDown(newsId)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<CountCollect>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<CountCollect>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(CountCollect data) {
                            ZHInfo info = new ZHInfo();
                            info.newsId = newsId;
                            info.countCollect = data;
                            EBInfo ebInfo = new EBInfo(EBInfo.ACTION_UPDATA_COUNT, info);
                            RxBus.getDefault().post(ebInfo);
                            view().showUpDownAnim(info.countCollect.upCount, info.countCollect.downCount, false);
                        }
                    });
        } else {
            view().showUpDownAnim(info.countCollect.upCount, info.countCollect.downCount, false);
        }
    }

    /**
     * 对资讯评论
     */
    public void comment(final String newsId, final String content) {
        view().showCommentDlg("观点发表中");
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
                        view().hideCommentDlg();
                    }

                    @Override
                    public void onNext(Comment data) {
                        view().hideCommentDlg();
                        view().hideSendCommentView();
                        view().toastCommentSuccess();
                        EBComment eb = new EBComment(CommentSubject.info,EBComment.ACTION_ADD, newsId, data);
                        RxBus.getDefault().post(eb);

                        info.countCollect.viewpointCount += 1;
                        EBInfo ebInfo = new EBInfo(EBInfo.ACTION_UPDATA_COUNT, info);
                        RxBus.getDefault().post(ebInfo);
                    }
                });
    }

    /**
     * 删除资讯评论
     */
    public void deleteComment(final Comment comment) {
        view().showCommentDlg("观点删除中");
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
                        view().hideCommentDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideCommentDlg();
                        view().showToast("删除成功");
                        EBComment eb = new EBComment(CommentSubject.info, EBComment.ACTION_DELETE, String.valueOf(newsId), comment);
                        RxBus.getDefault().post(eb);

                        info.countCollect.viewpointCount -= 1;
                        EBInfo ebInfo = new EBInfo(EBInfo.ACTION_UPDATA_COUNT, info);
                        RxBus.getDefault().post(ebInfo);
                    }
                });
    }

    /**
     * 对资讯评论点赞
     */
    public void onCommentLikeClick(final Comment comment) {
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
                        comment.likeCustomIcon.operable = 0;
                        comment.likeCustomIcon.clickState = 1;
                        comment.likeCustomIcon.quantity++;
                        view().refreshCommentList();
                    }
                });
    }

    /**
     * 对资讯评论或资讯评论的回复，进行回复
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    public void commentReply(final long viewpointId, final Long replyId, final String content) {
        view().showCommentDlg("观点发表中");
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
                        view().hideCommentDlg();
                    }

                    @Override
                    public void onNext(Reply data) {
                        view().hideCommentDlg();
                        view().hideSendCommentView();
                        view().toastCommentSuccess();
                        EBReply eb = new EBReply(CommentSubject.info,EBReply.ACTION_ADD, viewpointId, data);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    public void deleteReply(final Comment comment, final Reply reply) {
        view().showCommentDlg("回复删除中");
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
                        view().hideCommentDlg();
                    }

                    @Override
                    public void onNext(Void data) {
                        view().hideCommentDlg();
                        view().showToast("删除成功");
                        EBReply eb = new EBReply(CommentSubject.info,EBReply.ACTION_DELETE, comment.commentId, reply);
                        RxBus.getDefault().post(eb);
                    }
                });
    }

    /**
     * 准备发表观点或回复
     */
    public void wantSendComment(SendCommentView.ToType toType, String toName, Long commentId, Long replyId) {
        view().showSendCommentView(toType, toName, newsId, commentId, replyId);
    }

    /**
     * 查看更多回复
     */
    public void lookMoreReply(Comment comment) {
        view().gotoCommentDetail(comment, info);
    }


    /**
     * 刷新顶踩view
     */
    public void refreshUpDownView(CountCollect countCollect) {

        view().showUpDownView(countCollect.upCount, countCollect.downCount);
        if (countCollect.userUpDownState == CountCollect.UP_DOWN_STATE_NULL) {
            view().setUpDownSelect(false, false);
        } else {
            view().setUpDownSelect(false, false);
            if (countCollect.userUpDownState == CountCollect.UP_DOWN_STATE_UP) {
                view().setUpDownSelect(true, false);
            } else {
                view().setUpDownSelect(false, true);
            }
        }
    }

    /**
     * 开始处理social的视图逻辑
     */
    public void onReadConentloaded() {
        view().showComment();
        getCommentListFromInternet(null);
    }

    /**
     * 分享按钮被点击
     */
    public void onShareClicked() {
        if (info.infoShareUrl != null) {
            view().showShareView(info);
        }
    }

    public void registerInfoRxBus() {
        infoSubscription = RxBus.getDefault().toObservable(EBInfo.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBInfo>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBInfo>() {
                    @Override
                    public void call(EBInfo eb) {
                        if (eb.action == EBInfo.ACTION_UPDATA_COUNT && eb.info != null
                                && eb.info.countCollect != null && eb.info.newsId == info.newsId) {
                            info.countCollect = eb.info.countCollect;
                            refreshUpDownView(eb.info.countCollect);
                            view().setCommentCount(eb.info.countCollect.viewpointCount);
                        }
                    }
                });
    }

    public void registerReplyRxBus() {
        replySubscription = RxBus.getDefault().toObservable(EBReply.class).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<EBReply>bindUntilEvent(PresenterEvent.UNBIND_VIEW)).subscribe(new Action1<EBReply>() {
                    @Override
                    public void call(EBReply eb) {
                        if(eb.subjectType != CommentSubject.info){
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
                        if (eb.subjectType == CommentSubject.info && eb.subjectId != null && eb.subjectId.equals(String.valueOf(newsId)) && eb.comment != null) {
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

    @Override
    public void onReplyContentClick(Comment comment, Reply reply) {
        if (reply == null || reply.fromUser == null) {
            return;
        }
        if (reply.fromUser.uid == PrefUtil.Instance().getUserId()) {
            view().showDeleteDialog(comment, reply);
        } else {
            view().showSendCommentView(SendCommentView.ToType.reply, reply.fromUser.name, info.newsId, comment.commentId, reply.replyId);
        }
    }

    public void onDeleteCommentClick(Comment comment) {
        view().showDeleteDialog(comment, null);
    }

    public void onScrolledComment(boolean scrolledComment) {
        if (sendViewShowing && !scrolledComment) {
            //显示之后不隐藏
            view().hideSendBottomView();
            sendViewShowing = false;
        } else if (!sendViewShowing && scrolledComment && info != null && info.countCollect != null) {
            view().showSendBottomView();
            sendViewShowing = true;
        }
    }
}
