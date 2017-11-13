package com.zhisland.android.blog.freshtask.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.freshtask.bean.TaskCommentDetail;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 好友神评论
 */
public class FriendCommentModel implements IMvpModel {

    private FreshTaskApi api;

    public FriendCommentModel() {
        api = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    /**
     * 新手任务，获取好友神评论
     */
    public Observable<TaskCommentDetail> getFriendCommentData() {
        return Observable.create(new AppCall<TaskCommentDetail>() {
            @Override
            protected Response<TaskCommentDetail> doRemoteCall() throws Exception {
                Call<TaskCommentDetail> call = api.getFriendCommentData();
                return call.execute();
            }
        });
    }
}
