package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
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
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.contact.FragUserAssistant;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 助理block
 */
public class UserAssistantBlock extends ProfileBaseCustomBlock<UserAssistant> {

	@InjectView(R.id.tvTitle)
	TextView tvTitle;

	@InjectView(R.id.tvName)
	TextView tvName;

	@InjectView(R.id.tvWhoCanSee)
	TextView tvWhoCanSee;

	@InjectView(R.id.ivPhone)
	ImageView ivPhone;

	public UserAssistantBlock(Activity context, UserDetail userDetail,
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
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvAdd = (TextView) view.findViewById(R.id.tvAdd);
		tvTitle.setText(getBlockTitle());
		tvAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragUserAssistant.invoke(getContext(), getBlock());
			}
		});
		return view;
	}

	@Override
	protected void loadData(List<UserAssistant> datas) {
		UserAssistant userAssistant = getUserAssistant();
		if (userAssistant == null) {
			return;
		}
		tvTitle.setText(getBlockTitle());
		if (isUserSelf()) {
			// 可见范围如果为空，默认为岛邻可见
			if (userAssistant.visibleRange == null) {
				userAssistant.visibleRange = UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_FRIEND;
			}
			switch (userAssistant.visibleRange) {
			case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL:
				tvWhoCanSee
						.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_ALL);
				break;
			case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND:
				tvWhoCanSee
						.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_FRIEND);
				break;
			case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_NO:
				tvWhoCanSee
						.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_NO);
				break;
			}
		} else {
			tvWhoCanSee.setVisibility(View.GONE);
		}
		if (userAssistant.assistant != null) {
			tvName.setText(userAssistant.assistant.name == null ? "-"
					: userAssistant.assistant.name);
		} else {
			tvName.setText("-");
		}
	}

	@OnClick(R.id.llItem)
	void onContactInfoEditClick() {
		if (isUserSelf()) {
			FragUserAssistant.invoke(getContext(), getBlock());
		}
	}

	@OnClick(R.id.ivPhone)
	void onPhoneClick() {
		UserAssistant userAssistant = getUserAssistant();
		if (userAssistant == null || userAssistant.assistant == null
				|| StringUtil.isNullOrEmpty(userAssistant.assistant.userMobile)) {
			return;
		}
		IntentUtil.dialTo(getContext(), (userAssistant.assistant.countryCodeShow == null ? ""
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
