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
public class ActSignUpEvents extends FragBaseActivity {

	FragSignUpEvents fragSignUpEvents;

	public static void invoke(Context context) {
		Intent intent = new Intent(context, ActSignUpEvents.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		getTitleBar().setTitle("报名的活动");
		getTitleBar().addBackButton();
		fragSignUpEvents = new FragSignUpEvents();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragSignUpEvents);
		ft.commit();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}

}
