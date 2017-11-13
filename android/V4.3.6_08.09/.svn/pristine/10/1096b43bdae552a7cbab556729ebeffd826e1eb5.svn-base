package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.ActProfileIntroduction;
import com.zhisland.lib.util.DensityUtil;

/**
 * 个人简介 block
 */
public class UserIntroduceBlock extends ProfileBaseCommonBlock<String> {

	public UserIntroduceBlock(Activity context, UserDetail userDetail, SimpleBlock<String> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("想让大家记住，一个怎样的自己？");
		tvEmptyButton.setText("添加个人介绍");
		
		LinearLayout.LayoutParams llContainerParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llContainerParams.topMargin = DensityUtil.dip2px(15);
		llBlockContent.setLayoutParams(llContainerParams);
	}

	@Override
	protected void loadData(List<String> datas) {
		TextView textView = new TextView(context);
		textView.setTextColor(context.getResources().getColor(
				R.color.color_f1));
		DensityUtil.setTextSize(textView, R.dimen.txt_16);
		textView.setText(datas.get(0));

		llBlockContent.addView(textView);
	}

	@Override
	protected void onIvRightClick() {
		ActProfileIntroduction.invoke(context);
	}

	@Override
	protected void ontvRightClick() {

	}

	@Override
	protected void onTvEmptyButtonClick() {
		ActProfileIntroduction.invoke(context);
	}

}
