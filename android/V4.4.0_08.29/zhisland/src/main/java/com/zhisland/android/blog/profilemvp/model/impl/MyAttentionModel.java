package com.zhisland.android.blog.profilemvp.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profilemvp.model.IMyAttentionModel;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/9/6.
 */
public class MyAttentionModel implements IMyAttentionModel {

    private RelationApi relationApi;

    public MyAttentionModel() {
        relationApi = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class);
    }

    /**
     * 获取评论列表
     */
    @Override
    public Observable<ZHPageData<InviteUser>> getAttentionList(final String nextId) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = relationApi.getAttentionList(nextId, 20);
                return call.execute();
            }
        });
    }

}
