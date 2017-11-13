package com.zhisland.android.blog.freshtask.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;

/**
 * 任务列表
 */
public class AUriTasksList extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        //跳转任务列表
        BusFreshTask.Bus().post(new EventTitleClick());
    }

}

