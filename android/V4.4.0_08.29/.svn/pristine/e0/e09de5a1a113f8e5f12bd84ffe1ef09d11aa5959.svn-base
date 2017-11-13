package com.zhisland.android.blog.info.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

/**
 * 资讯tab model
 * Created by Mr.Tui on 2016/6/29.
 */
public class InfoHomeTabModel implements IMvpModel {

    public void getInvitationRequests(Context context, TaskCallback<ZHPageData<User>> callback) {
        ZHApis.getUserApi().getInvitationRequests(context, null,
                callback);
    }

}
