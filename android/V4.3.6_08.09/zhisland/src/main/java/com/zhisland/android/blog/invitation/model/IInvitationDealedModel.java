package com.zhisland.android.blog.invitation.model;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.bean.InvitationData;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.List;

import rx.Observable;

/**
 * Created by arthur on 2016/8/9.
 */
public interface IInvitationDealedModel extends IMvpModel {


    /**
     * 加载数据
     *
     * @return
     */
    Observable<InvitationData> loadData();
}
