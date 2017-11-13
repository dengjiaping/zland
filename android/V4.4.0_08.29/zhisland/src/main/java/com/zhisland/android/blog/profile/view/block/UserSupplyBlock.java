package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.resource.FragSupplyEdit;
import com.zhisland.android.blog.profile.controller.resource.FragUserResource;
import com.zhisland.android.blog.profile.controller.resource.UserResourceCell;

/**
 * 资源 block
 */
public class UserSupplyBlock extends ProfileBaseCommonBlock<Resource> {

	public UserSupplyBlock(Activity context, UserDetail userDetail,
			SimpleBlock<Resource> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("愿意分享给岛上朋友的资源");
		tvEmptyButton.setText("添加我的资源");
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
		FragSupplyEdit.invoke(context, null);
	}

}
