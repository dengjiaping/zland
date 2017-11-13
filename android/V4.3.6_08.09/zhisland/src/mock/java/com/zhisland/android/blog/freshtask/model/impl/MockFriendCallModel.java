package com.zhisland.android.blog.freshtask.model.impl;

import android.content.Context;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.model.IFriendCallModel;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by arthur on 2016/6/21.
 */
public class MockFriendCallModel extends FriendCallModel {
    @Override
    public Observable<ZHPageData<User>> getCallFriendList() {
        ZHPageData<User> data = new ZHPageData<>();
        data.data = new ArrayList<>(1);

        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        data.data.add(selfUser);
        return Observable.just(data);
    }

}
