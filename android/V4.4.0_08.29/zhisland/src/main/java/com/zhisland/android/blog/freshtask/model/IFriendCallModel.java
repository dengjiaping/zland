package com.zhisland.android.blog.freshtask.model;

import android.content.Context;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.List;

import rx.Observable;

/**
 * Created by arthur on 2016/6/21.
 */
public interface IFriendCallModel extends IMvpModel {

    /**
     * 获取召唤好友数据
     *
     * @return
     */
    Observable<ZHPageData<User>> getCallFriendList();

    /**
     * 召唤好友
     *
     * @param users
     * @return
     */
    Observable<Void> callFriend(List<User> users);

    /**
     * 召唤好友
     *
     * @param context
     * @param users
     * @param responseCallback
     */
    void callFriend(Context context, List<User> users, TaskCallback<Object> responseCallback);
}
