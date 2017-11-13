package com.zhisland.android.blog.contacts.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.contacts.controller.ActCard;

/**
 * 请求加您好友
 */
public class AUriCardAddFriend extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        ActCard.invoke(context, ActCard.FROM_ADD_FRIEND);
    }

}