package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.resource.FragDemandEdit;
import com.zhisland.android.blog.profile.controller.resource.FragUserResource;
import com.zhisland.android.blog.profile.controller.resource.UserResourceCell;

/**
 * 需求 block
 */
public class UserDemandBlock extends ProfileBaseCommonBlock<Resource> {

	public UserDemandBlock(Activity context, UserDetail userDetail,
			SimpleBlock<Resource> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("企业长期需求或短期急需");
		tvEmptyButton.setText("添加我的需求");
	}

	@Override
	protected void loadData(List<Resource> datas) {
		for (Resource resource : datas) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_resource, null);
			UserResourceCell cell = new UserResourceCell(view, context);
			cell.fill(resource, false);
			llBlockContent.addView(view);
		}
	}

	@Override
	protected void onIvRightClick() {
		FragUserResource.invoke(context, getUserDetail());
	}

	@Override
	protected void ontvRightClick() {
	}

	@Override
	protected void onTvEmptyButtonClick() {
		FragDemandEdit.invoke(context, null);
	}

}
