package com.zhisland.android.blog.find.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.find.controller.ActAllRes;

/**
 * 全部资源需求
 */
public class AUriResources extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        String rType = null;
        String keyword = null;
        try {
            rType = uri.getQueryParameter("rType");
            keyword = uri.getQueryParameter("keyword");
        } catch (Exception e) {

        }
        ActAllRes.invoke(context, rType, keyword);
    }

}
