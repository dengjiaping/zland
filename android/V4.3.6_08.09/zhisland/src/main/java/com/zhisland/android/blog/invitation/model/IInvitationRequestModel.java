package com.zhisland.android.blog.invitation.model;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * Created by arthur on 2016/8/10.
 */
public interface IInvitationRequestModel extends IMvpModel {

    //获取邀请的数据
    Observable<InvitationData> getInvitationData();

    //忽略请求
    Observable<Object> ignoreHaikeRequest(final User user);

    Observable<Object> preConfirm(long uid);
}
