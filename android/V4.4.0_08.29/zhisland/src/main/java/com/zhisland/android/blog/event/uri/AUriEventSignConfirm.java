package com.zhisland.android.blog.event.uri;

import android.content.Context;
import android.net.Uri;

import com.zhisland.android.blog.common.uri.AUriBase;
import com.zhisland.android.blog.event.eb.EBEvent;

import de.greenrobot.event.EventBus;

/**
 * 活动报名确认页
 */
public class AUriEventSignConfirm extends AUriBase {

	@Override
	public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
        EventBus.getDefault().post(new EBEvent(EBEvent.TYPE_SIGN_CONFIRM, null));
	}

}
