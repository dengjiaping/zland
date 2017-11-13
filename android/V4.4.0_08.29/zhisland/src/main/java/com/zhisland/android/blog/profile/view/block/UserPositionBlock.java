package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.controller.position.ActUserCompany;
import com.zhisland.android.blog.profile.controller.position.FragOrgDetail;
import com.zhisland.android.blog.profile.controller.position.FragUserCompanySimpleEdit;
import com.zhisland.android.blog.profile.controller.position.UserCompanyCell;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * 任职 block
 */
public class UserPositionBlock extends ProfileBaseCommonBlock<Company> {

	public UserPositionBlock(Activity context, UserDetail userDetail,
			SimpleBlock<Company> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("您暂时还没有任职");
		tvEmptyButton.setText("添加我的任职");
	}

	@Override
	protected void loadData(List<Company> datas) {
		ArrayList<CompanyType> types = Dict.getInstance().getCompanyType();
		for (Company data : datas) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_block_user_position, null);
			UserCompanyCell cell = new UserCompanyCell(context, view);
			cell.fill(data, getBlockUser(), true, types);
			llBlockContent.addView(view);
		}
	}

	@Override
	protected void onIvRightClick() {
		User user = getBlockUser();
		//预注册用户直接跳到编辑页
		if (user != null && user.isYuZhuCe()) {
			List<Company> datas = getBlockDatas();
			if (datas != null && datas.size() > 0) {
				if (datas.get(0).type != null && datas.get(0).type.equals(CompanyType.KEY_ZHISLAND_ORG)) {
					FragOrgDetail.invoke(context, datas.get(0));
				} else {
					FragUserCompanySimpleEdit.invoke(context, datas.get(0));
				}
				return;
			}
		}
		ActUserCompany.invoke(context,
				(ArrayList<Company>) getBlockDatas(), getBlockUser());
	}

	@Override
	protected void ontvRightClick() {
	}

	@Override
	protected void onTvEmptyButtonClick() {
		ActUserCompany.invoke(context,
				(ArrayList<Company>) getBlockDatas(), getBlockUser());
	}

}
