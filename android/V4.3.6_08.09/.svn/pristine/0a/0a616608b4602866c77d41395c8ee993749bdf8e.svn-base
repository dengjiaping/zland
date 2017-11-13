package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserAssistant;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 岛丁block
 */
public class UserDingBlock extends ProfileBaseCustomBlock<UserAssistant> {

	@InjectView(R.id.tvTitle)
	TextView tvTitle;

	@InjectView(R.id.tvName)
	TextView tvName;

	@InjectView(R.id.tvWhoCanSee)
	TextView tvWhoCanSee;

	@InjectView(R.id.ivPhone)
	ImageView ivPhone;

	public UserDingBlock(Activity context, UserDetail userDetail,
			SimpleBlock<UserAssistant> block) {
		super(context, userDetail, block);
	}

	@Override
	protected View setCustomContentView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_user_assistant, null);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	protected View setCustomEmptyView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.empty_user_assistant, null);
		TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
		TextView tvAdd = (TextView)view.findViewById(R.id.tvAdd);
		tvTitle.setText(getBlockTitle());
		tvAdd.setVisibility(View.GONE);
		return view;
	}

	@Override
	protected void loadData(List<UserAssistant> datas) {
		UserAssistant userAssistant = getUserAssistant();
		tvTitle.setText(getBlockTitle());
		tvWhoCanSee.setVisibility(View.GONE);
		if (userAssistant != null && userAssistant.assistant != null) {
			tvName.setText(userAssistant.assistant.name == null ? "-"
					: userAssistant.assistant.name);
		} else {
			tvName.setText("-");
		}
	}

	@OnClick(R.id.ivPhone)
	void onPhoneClick() {
		UserAssistant userAssistant = getUserAssistant();
		if (userAssistant == null || userAssistant.assistant == null
				|| StringUtil.isNullOrEmpty(userAssistant.assistant.userMobile)) {
			return;
		}
		IntentUtil.dialTo(getContext(),(userAssistant.assistant.countryCodeShow == null ? ""
				: userAssistant.assistant.countryCodeShow)
				+ userAssistant.assistant.userMobile);
	}

	private UserAssistant getUserAssistant() {
		List<UserAssistant> datas = getBlockDatas();
		UserAssistant userAssistant = null;
		if (datas != null && datas.size() > 0) {
			userAssistant = datas.get(0);
		}
		return userAssistant;
	}
}
