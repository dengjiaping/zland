package com.zhisland.android.blog.invitation.model.impl;

import com.zhisland.android.blog.aa.dto.PhoneContact;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.IInvitationConfirmModel;
import com.zhisland.android.blog.invitation.model.remote.InvitationApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/8/10.
 */
public class InvitationConfirmModel implements IInvitationConfirmModel {

    private InvitationApi api;

    public InvitationConfirmModel() {
        api = RetrofitFactory.getInstance().getApi(Config.getRetrofitHttpsBaseUrl(), InvitationApi.class);
    }

    @Override
    public Observable<ZHPageData<InviteUser>> getConfirmableUsers(final String contactData, final String nextId) {
        return Observable.create(new AppCall<ZHPageData<InviteUser>>() {
            @Override
            protected Response<ZHPageData<InviteUser>> doRemoteCall() throws Exception {
                Call<ZHPageData<InviteUser>> call = api.getConfirmableUsers(contactData, nextId);
                return call.execute();
            }
        });
    }

    @Override
    public boolean isContactAllowed() {
        return PhoneContactUtil.hasContactData();
    }

    @Override
    public PhoneContactUtil.ContactResult<String> getChangeContacts() {
        return PhoneContactUtil.getChangeContact();
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
