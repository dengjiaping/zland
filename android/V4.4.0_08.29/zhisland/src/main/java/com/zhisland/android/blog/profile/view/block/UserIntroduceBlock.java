package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.controller.ActProfileIntroduction;
import com.zhisland.android.blog.profile.controller.introduce.UserIntroduceCell;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;

import java.util.List;

/**
 * 个人简介 block
 */
public class UserIntroduceBlock extends ProfileBaseCommonBlock<String> {

	public UserIntroduceBlock(Activity context, UserDetail userDetail, SimpleBlock<String> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		ivEmptyIcon.setImageResource(R.drawable.img_profile_introduce);
		tvEmptyDesc.setText("用简明而性感的文字向大家介绍自己");
		tvEmptyButton.setText("添加个人介绍");
	}

	@Override
	protected void loadData(List<String> datas) {
		if (datas != null && datas.size() > 0) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_block_introduce, null);
			UserIntroduceCell cell = new UserIntroduceCell(view, context);
			cell.fill(datas.get(0));
			llBlockContent.addView(view);
		}
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
