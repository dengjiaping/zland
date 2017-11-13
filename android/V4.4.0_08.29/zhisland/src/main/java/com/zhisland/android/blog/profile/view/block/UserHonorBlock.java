package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.honor.FragHonorEdit;
import com.zhisland.android.blog.profile.controller.honor.FragUserHonor;
import com.zhisland.android.blog.profile.controller.honor.UserHonorCell;

/**
 * 荣誉 block
 */
public class UserHonorBlock extends ProfileBaseCommonBlock<Honor> {

	public UserHonorBlock(Activity context, UserDetail userDetail, SimpleBlock<Honor> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		ivEmptyIcon.setImageResource(R.drawable.img_profile_cup);
		tvEmptyDesc.setText("这面墙都是给你放奖杯和奖状的");
		tvEmptyButton.setText("添加个人荣誉");
	}

	@Override
	protected void loadData(List<Honor> datas) {
		for (Honor honor : datas) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_block_honor, null);
			UserHonorCell cell = new UserHonorCell(view, context);
			cell.fill(honor, false);

			llBlockContent.addView(view);
		}
	}

	@Override
	protected void onIvRightClick() {
		FragUserHonor.invoke(context, getBlock());
	}

	@Override
	protected void ontvRightClick() {
	}

	@Override
	protected void onTvEmptyButtonClick() {
		FragHonorEdit.invoke(context, null);
	}

}
