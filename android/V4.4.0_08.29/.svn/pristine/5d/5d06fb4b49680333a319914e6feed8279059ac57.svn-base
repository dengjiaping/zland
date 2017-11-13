package com.zhisland.android.blog.message.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.message.model.IFansMessageModel;
import com.zhisland.android.blog.message.model.remoter.MessageApi;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/13.
 */
public class FansMessageModel implements IFansMessageModel {

    private MessageApi messageApi;
    private RelationApi relationApi;

    public FansMessageModel() {
        messageApi = RetrofitFactory.getInstance().getApi(MessageApi.class);
        relationApi = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class);
    }

    @Override
    public Observable<ZHPageData<InviteUser>> loadData(final String nextId, final int i) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = messageApi.getNewFans(nextId, i);
                return call.execute();
            }
        });
    }

    @Override
    public Observable<Void> follow(final long uid, final String source) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = relationApi.follow(uid, source);
                return call.execute();
            }
        });
    }
}
