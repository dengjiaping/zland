package com.zhisland.android.blog.freshtask.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;

import de.greenrobot.event.EventBus;

/**
 * 任务卡片
 */
public class AUriTasksCard extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        try {
            String idString = uri.getLastPathSegment();
            int position = Integer.parseInt(idString);
            EventFreshTask eb = new EventFreshTask(EventFreshTask.TYPE_SHOW_CARD, position);
            EventBus.getDefault().post(eb);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

