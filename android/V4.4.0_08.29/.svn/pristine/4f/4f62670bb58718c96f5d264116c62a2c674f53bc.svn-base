package com.zhisland.android.blog.invitation.model.impl;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.android.blog.invitation.model.IInvitationDealedModel;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;

import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/8/9.
 */
public class InvitationDealedModel implements IInvitationDealedModel {

    private InvitationApi api;
    private InviteStructure inviteStructure;


    public InvitationDealedModel() {
        api = RetrofitFactory.getInstance().getApi(InvitationApi.class);
    }

    @Override
    public Observable<InvitationData> loadData() {
        return Observable.create(new AppCall<InvitationData>() {
            @Override
            protected Response<InvitationData> doRemoteCall() throws Exception {
                Call<InvitationData> call = api.getDealedList();
                return call.execute();
            }
        });
    }
}
