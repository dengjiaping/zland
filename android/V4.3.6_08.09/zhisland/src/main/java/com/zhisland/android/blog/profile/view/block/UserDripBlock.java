package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.drip.ActUserDripList;
import com.zhisland.android.blog.profile.controller.drip.UserDripCellBase;
import com.zhisland.android.blog.profile.controller.drip.UserDripProfileCell;
import com.zhisland.android.blog.profile.controller.drip.UserDripProfileCell.OnContentClickListener;
import com.zhisland.lib.util.StringUtil;

/**
 * 用户点滴 block
 */
public class UserDripBlock extends ProfileBaseCommonBlock<CustomDict> {

	public UserDripBlock(Activity context, UserDetail userDetail,
			SimpleBlock<CustomDict> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("您暂时还没有点滴");
		tvEmptyButton.setText("添加我的点滴");
	}

	@Override
	protected void loadData(List<CustomDict> datas) {
		for (CustomDict data : datas) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_drip, null);
			UserDripCellBase cell = new UserDripProfileCell(view, context);
			((UserDripProfileCell) cell)
					.setOnContentClickListener(new OnContentClickListener() {

						@Override
						public void onClick(View v, CustomDict dict) {
//							if (StringUtil.isNullOrEmpty(dict.value)) {
//								ActUserDripList
//										.invoke(context, getBlockDatas());
//							}
						}
					});
			cell.fill(data);
			llBlockContent.addView(view);
		}
	}

	@Override
	protected void onIvRightClick() {
		ActUserDripList.invoke(context, getBlockDatas());
	}

	@Override
	protected void ontvRightClick() {
	}

	@Override
	protected void onTvEmptyButtonClick() {
		ActUserDripList.invoke(context, getBlockDatas());
	}
}
