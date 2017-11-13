package com.zhisland.android.blog.event.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.lib.component.act.TitleType;

/**
 * 活动详情页面
 * */
public class ActEventDetail extends FragBaseActivity {

	FragEventDetail fragEventDetail;

	public static final String KEY_INTENT_EVENTID = "key_intent_eventid";
	public static final String KEY_INTENT_SHOW_SHARE = "key_intent_show_share";

	/**
	 * 启动活动详情页面。
	 * 
	 * @param showShare 是否在启动时，显示分享对话框。
	 * */
	public static void invoke(Context context, long eventId, boolean showShare) {
		Intent intent = new Intent(context, ActEventDetail.class);
		intent.putExtra(KEY_INTENT_EVENTID, eventId);
		intent.putExtra(KEY_INTENT_SHOW_SHARE, showShare);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);
		fragEventDetail = new FragEventDetail();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragEventDetail);
		ft.commit();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

}
