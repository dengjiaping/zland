package com.zhisland.android.blog.feed.model.remote;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.TimelineData;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * 新鲜事 api
 */
public interface FeedApi {

    //region 发布、列表

    /**
     * 获取商圈的新鲜事列表
     */
    @GET(Config.API_PART + "/circle")
    @Headers({"apiVersion:1.0"})
    Call<TimelineData> getFeedList(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取我的动态列表
     */
    @GET(Config.API_PART + "/feed/user/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Feed>> getMyPubFeedList(@Path("userId") long userId, @Query("nextId") String nextId, @Query("count") int count);

    @FormUrlEncoded
    @POST(Config.API_PART + "/thing")
    @Headers({"apiVersion:1.0"})
    Call<Feed> createsFeed(@Field("thing") String feedGson);

    /**
     * 分享活动到商圈
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/event/{id}/share/circle")
    @Headers({"apiVersion:1.0"})
    Call<Feed> shareEvent(@Path("id") long eventId, @Field("reason") String reason);

    /**
     * 分享资讯到商圈
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/{id}/share/circle")
    @Headers({"apiVersion:1.0"})
    Call<Feed> shareInfo(@Path("id") long infoId, @Field("reason") String reason);
    //endregion


    //region 对单条feed的操作

    /**
     * 对feed点赞
     */
    @POST(Config.API_PART + "/like/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> feedLike(@Path("id") String feedId);

    /**
     * 对feed取消点赞
     */
    @DELETE(Config.API_PART + "/like/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> feedCancelLike(@Path("id") String feedId);

    /**
     * 对feed转播
     */
    @POST(Config.API_PART + "/forward/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Feed> feedTransmit(@Path("id") String feedId);

    /**
     * 对feed取消转播
     */
    @DELETE(Config.API_PART + "/forward/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Feed> feedCancelTransmit(@Path("id") String feedId);

    /**
     * 获取feed详情
     */
    @GET(Config.API_PART + "/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Feed> getFeedDetail(@Path("id") String feedId);

    /**
     * 删除feed
     */
    @DELETE(Config.API_PART + "/feed/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> deleteFeed(@Path("id") String feedId);

    /**
     * 举报feed
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/feed/{id}/report")
    @Headers({"apiVersion:1.0"})
    Call<Void> reportFeed(@Path("id") String feedId, @Field("reasonId") String reasonId);

    /**
     * 获取举报原因
     */
    @GET(Config.API_PART + "/dict/feed/report/reason")
    @Headers({"apiVersion:1.0"})
    Call<ArrayList<Country>> getReportReason();

    //endregion

    //region 评论

    /**
     * 获取评论列表
     */
    @GET(Config.API_PART + "/feed/{id}/comment")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Comment>> getCommentList(@Path("id") String feedId, @Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取评论详情,回复列表
     */
    @GET(Config.API_PART + "/feed/comment/{id}/reply")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Reply>> getCommentDetail(@Path("id") long commentId, @Query("nextId") String nextId, @Query("count") int count);

    /**
     * 对feed评论点赞
     */
    @POST(Config.API_PART + "/like/comment/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> commentLike(@Path("id") long commentId);

    /**
     * 对feed评论取消点赞
     */
    @DELETE(Config.API_PART + "/like/comment/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> commentLikeCancel(@Path("id") long commentId);

    /**
     * 对feed评论
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/feed/{id}/comment")
    @Headers({"apiVersion:1.0"})
    Call<Comment> comment(@Path("id") String feedId, @Field("content") String content);

    /**
     * 对feed评论回复回复
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/feed/comment/{id}/reply")
    @Headers({"apiVersion:1.0"})
    Call<Reply> commentReply(@Path("id") long feedId, @Field("replyId") Long replyId, @Field("content") String content);

    /**
     * 删除feed评论
     */
    @DELETE(Config.API_PART + "/feed/comment/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> deleteComment(@Path("id") long id);

    /**
     * 删除feed评论回复
     */
    @DELETE(Config.API_PART + "/feed/comment/reply/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> deleteReply(@Path("id") long replyId);


    //endregion

}
