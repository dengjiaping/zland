package com.zhisland.android.blog.invitation.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * Created by arthur on 2016/8/10.
 */
public class InvitationData extends OrmDto {

    // 总数
    @SerializedName("total")
    public Integer total;

    // 使用总数
    @SerializedName("useTotal")
    public Integer useTotal;

    //认证请求数据
    @SerializedName("users")
    public List<InviteUser> users;

}
