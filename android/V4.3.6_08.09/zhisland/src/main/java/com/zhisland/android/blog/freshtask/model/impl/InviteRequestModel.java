package com.zhisland.android.blog.freshtask.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 新手任务 求邀请 升级 model
 */
public class InviteRequestModel implements IMvpModel {

    private FreshTaskApi api;

    public InviteRequestModel() {
        api = RetrofitFactory.getInstance().getHttpsApi(FreshTaskApi.class);
    }

    /**
     * 通过通讯录求邀请 normal
     */
    public Observable<ZHPageData<InviteUser>> requestInviteNormal(final String data) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = api.requestInviteNormal(data, 100);
                return call.execute();
            }
        });
    }

    /**
     * 通过通讯录求邀请 more
     */
    public Observable<ZHPageData<InviteUser>> requestInviteMore(final String nextId) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = api.requestInviteMore(nextId, 100);
                return call.execute();
            }
        });
    }

    /**
     * 求邀请
     */
    public Observable<Void> requestInvite(final long userId, final String explanation) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.requestInvite(userId, explanation);
                return call.execute();
            }
        });
    }
}
