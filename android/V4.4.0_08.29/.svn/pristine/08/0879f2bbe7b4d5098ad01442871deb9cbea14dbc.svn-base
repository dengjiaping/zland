package com.zhisland.android.blog.info.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.info.bean.CountCollect;
import com.zhisland.android.blog.info.bean.RInfoPageData;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.SectionInfo;
import com.zhisland.android.blog.info.bean.ZHInfo;
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
 * Created by Mr.Tui on 2016/7/1.
 */
public interface NewsApi {

    /**
     * 获取推荐资讯列表
     */
    @GET(Config.API_PART + "/news/recommend/pull/{type}")
    @Headers({"apiVersion:1.0"})
    Call<RInfoPageData<ZHInfo>> getRecommendInfo(@Path("type") String type, @Query("count") int count, @Query("nextId") String nextId);

    /**
     * 获取精选资讯列表
     */
    @GET(Config.API_PART + "/news/choice/pull")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<SectionInfo>> getFeaturedInfo(@Query("count") int count, @Query("nextId") String nextId);

    /**
     * 获取资讯详情
     */
    @GET(Config.API_PART + "/news/{newsId}/detail")
    @Headers({"apiVersion:1.0"})
    Call<ZHInfo> getInfoDetail(@Path("newsId") long newsId);

    /**
     * 获取评论列表
     */
    @GET(Config.API_PART + "/news/{newsId}/viewpoint/list")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Comment>> getCommentList(@Path("newsId") long newsId, @Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取评论详情
     */
    @GET(Config.API_PART + "/news/viewpoint/{viewpointId}/detail")
    @Headers({"apiVersion:1.0"})
    Call<Comment> getCommentDetail(@Path("viewpointId") long viewpointId);

    /**
     * 获取举报类型
     */
    @GET(Config.API_PART + "/dict/news/report/reason")
    @Headers({"apiVersion:1.0"})
    Call<ArrayList<ReportType>> getReportType();

    /**
     * 举报资讯
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/{newsId}/report")
    @Headers({"apiVersion:1.0"})
    Call<Void> reportInfo(@Path("newsId") long newsId, @Field("reasonCode") String reasonCode, @Field("reasonDesc") String reasonDesc);

    /**
     * 检查url是否为可推荐资讯
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/recommend/url/check")
    @Headers({"apiVersion:1.0"})
    Call<RecommendInfo> checkUrl(@Field("url") String url);

    /**
     * 推荐资讯
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/recommend/url/publish")
    @Headers({"apiVersion:1.0"})
    Call<Void> recommendInfo(@Field("url") String url, @Field("title") String title, @Field("reason") String reason);

    /**
     * 资讯 顶操作
     */
    @POST(Config.API_PART + "/news/{newsId}/up")
    @Headers({"apiVersion:1.0"})
    Call<CountCollect> infoUp(@Path("newsId") long newsId);

    /**
     * 资讯 踩操作
     */
    @POST(Config.API_PART + "/news/{newsId}/down")
    @Headers({"apiVersion:1.0"})
    Call<CountCollect> infoDown(@Path("newsId") long newsId);

    /**
     * 对资讯评论
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/{newsId}/viewpoint/publish")
    @Headers({"apiVersion:1.0"})
    Call<Comment> comment(@Path("newsId") String newsId, @Field("content") String content);

    /**
     * 删除资讯评论
     */
    @POST(Config.API_PART + "/news/viewpoint/{viewpointId}/remove")
    @Headers({"apiVersion:1.0"})
    Call<Void> deleteComment(@Path("viewpointId") long viewpointId);

    /**
     * 对资讯评论点赞
     */
    @POST(Config.API_PART + "/news/viewpoint/{viewpointId}/like")
    @Headers({"apiVersion:1.0"})
    Call<Void> commentLike(@Path("viewpointId") long viewpointId);

    /**
     * 对资讯评论回复回复
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/viewpoint/{viewpointId}/reply/publish")
    @Headers({"apiVersion:1.0"})
    Call<Reply> commentReply(@Path("viewpointId") long viewpointId, @Field("replyId") long replyId, @Field("content") String content);

    /**
     * 对资讯评论的回复
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/news/viewpoint/{viewpointId}/reply/publish")
    @Headers({"apiVersion:1.0"})
    Call<Reply> commentReply(@Path("viewpointId") long viewpointId, @Field("content") String content);

    /**
     * 删除资讯评论回复
     */
    @DELETE(Config.API_PART + "/news/viewpoint/reply/{replyId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> deleteReply(@Path("replyId") long replyId);

}
