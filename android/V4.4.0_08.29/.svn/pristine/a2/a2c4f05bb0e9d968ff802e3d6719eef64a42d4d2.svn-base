package com.zhisland.android.blog.info.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.info.view.impl.ActInfoDetail;
import com.zhisland.android.blog.tabhome.eb.EBTabHome;

import de.greenrobot.event.EventBus;

/**
 * 用来控制资讯的uri
 */
public class AUriInfo extends AUriBase {

    @Override
    public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        String idString = uri.getLastPathSegment();
        try {
            // 资讯详情
            long id = Long.parseLong(idString);
            ActInfoDetail.invoke(context, id);
        } catch (Exception e) {
            // 资讯列表
            HomeUtil.NavToHome(context);
            EventBus.getDefault().post(new EBTabHome(EBTabHome.TYPE_SEL_TAB_CATEGORY, TabHome.TAB_ID_INFO));
        }
    }

}
