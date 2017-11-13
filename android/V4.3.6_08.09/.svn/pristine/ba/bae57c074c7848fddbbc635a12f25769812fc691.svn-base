package com.zhisland.android.blog.freshtask.model.remote;

import com.zhisland.android.blog.common.app.Config;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public interface ResourceApi {

    @FormUrlEncoded
    @POST(Config.API_PART + "/resource/save/task")
    Call<Long> addResource(@Field("resource") String resource);
}
