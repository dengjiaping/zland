package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.freshtask.model.IFriendCallModel;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 建立好友圈 召唤好友 model
 */
public class FriendCallModel implements IFriendCallModel {

    private FreshTaskApi api;

    public FriendCallModel() {
        api = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    /**
     * 获取可召唤好友列表
     */
    @Override
    public Observable<ZHPageData<User>> getCallFriendList() {
        return Observable.create(new AppCall<ZHPageData<User>>() {
            @Override
            protected Response<ZHPageData<User>> doRemoteCall() throws Exception {
                Call<ZHPageData<User>> call = api.getCallFriendList(null);
                return call.execute();
            }
        });
    }

    /**
     * 召唤好友
     */
    @Override
    public Observable<Void> callFriend(final List<User> users) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                String data = GsonHelper.GetCommonGson().toJson(users);
                Call<Void> call = api.callFriend(data);
                return call.execute();
            }
        });
    }

    /**
     * 召唤好友
     */
    @Override
    public void callFriend(Context context, final List<User> users, TaskCallback<Object> responseCallback) {
        String data = GsonHelper.GetCommonGson().toJson(users);
        ZHApis.getUserApi().callFriend(context, data, responseCallback);
    }
}
