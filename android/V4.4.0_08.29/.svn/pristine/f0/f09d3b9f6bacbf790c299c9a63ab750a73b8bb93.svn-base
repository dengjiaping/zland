package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * feed详情model
 * Created by arthur on 2016/8/31.
 */
public interface IFeedCommentDetailModel extends IMvpModel {

    /**
     * 获取回复列表
     */
    public Observable<ZHPageData<Reply>> getReplyData(final long commentId,final String nextId);

    /**
     * 删除评论
     */
    public Observable<Void> deleteComment(final long commentId);

    /**
     * 对评论点赞
     */
    public Observable<Void> commentLike(final long commentId);

    /**
     * 对评论取消点赞
     */
    public Observable<Void> commentLikeCancel(final long commentId);

    /**
     * 对评论或资讯评论的回复，进行回复。
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    public Observable<Reply> commentReply(final long viewpointId, final Long replyId, final String content);

    /**
     * 删除评论回复
     */
    public Observable<Void> deleteReply(final long replyId);

}
