package com.zhisland.android.blog.contacts.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 邀请人
 */
public class InviteUser extends OrmDto {

    //通讯录好友
    public static final int INVITE_FRIEND_OF_CONTACT = 60;

    // 用户信息
    @SerializedName("user")
    public User user;

    // 状态
    @SerializedName("state")
    public CustomState state;

    @SerializedName("inviteRegister")
    public int inviteRegister;

    @SerializedName("requestExplanation")
    public String requestExplanation;

    @SerializedName("requestTime")
    public String requestTime;

}
