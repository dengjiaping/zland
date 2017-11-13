package com.zhisland.android.blog.freshtask.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.view.impl.FragAddResource;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendAdd;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendComment;
import com.zhisland.android.blog.freshtask.view.impl.FragIntroduction;
import com.zhisland.android.blog.freshtask.view.impl.FragInviteRequest;

import de.greenrobot.event.EventBus;

/**
 * 新手任务 任务详情
 */
public class AUriFreshTaskDetail extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        try {
            String strType = uri.getLastPathSegment();
            int type = Integer.parseInt(strType);
            switch (type){
                case TaskType.CONTACT:
                    FragFriendAdd.invoke(context);
                    break;
                case TaskType.FIGURE:
                    break;
                case TaskType.INTRODUCTION:
                    FragIntroduction.invoke(context);
                    break;
                case TaskType.RESOURCE:
                    FragAddResource.invoke(context);
                    break;
                case TaskType.PRICE:
                    FragFriendComment.invoke(context);
                    break;
                case TaskType.UPGRADE_HAIKE:
                    FragInviteRequest.invoke(context, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

