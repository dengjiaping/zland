package com.zhisland.android.blog.profilemvp.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.lib.component.adapter.ZHPageData;

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
 * 个人中心api
 * Created by Mr.Tui on 2016/9/6.
 */
public interface ProfileApi {

    /**
     * 个人主页 上传相册照片
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/user/photo/gallery")
    @Headers({"apiVersion:1.0"})
    Call<Void> addPhoto(@Field("desc") String desc, @Field("data") String data);

    /**
     * 分页获取相册
     */
    @GET(Config.API_PART + "/user/photo/gallery/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<UserPhoto>> getPhoto(@Path("userId") long userId, @Query("nextId") String nextId, @Query("count") int count);

    /**
     * 删除相册图片（单张）
     */
    @DELETE(Config.API_PART + "/user/photo/gallery/{photoId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> deletePhoto(@Path("photoId") long photoId);

    /**
     * 用户点赞
     */
    @POST(Config.API_PART + "/like/user/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> likeUser(@Path("id") long userId);

    /**
     * 取消用户点赞
     */
    @DELETE(Config.API_PART + "/like/user/{id}")
    @Headers({"apiVersion:1.0"})
    Call<Void> cancelLikeUser(@Path("id") long userId);

}
