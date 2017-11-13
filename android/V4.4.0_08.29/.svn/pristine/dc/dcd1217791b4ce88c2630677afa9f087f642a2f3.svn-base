package com.zhisland.android.blog.common.uri;

import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;

import android.content.Context;
import android.net.Uri;

/**
 * 用来控制User的uri
 * 
 * @author arthur
 *
 */
public class AUriUser extends AUriBase {

	@Override
	public void viewRes(Context context, Uri uri, String arg1, Object arg2) {
		String idString = uri.getLastPathSegment();
		long id = Long.parseLong(idString);
		ActProfileDetail.invoke(context, id);
	}

}
