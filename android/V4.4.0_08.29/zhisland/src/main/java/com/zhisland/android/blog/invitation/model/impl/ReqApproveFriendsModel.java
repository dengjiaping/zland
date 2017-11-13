package com.zhisland.android.blog.invitation.model.impl;

import com.j256.ormlite.dao.DaoManager;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.bean.Constants;
import com.zhisland.android.blog.invitation.model.IReqApproveFriendsModel;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;

import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by lipengju on 16/8/10.
 */
public class ReqApproveFriendsModel implements IReqApproveFriendsModel {


    private List<InviteUser> users;


    private InvitationApi api;

    public ReqApproveFriendsModel() {
        api = RetrofitFactory.getInstance().getApi(InvitationApi.class);
    }
    @Override
    public void setData(List<InviteUser> users) {
        this.users = users;
    }

    @Override
    public Observable<List<InviteUser>> loadData() {
        return Observable.just(users);
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
