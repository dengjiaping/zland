package com.zhisland.android.blog.invitation.model;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.List;

import rx.Observable;

/**
 * Created by lipengju on 16/8/10.
 */
public interface IReqApproveFriendsModel extends IMvpModel {

    void setData(List<InviteUser> users);

    Observable<List<InviteUser>> loadData();

    //忽略请求
    Observable<Object> ignoreHaikeRequest(final User user);

    //批准之前的检查
    Observable<Integer> preConfirm(long uid);
}
