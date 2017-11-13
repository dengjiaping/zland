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
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.contact.FragUserContactInfo;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 基本信息block
 */
public class UserContactBlock extends ProfileBaseCustomBlock<UserContactInfo> {

	@InjectView(R.id.ivAvatar)
	AvatarView ivAvatar;

	@InjectView(R.id.tvName)
	TextView tvName;

	@InjectView(R.id.ivPencil)
	ImageView ivPencil;

	@InjectView(R.id.tvWhoCanSeeSelf)
	TextView tvWhoCanSeeSelf;

	@InjectView(R.id.tvWhoCanSeeOther)
	TextView tvWhoCanSeeOther;

	@InjectView(R.id.tvMail)
	TextView tvMail;

	@InjectView(R.id.tvPhone)
	TextView tvPhone;

	@InjectView(R.id.tvBlockTitle)
	TextView tvBlockTitle;

	public UserContactBlock(Activity context, UserDetail userDetail,
			SimpleBlock<UserContactInfo> block) {
		super(context, userDetail, block);
	}

	@Override
	protected View setCustomContentView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_user_contact_info, null);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	protected View setCustomEmptyView() {
		TextView textView = new TextView(context);
		textView.setText("Content view");
		textView.setBackgroundColor(context.getResources().getColor(
				R.color.white));
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showShort("click empty view");
			}
		});
		return textView;
	}

	@Override
	protected void loadData(List<UserContactInfo> datas) {
		UserContactInfo userContactInfo = getUserContactInfo();
		if (userContactInfo == null) {
			return;
		}
		if (isUserSelf()) {
			ivPencil.setVisibility(View.VISIBLE);
			tvWhoCanSeeSelf.setVisibility(View.VISIBLE);
			tvWhoCanSeeOther.setVisibility(View.GONE);
		} else {
			ivPencil.setVisibility(View.GONE);
			tvWhoCanSeeSelf.setVisibility(View.GONE);
			tvWhoCanSeeOther.setVisibility(View.VISIBLE);
		}
		tvBlockTitle.setText(getBlockTitle());
		tvName.setText(userContactInfo.userName == null ? ""
				: userContactInfo.userName);

		// 如果联系方式可见范围为空，默认设置为岛邻可见
		if (userContactInfo.visibleRange == null) {
			userContactInfo.visibleRange = UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND;
		}

		switch (userContactInfo.visibleRange) {
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL:
			tvWhoCanSeeSelf
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_ALL);
			tvWhoCanSeeOther
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_ALL);
			break;
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND:
			tvWhoCanSeeSelf
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_FRIEND);
			tvWhoCanSeeOther
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_FRIEND);
			break;
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_NO:
			tvWhoCanSeeSelf
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_NO);
			tvWhoCanSeeOther
					.setText(UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_NO);
			break;
		}

		if (StringUtil.isNullOrEmpty(userContactInfo.email)) {
			tvMail.setText("-");
		} else {
			tvMail.setText(userContactInfo.email);
		}

		if (StringUtil.isNullOrEmpty(userContactInfo.mobile)) {
			tvPhone.setText("-");
		} else {
			if (StringUtil.isNullOrEmpty(userContactInfo.countryCode)) {
				tvPhone.setText(userContactInfo.mobile);
			} else {
				tvPhone.setText(userContactInfo.countryCode + "-"
						+ userContactInfo.mobile);
			}
		}

        ivAvatar.fill(userContactInfo.userAvatar, false);
	}

	@OnClick(R.id.ivPencil)
	void onContactInfoEditClick() {
		FragUserContactInfo.invoke(getContext(), getBlock());
	}

	@OnClick(R.id.tvPhone)
	void onPhoneClick() {
		UserContactInfo userContactInfo = getUserContactInfo();
		if (isUserSelf() || userContactInfo == null
				|| StringUtil.isNullOrEmpty(userContactInfo.mobile)) {
			return;
		}
		IntentUtil.dialTo(getContext(),(userContactInfo.countryCode == null ? ""
				: userContactInfo.countryCode) + userContactInfo.mobile);
	}

	@OnClick(R.id.tvMail)
	void onMailClick() {
		UserContactInfo userContactInfo = getUserContactInfo();
		if (isUserSelf() || userContactInfo == null
				|| StringUtil.isNullOrEmpty(userContactInfo.email)) {
			return;
		}
		Intent intent = IntentUtil.intentToSendMail(userContactInfo.email);
		getContext().startActivity(intent);
	}

	private UserContactInfo getUserContactInfo() {
		List<UserContactInfo> datas = getBlockDatas();
		UserContactInfo userContactInfo = null;
		if (datas != null && datas.size() > 0) {
			userContactInfo = datas.get(0);
		}
		return userContactInfo;
	}
}
