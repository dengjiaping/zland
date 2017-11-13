package com.zhisland.android.blog.profilemvp.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.List;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by arthur on 2016/9/6.
 */
public interface RelationApi {

    /**
     * 获取我关注的人列表
     */
    @GET(Config.API_PART + "/user/relation/attention")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<InviteUser>> getAttentionList(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取我的粉丝列表
     */
    @GET(Config.API_PART + "/user/relation/fans")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<InviteUser>> getFansList(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 忽略推荐
     *
     * @param uid
     * @return
     */
    @PUT(Config.API_PART + "/user/relation/attention/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> ignoreRecommend(@Path("userId") long uid);

    /**
     * 关注
     *
     * @param uid
     * @param source
     * @return
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/user/relation/attention/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> follow(@Path("userId") long uid, @Field("source") String source);

    /**
     * 取消关注
     *
     * @param uid
     * @return
     */
    @DELETE(Config.API_PART + "/user/relation/attention/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> unFollow(@Path("userId") long uid);

    /**
     * 获取用户当前 粉丝 关注 热度信息
     */
    @GET(Config.API_PART + "/center/heat/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<UserHeatReport> getUserHeatReport(@Path("userId") long uid);

    /**
     * 移除粉丝
     */
    @DELETE(Config.API_PART + "/user/relation/fans/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> removeFans(@Path("userId") long userId);

    /**
     * 获取可能感兴趣的人
     */
    @GET(Config.API_PART + "/user/relation/recommend")
    @Headers({"apiVersion:1.0"})
    Call<List<RecommendUser>> getIntrestPeople();
}
