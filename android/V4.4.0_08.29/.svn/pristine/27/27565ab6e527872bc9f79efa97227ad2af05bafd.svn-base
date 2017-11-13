package com.zhisland.android.blog.profile.controller.position;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 当前任职
 */
public class FragOrgDetail extends FragBase {

	public static final String INK_COMPANY_ORG = "ink_company_org";

	@InjectView(R.id.ivOrgLogo)
	ImageView ivOrgLogo;

	@InjectView(R.id.tvOrgName)
	TextView tvOrgName;

	@InjectView(R.id.tvOrgPosition)
	TextView tvOrgPosition;

	public static void invoke(Context context, Company company) {
		Intent intent = CommonFragActivity.createIntent(context,
				createFragParams(context));
		intent.putExtra(INK_COMPANY_ORG, company);
		context.startActivity(intent);
	}

	private static CommonFragParams createFragParams(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragOrgDetail.class;
		param.swipeBackEnable = false;
		param.title = "当前任职";
		param.enableBack = true;
		return param;
	}

	/**
	 * 空构造函数得保留
	 */
	public FragOrgDetail() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.user_position_org, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Company company = (Company) getActivity().getIntent()
				.getSerializableExtra(INK_COMPANY_ORG);
		updateView(company);
	}

	private void updateView(Company company) {
		ImageWorkFactory.getCircleFetcher().loadImage(company.logo, ivOrgLogo,
				R.drawable.img_zhisland_logo_empty);
		tvOrgName.setText(company.name);
		tvOrgPosition.setText(company.position);
	}

	@Override
	public String getPageName() {
		return "ProfileMyCompanyOrganization";
	}

}
