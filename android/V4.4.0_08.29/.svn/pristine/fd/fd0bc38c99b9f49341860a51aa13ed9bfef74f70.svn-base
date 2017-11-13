package com.zhisland.android.blog.invitation.model;

import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * Created by arthur on 2016/8/10.
 */
public interface IInvitationConfirmModel extends IMvpModel {

    //可认证的好友列表
    Observable<ZHPageData<InviteUser>> getConfirmableUsers(String contactData, String nextId);

    //是否授权通讯录
    boolean isContactAllowed();

    //获取上次上传后有变更的
    PhoneContactUtil.ContactResult<String> getChangeContacts();

    //批准之前的检查
    Observable<Integer> preConfirm(long uid);


}
