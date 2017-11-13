package com.zhisland.android.blog.invitation.model.impl;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.invitation.bean.Constants;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/8/10.
 */
public class ApproveHaiKeModel implements IMvpModel {

    private InvitationApi api;

    public ApproveHaiKeModel() {
        api = RetrofitFactory.getInstance().getApi(InvitationApi.class);
    }

    public Observable<Object> allowHaikeRequest(final long uid, final String explanation) {
        return Observable.create(new AppCall<Object>() {
            @Override
            protected Response<Object> doRemoteCall() throws Exception {
                Call<Object> call = api.updateRequestStatus(uid, Constants.STATUS_AGREE, explanation);
                return call.execute();
            }
        });
    }
}
