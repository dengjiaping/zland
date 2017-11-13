package com.zhisland.android.blog.im.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.im.controller.ActChat;

/**
 * 单聊
 */
public class AUriSingleChat extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        try {
            String idString = uri.getLastPathSegment();
            long uid = Long.parseLong(idString);
            ActChat.invoke(context, uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}