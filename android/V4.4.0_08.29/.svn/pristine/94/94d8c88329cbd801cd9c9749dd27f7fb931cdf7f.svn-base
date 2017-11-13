package com.zhisland.android.blog.immvp.model.remote;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.immvp.bean.ChatReceiveVerify;
import com.zhisland.android.blog.immvp.bean.ChatSendVerify;
import com.zhisland.android.blog.tabhome.bean.FreshTaskAndComment;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * IM相关的API
 * <p/>
 * Created by arthur on 2016/9/18.
 */
public interface IIMApi {

    /**
     * 尝试开启新的聊天
     */
    @GET(Config.API_PART + "/im/chat/send/verify/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<ChatSendVerify> tryStartIm(@Path("userId") long userId);

    /**
     * 获取消息发送方的信息
     */
    @GET(Config.API_PART + "/im/chat/receive/verify/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<ChatReceiveVerify> getImSenderInfo(@Path("userId") long userId);

    /**
     * 聊天特权消费
     */
    @GET(Config.API_PART + "/im/chat/privilege/{userId}")
    @Headers({"apiVersion:1.0"})
    Call<Void> costPrivilege(@Path("userId") long userId);

}
