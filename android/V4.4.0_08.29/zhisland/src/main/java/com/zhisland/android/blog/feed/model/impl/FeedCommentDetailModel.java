package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.model.IFeedCommentDetailModel;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 *
 */
public class FeedCommentDetailModel implements IFeedCommentDetailModel {

    private FeedApi feedApi;

    public FeedCommentDetailModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }

    /**
     * 获取回复列表
     */
    @Override
    public Observable<ZHPageData<Reply>> getReplyData(final long commentId,final String nextId) {
        return Observable.create(new AppCall<ZHPageData<Reply>>() {
            @Override
            protected Response<ZHPageData<Reply>> doRemoteCall() throws Exception {
                Call<ZHPageData<Reply>> call = feedApi.getCommentDetail(commentId, nextId, 20);
                return call.execute();
            }
        });
    }

    /**
     * 删除评论
     */
    @Override
    public Observable<Void> deleteComment(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.deleteComment(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对评论点赞
     */
    @Override
    public Observable<Void> commentLike(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.commentLike(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对评论取消点赞
     */
    @Override
    public Observable<Void> commentLikeCancel(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.commentLikeCancel(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对评论或资讯评论的回复，进行回复。
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    @Override
    public Observable<Reply> commentReply(final long viewpointId, final Long replyId, final String content) {
        return Observable.create(new AppCall<Reply>() {
            @Override
            protected Response<Reply> doRemoteCall() throws Exception {
                Call<Reply> call = feedApi.commentReply(viewpointId, replyId, content);
                return call.execute();
            }
        });
    }

    /**
     * 删除评论回复
     */
    @Override
    public Observable<Void> deleteReply(final long replyId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = feedApi.deleteReply(replyId);
                return call.execute();
            }
        });
    }

}
