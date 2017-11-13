package com.zhisland.android.blog.contacts.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.invitation.view.impl.FragInvitationDealedList;

/**
 * 主动邀请已批准列表页
 */
public class AUriInviteDealed extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        FragInvitationDealedList.invoke(context, null);
    }

}