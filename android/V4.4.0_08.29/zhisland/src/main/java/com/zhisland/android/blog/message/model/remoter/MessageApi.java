package com.zhisland.android.blog.message.model.remoter;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by arthur on 2016/9/13.
 */
public interface MessageApi {

    /**
     * 获取新增粉丝消息列表数据
     */
    @GET(Config.API_PART + "/notification/center/fans")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<InviteUser>> getNewFans(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取新增粉丝消息列表数据
     */
    @GET(Config.API_PART + "/notification/center/system")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Message>> getSystemMessage(@Query("nextId") String nextId, @Query("count") int count);

    /**
     * 获取互动消息
     *
     * @param
     * @return
     */
    @GET(Config.API_PART + "/notification/center/interactive")
    @Headers({"apiVersion:1.0"})
    Call<ZHPageData<Message>> getInteractionMessage(@Query("nextId") String nextId, @Query("count") int count);
}
