package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 远程调用和本地调用
 * Created by 向飞 on 2016/5/27.
 */
public class TaskContainerModel implements IMvpModel {

    private FreshTaskApi api;

    public TaskContainerModel() {
        api = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    /**
     * 上传页面进入状态
     *
     * @param visited
     */
    public Observable<Void> uploadVisit(final String visited) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.uploadVisited(visited);
                return call.execute();
            }
        });
    }

    /**
     * 更新用户形象照
     */
    public void updateUserFigure(Context context, String figure, TaskCallback<Object> callback) {
        ZHApis.getUserApi().updateUserFigure(context, figure, callback);
    }

    /**
     * 缓存USER
     *
     * @param tmpUser
     */
    public void cacheUser(User tmpUser) {
        DBMgr.getMgr().getUserDao()
                .creatOrUpdateUserNotNull(tmpUser);
    }

    /**
     * 获取当前挡路用户的信息
     *
     * @return
     */
    public User getSelf() {
        return DBMgr.getMgr().getUserDao().getSelfUser();
    }
}
