package com.zhisland.android.blog.invitation.model;

import com.zhisland.android.blog.invitation.model.impl.InvitationConfirmModel;
import com.zhisland.android.blog.invitation.model.impl.InvitationDealedModel;
import com.zhisland.android.blog.invitation.model.impl.InvitationRequestModel;

/**
 * model 工厂类
 * <p>
 * Created by arthur on 2016/8/9.
 */
public class InvitationModelFactory {

    /**
     * 创建已批准列表的model
     *
     * @return
     */
    public static IInvitationDealedModel createInvitationDealedModel() {
        return new InvitationDealedModel();
    }

    //创建主动邀请请求和头部的model
    public static IInvitationRequestModel createInvitationRequestModel() {
        return new InvitationRequestModel();
    }

    //创建可批准的列表模型
    public static IInvitationConfirmModel createInvitationConfirmModel() {
        return new InvitationConfirmModel();
    }
}
