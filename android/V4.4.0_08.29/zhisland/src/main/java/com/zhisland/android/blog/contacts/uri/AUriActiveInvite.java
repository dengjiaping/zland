package com.zhisland.android.blog.contacts.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.invitation.view.impl.FragHaikeConfirm;

/**
 * 主动邀请界面
 */
public class AUriActiveInvite extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        FragHaikeConfirm.invoke(context);
    }

}