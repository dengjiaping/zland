package com.zhisland.android.blog.common.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.tabhome.eb.EBTabHome;

import de.greenrobot.event.EventBus;

/**
 * 跳转主页面uri
 */
public class AUriHome extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        HomeUtil.NavToHome(context);
        String idString = uri.getLastPathSegment();
        try {
            int tabPos = Integer.parseInt(idString);
            EventBus.getDefault().post(new EBTabHome(EBTabHome.TYPE_SEL_TAB_POSITION, tabPos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
