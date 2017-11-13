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

    /**
     * CustomState 的state 为6时，为访客用户。
     */
    public static final int CustomState_State_FANGKE = 6;

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

    //是否通过职位白名单 1为通过，0为未通过
    private Integer whiteOk;

    public boolean isWhiteOk(){
        if(whiteOk != null && whiteOk == 1){
            return true;
        }
        return false;
    }

    public void setWhiteOk(Integer whiteOk){
        this.whiteOk = whiteOk;
    }

}
