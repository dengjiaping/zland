package com.zhisland.android.blog.invitation.eb;

/**
 * Created by Mr.Tui on 2016/8/10.
 */
public class EBInvite {

    public static final int ACTION_ALLOW_INVITE_REQUEST = 1;

    public long userId;

    public int action;

    public EBInvite(long userId, int action) {
        this.userId = userId;
        this.action = action;
    }
}
