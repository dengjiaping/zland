package com.zhisland.android.blog.setting.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 关于我们
 */
public class FragAboutUs extends FragBase {

	@InjectView(R.id.tvVersionName)
	TextView tvVersionName;

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragAboutUs.class;
		param.title = "关于我们";
		param.enableBack = true;
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_about_us, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		tvVersionName.setText("V " + AppUtil.Instance().getVersionName());
	}

	protected String getPageName() {
		return "SettingAboutUs";
	}
}
