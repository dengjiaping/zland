package com.zhisland.android.blog.contacts.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * 主动邀请 数据结构
 */
public class InviteStructure extends OrmDto {

    // 总数
    @SerializedName("total")
    private Integer total;

    // 使用总数
    @SerializedName("useTotal")
    private Integer useTotal;

    // 求邀请用户
    @SerializedName("users")
    private List<User> users;

    // 邀请用户
    @SerializedName("invites")
    private List<InviteUser> invites;

    // 总数
    public int getTotalCount() {
        if (total != null) {
            return total;
        }
        return 0;
    }

    // 使用总数
    public int getUsedCount() {
        if (useTotal != null) {
            return useTotal;
        }
        return 0;
    }

    // 求邀请用户
    public List<User> getRequestUsers() {
        return users;
    }

    // 邀请用户
    public List<InviteUser> getInvites() {
        return invites;
    }
}
