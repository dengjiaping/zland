package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * feed详情model
 * Created by arthur on 2016/8/31.
 */
public interface IFeedDetailCommentModel extends IMvpModel {

    public Observable<ZHPageData<Comment>> getCommentList(final String feedId, final String nextId);

    public Observable<Void> commentLike(final long commentId);

    /**
     * 对评论取消点赞
     */
    public Observable<Void> commentLikeCancel(final long commentId);

    public Observable<Reply> commentReply(final long viewpointId, final Long replyId, final String content);

    public Observable<Void> deleteReply(final long replyId);

    public Observable<Comment> comment(final String feedId, final String content);

    public Observable<Void> deleteComment(final long commentId);

}
