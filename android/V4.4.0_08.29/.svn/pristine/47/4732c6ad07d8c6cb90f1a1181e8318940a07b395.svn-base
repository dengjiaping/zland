package com.zhisland.android.blog.invitation.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/8/9.
 */
public class SearchModel implements IMvpModel {

    private InvitationApi api;

    public SearchModel() {
        api = RetrofitFactory.getInstance().getApi(InvitationApi.class);
    }

    public Observable<ZHPageData<InviteUser>> search(final String key, final String nextId) {
        AppCall appCall = new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = api.searchInviteUser(key, nextId, 20);
                return call.execute();
            }
        };
        return Observable.create(appCall);
    }

    public Observable<Integer> preConfirm(final long uid) {
        return Observable.create(new AppCall<Integer>() {
            @Override
            protected Response<Integer> doRemoteCall() throws Exception {
                Call<Integer> call = api.preConfirm(uid);
                return call.execute();
            }

        });
    }

}
