package com.zhisland.android.blog.freshtask.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskCommentDetail;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.tabhome.bean.FreshTaskAndComment;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * 新手任务api
 * Created by 向飞 on 2016/5/27.
 */
public interface FreshTaskApi {

    /**
     * 新手任务 推荐的神评论列表
     */
    @GET(Config.API_PART + "/interception/boot")
    @Headers({"apiVersion:1.1"})
    Call<FreshTaskAndComment> checkFreshTaskAndRecommendComment();

    /**
     * 推荐的神评论列表
     *
     * @return
     */
    @GET(Config.API_PART + "/recommend/comment/boot")
    @Headers({"apiVersion:1.0"})
    Call<List<UserComment>> getRecommendComment();

    /**
     * 上传页面进入情况
     *
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/taskCard/readed")
    @Headers({"apiVersion:1.0"})
    Call<Void> uploadVisited(@Field("data") String type);

    /**
     * 任务卡片列表
     *
     * @return
     */
    @GET(Config.API_PART + "/taskCard/list")
    @Headers({"apiVersion:1.1"})
    Call<TaskCardList> getFreshTask();

    /**
     * 获取可能认识的人
     */
    @GET(Config.API_PART + "/relation/friend/mayknow/list/task")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<User>> getContactFriend(@Query("nextId") String nextId);

    /**
     * 加好友
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/relation/task/mayknow/friend/apply")
    @Headers({"apiVersion:1.0"})
    Call<Void> addFriend(@Field("data") String data);

    /**
     * 获取可召唤好友列表
     */
    @GET(Config.API_PART + "/relation/summon/list")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<User>> getCallFriendList(@Query("nextId") String nextId);

    /**
     * 召唤好友
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/relation/task/summon/friend/apply")
    @Headers({"apiVersion:1.0"})
    Call<Void> callFriend(@Field("data") String data);

    /**
     * 新手任务，获取好友神评论
     */
    @GET(Config.API_PART + "/taskCard/comment")
    @Headers({"apiVersion:1.0"})
    Call<TaskCommentDetail> getFriendCommentData();

    /**
     * 新手任务，更新用户形象照
     */
    @FormUrlEncoded
    @PUT(Config.API_PART + "/user/figure")
    @Headers({"apiVersion:1.0"})
    Call<Void> updateUserFigure(@Field("figure") String figure);

    /**
     * 新手任务，更新用户个人简介
     */
    @FormUrlEncoded
    @PUT(Config.API_PART + "/user/introduce/task")
    @Headers({"apiVersion:1.0"})
    Call<Void> updateUserIntroduction(@Field("introduce") String introduction);

    /**
     * 通过通讯录求邀请 normal
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/contacts/match/request")
    @Headers({"apiVersion:1.1"})
    Call<ZHPageData<InviteUser>> requestInviteNormal(@Field("data") String data, @Field("count") int count);

    /**
     * 通过通讯录求邀请 more
     */
    @GET(Config.API_PART + "/contacts/match/request")
    @Headers({"apiVersion:1.1"})
    Call<ZHPageData<InviteUser>> requestInviteMore(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 求邀请
     */
     @FormUrlEncoded
     @POST(Config.API_PART + "/contacts/request")
     @Headers({"apiVersion:1.1"})
     Call<Void> requestInvite(@Field("userId") long userId, @Field("explanation") String explanation);

}
