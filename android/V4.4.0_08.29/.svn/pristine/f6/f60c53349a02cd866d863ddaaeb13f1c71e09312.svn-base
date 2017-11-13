package com.zhisland.android.blog.profilemvp.model.impl;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profilemvp.model.IMyFansModel;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyFansModel implements IMyFansModel {

    private RelationApi relationApiHttps;

    public MyFansModel() {
        relationApiHttps = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class);
    }

    @Override
    public Observable<ZHPageData<InviteUser>> getFansList(final String nextId) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = relationApiHttps.getFansList(nextId, 20);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Void> follow(final User user, final String source) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = relationApiHttps.follow(user.uid, source);
                return call.execute();
            }
        });
    }

}
