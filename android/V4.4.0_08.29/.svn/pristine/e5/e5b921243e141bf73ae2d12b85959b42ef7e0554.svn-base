package com.zhisland.android.blog.profile.controller.position;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.lib.component.act.TitleType;

/**
 * 任职列表的Activity
 * @author zhangxiang
 *
 */
public class ActUserCompany extends FragBaseActivity {

	public static final String INK_USER_COMPANY = "ink_user_positions";
	public static final String INK_USER = "ink_user";
	
	FragUserCompany fragProfileJob;

	public static void invoke(Context context, ArrayList<Company> userPositionList, User user) {
		Intent intent = new Intent(context, ActUserCompany.class);
		intent.putExtra(INK_USER_COMPANY, userPositionList);
		intent.putExtra(INK_USER, user);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(true);
		getTitleBar().addBackButton();
		getTitleBar().setTitle("当前任职");
		fragProfileJob = new FragUserCompany();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragProfileJob);
		ft.commit();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}
}
