package com.zhisland.android.blog.message.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.ISystemMessageModel;
import com.zhisland.android.blog.message.model.remoter.MessageApi;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/13.
 */
public class SystemMessageModel implements ISystemMessageModel {

    private MessageApi messageApi;

    public SystemMessageModel() {
        messageApi = RetrofitFactory.getInstance().getApi(MessageApi.class);
    }

    @Override
    public Observable<ZHPageData<Message>> loadData(final String nextId, final int i) {
        return Observable.create(new AppCall<ZHPageData<Message>>() {
            @Override
            protected Response<ZHPageData<Message>> doRemoteCall() throws Exception {
                Call<ZHPageData<Message>> call = messageApi.getSystemMessage(nextId, i);
                return call.execute();
            }
        });
    }
}
