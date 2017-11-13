package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.freshtask.model.remote.ResourceApi;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.mvp.model.IMvpModel;


/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class AddResourceModel implements IMvpModel {

    private ResourceApi resourceApi;

    public AddResourceModel() {
        resourceApi = RetrofitFactory.getInstance().getApi(ResourceApi.class);
    }

    public void addResource(Context context, Resource resource, TaskCallback<Object> callback) {
        ZHApis.getUserApi().addReource(context, GsonHelper.GetCommonGson().toJson(resource), callback);
    }

}
