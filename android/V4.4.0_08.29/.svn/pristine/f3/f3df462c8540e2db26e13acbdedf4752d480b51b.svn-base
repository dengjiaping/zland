package com.zhisland.android.blog.message.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.IInteractionModel;
import com.zhisland.android.blog.message.model.remoter.MessageApi;
import com.zhisland.lib.component.adapter.ZHPageData;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by arthur on 2016/9/6.
 */
public class InteractionModel implements IInteractionModel {

    private MessageApi relationApi;

    public InteractionModel() {
        relationApi = RetrofitFactory.getInstance().getHttpsApi(MessageApi.class);
    }





    public Observable<ZHPageData<Message>> getInteractionMessageList(final String id,final int count) {
        return Observable.create(new AppCall<ZHPageData<Message>>() {
            @Override
            protected Response<ZHPageData<Message>> doRemoteCall() throws Exception {
                Call<ZHPageData<Message>> call = relationApi.getInteractionMessage(id,count);
                return call.execute();
            }
        });
    }

}
