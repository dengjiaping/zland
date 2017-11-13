package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
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
 * 建立好友圈 加好友 model
 */
public class FriendAddModel implements IMvpModel {

    private FreshTaskApi api;

    public FriendAddModel() {
        api = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    /**
     * 上传联系人
     */
    public void uploadContact(final Context context, PhoneContactUtil.ContactResult<String> contacts, TaskCallback<Object> callback) {
        ZHApis.getCommonApi().UploadContact(context, contacts.result, callback);
    }

    /**
     * 获取可能认识的人
     */
    public Observable<ZHPageData<User>> getContactFriend() {
        return Observable.create(new AppCall<ZHPageData<User>>() {
            @Override
            protected Response<ZHPageData<User>> doRemoteCall() throws Exception {
                Call<ZHPageData<User>> call = api.getContactFriend(null);
                return call.execute();
            }
        });
    }

    /**
     * 加好友
     */
    public Observable<Void> addFriend(final List<User> users) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                String data = GsonHelper.GetCommonGson().toJson(users);
                Call<Void> call = api.addFriend(data);
                return call.execute();
            }
        });
    }

}
