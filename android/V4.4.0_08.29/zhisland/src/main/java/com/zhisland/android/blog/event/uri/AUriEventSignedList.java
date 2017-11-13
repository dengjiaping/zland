package com.zhisland.android.blog.event.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.event.controller.ActSignUpEvents;

/**
 *  我报名的活动列表 uri
 */
public class AUriEventSignedList extends AUriBase {

	@Override
	public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        ActSignUpEvents.invoke(context);
    }

}
