package com.zhisland.android.blog.immvp.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.remote.FeedApi;
import com.zhisland.android.blog.immvp.bean.ChatReceiveVerify;
import com.zhisland.android.blog.immvp.bean.ChatSendVerify;
import com.zhisland.android.blog.immvp.model.remote.IIMApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/18.
 */
public class IMModel implements IMvpModel {

    private IIMApi imApi;

    public IMModel() {
        imApi = RetrofitFactory.getInstance().getApi(IIMApi.class);
    }

    /**
     * 开启新会话
     */
    public Observable<ChatSendVerify> startIM(final long uid) {
        return Observable.create(new AppCall<ChatSendVerify>() {
            @Override
            protected Response<ChatSendVerify> doRemoteCall() throws Exception {
                Call<ChatSendVerify> call = imApi.tryStartIm(uid);
                return call.execute();
            }
        });
    }

    /**
     * 开启新会话
     */
    public Observable<ChatReceiveVerify> getSenderInfo(final long uid) {
        return Observable.create(new AppCall<ChatReceiveVerify>() {
            @Override
            protected Response<ChatReceiveVerify> doRemoteCall() throws Exception {
                Call<ChatReceiveVerify> call = imApi.getImSenderInfo(uid);
                return call.execute();
            }
        });
    }

    /**
     * 使用特权开启新会话
     */
    public Observable<Void> costPrivilege(final long uid) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = imApi.costPrivilege(uid);
                return call.execute();
            }
        });
    }

}
