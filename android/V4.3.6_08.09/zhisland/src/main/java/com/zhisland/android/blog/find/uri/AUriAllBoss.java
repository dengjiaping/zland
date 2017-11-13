package com.zhisland.android.blog.find.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.find.controller.ActAllBoss;

/**
 * 全部企业家
 */
public class AUriAllBoss extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        ActAllBoss.invoke(context);
    }

}
