package com.zhisland.android.blog.event.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.event.controller.ActEventDetail;
import com.zhisland.android.blog.event.controller.FragEvent;

/**
 * 用来控制event的uri
 * 
 * @author arthur
 *
 */
public class AUriEvent extends AUriBase {

	@Override
	public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        String idString = uri.getLastPathSegment();
        try {
            // 活动详情
            long id = Long.parseLong(idString);
            ActEventDetail.invoke(context, id, false);
        } catch (Exception e) {
            // 活动列表
            HomeUtil.NavToHome(context);
            FragEvent.Invoke(context);
        }
	}

}