package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.model.IFeedDetailCommentModel;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * feed详情中评论的model
 */
public class FeedDetailCommentModel implements IFeedDetailCommentModel {

    private FeedApi feedApi;

    public FeedDetailCommentModel() {
        feedApi = RetrofitFactory.getInstance().getApi(FeedApi.class);
    }

    /**
     * 获取评论列表
     */
    @Override
    public Observable<ZHPageData<Comment>> getCommentList(final String feedId, final String nextId) {
        return Observable.create(new AppCall<ZHPageData<Comment>>() {
            @Override
            protected Response<ZHPageData<Comment>> doRemoteCall() throws Exception {
                Call<ZHPageData<Comment>> call = feedApi.getCommentList(feedId, nextId, 10);
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
     * 对评论或feed评论的回复，进行回复。
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

    /**
     * 对feed评论
     */
    @Override
    public Observable<Comment> comment(final String feedId, final String content) {
        return Observable.create(new AppCall<Comment>() {
            @Override
            protected Response<Comment> doRemoteCall() throws Exception {
                Call<Comment> call = feedApi.comment(feedId, content);
                return call.execute();
            }
        });
    }

    /**
     * 删除feed评论
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

}
