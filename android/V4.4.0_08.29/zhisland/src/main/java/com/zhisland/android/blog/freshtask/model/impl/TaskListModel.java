package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.aa.api.TaskUpdateUser;
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
public class TaskListModel implements IMvpModel {

    private FreshTaskApi api;

    public TaskListModel() {
        api = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    /**
     * 更新用户头像
     *
     * @param context
     * @param tmpUser
     * @param callback
     */
    public void updateUser(Context context, User tmpUser, TaskCallback<Object> callback) {
        ZHApis.getUserApi().updateUser(context, tmpUser,
                TaskUpdateUser.TYPE_UPDATE_OTHER,
                callback);
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
