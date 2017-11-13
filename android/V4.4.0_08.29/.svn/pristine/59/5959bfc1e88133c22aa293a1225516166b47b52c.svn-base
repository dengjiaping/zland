package com.zhisland.android.blog.invitation.model.impl;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.invitation.bean.Constants;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.android.blog.invitation.model.IInvitationRequestModel;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/8/10.
 */
public class InvitationRequestModel implements IInvitationRequestModel {

    private InvitationApi api;

    public InvitationRequestModel() {
        api = RetrofitFactory.getInstance().getApi(InvitationApi.class);
    }

    /**
     * 获取求邀请的数据
     *
     * @return
     */
    public Observable<InvitationData> getInvitationData() {
        return Observable.create(new AppCall<InvitationData>() {
            @Override
            protected Response<InvitationData> doRemoteCall() throws Exception {
                Call<InvitationData> call = api.getInvitationData();
                return call.execute();
            }
        });
    }

    /**
     * @param user
     * @return
     */
    public Observable<Object> ignoreHaikeRequest(final User user) {
        return Observable.create(new AppCall<Object>() {
            @Override
            protected Response<Object> doRemoteCall() throws Exception {
                Call<Object> call = api.updateRequestStatus(user.uid, Constants.STATUS_REFUSE, null);
                return call.execute();
            }
        });
    }

    @Override
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
