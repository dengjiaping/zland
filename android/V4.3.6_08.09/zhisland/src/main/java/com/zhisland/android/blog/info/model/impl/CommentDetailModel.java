package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 观点详情Model
 * Created by Mr.Tui on 2016/7/6.
 */
public class CommentDetailModel implements IMvpModel {

    private NewsApi api;

    public CommentDetailModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    /**
     * 获取观点详情。
     * */
    public Observable<Comment> getCommentDetail(final long commentId) {
        return Observable.create(new AppCall<Comment>() {
            @Override
            protected Response<Comment> doRemoteCall() throws Exception {
                Call<Comment> call = api.getCommentDetail(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论
     */
    public Observable<Comment> comment(final long newsId, final String content) {
        return Observable.create(new AppCall<Comment>() {
            @Override
            protected Response<Comment> doRemoteCall() throws Exception {
                Call<Comment> call = api.comment(newsId, content);
                return call.execute();
            }
        });
    }

    /**
     * 删除资讯评论
     */
    public Observable<Void> deleteComment(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.deleteComment(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论点赞
     */
    public Observable<Void> commentLike(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.commentLike(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论或资讯评论的回复，进行回复。
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    public Observable<Reply> commentReply(final long viewpointId, final Long replyId, final String content) {
        return Observable.create(new AppCall<Reply>() {
            @Override
            protected Response<Reply> doRemoteCall() throws Exception {
                Call<Reply> call;
                if (replyId == null) {
                    call = api.commentReply(viewpointId, content);
                } else {
                    call = api.commentReply(viewpointId, replyId, content);
                }
                return call.execute();
            }
        });
    }

    /**
     * 删除资讯评论回复
     */
    public Observable<Void> deleteReply(final long replyId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.deleteReply(replyId);
                return call.execute();
            }
        });
    }

}