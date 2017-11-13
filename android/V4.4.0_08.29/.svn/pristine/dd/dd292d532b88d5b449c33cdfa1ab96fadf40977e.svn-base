package com.zhisland.android.blog.invitation.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.bean.InvitationData;
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
 * 邀请远程访问API
 * Created by arthur on 2016/8/9.
 */
public interface InvitationApi {

    /**
     * 新手任务 推荐的神评论列表
     */
    @GET(Config.API_PART + "/invite/approval")
    @Headers({"apiVersion:1.0"})
    Call<InvitationData> getDealedList();

    /**
     * 邀请主页数据
     */
    @GET(Config.API_PART + "/invite")
    @Headers({"apiVersion:1.1"})
    Call<InvitationData> getInvitationData();

    /**
     * 邀请搜索
     */
    @GET(Config.API_PART + "/search/invite")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<InviteUser>> searchInviteUser(@Query("keyword") String keyword, @Query("nextId") String nextId, @Query("count") int count);

    /**
     * 可批准列表
     *
     * @param data   通讯录数据
     * @param nextId
     * @return
     */
    @FormUrlEncoded
    @POST(Config.API_PART + "/contacts/match/pre")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<InviteUser>> getConfirmableUsers(@Field("data") String data, @Field("nextId") String nextId);

    /**
     * 忽略新手认证的请求
     */
    @FormUrlEncoded
    @PUT(Config.API_PART + "/invite/request")
    @Headers({"apiVersion:1.1"})
    Call<Object> updateRequestStatus(@Field("userId") long userId, @Field("status") int status, @Field("explanation") String explanation);

    /**
     * 预批准
     */
    @FormUrlEncoded
    @PUT(Config.API_PART + "/invite/approve/pretreatment")
    @Headers({"apiVersion:1.0"})
    Call<Object> preConfirm(@Field("userId") long userId);
}
