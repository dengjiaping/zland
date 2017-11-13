package com.zhisland.android.blog.contacts.controller;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.contacts.dto.Organization;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.common.view.MultilineTextView;
import com.zhisland.android.blog.common.view.RoundAngleImageView;
import com.zhisland.android.blog.common.view.SignUpLayout;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

public class CardViewHolder {

	private static final int TOTAL_BADGE_COUNT = 4;
	private static final int BADGE_INTERVAL = DensityUtil.dip2px(10);

	@InjectView(R.id.ivUcFigure)
	public RoundAngleImageView ivUcFigure;

	@InjectView(R.id.tvUcName)
	public TextView tvUcName;

	@InjectView(R.id.tvUcCompany)
	public TextView tvUcCompany;

	@InjectView(R.id.tvUcDes)
	public MultilineTextView tvUcDes;

	@InjectView(R.id.id_flowlayout)
	public TagFlowLayout flowLayout;

	@InjectView(R.id.flBottom)
	public FrameLayout flBottom;

	@InjectView(R.id.tvCommonFriend)
	public TextView tvCommonFriend;

	@InjectView(R.id.imgsSignUp)
	public SignUpLayout commonFriendView;

	// 徽章
	@InjectView(R.id.llBadgeImgs)
	public LinearLayout llBadgeImgs;

	@InjectView(R.id.llCardBottomContainer)
	public LinearLayout llCardBottomContainer;

	private Context context;

	private int cellWidth;

	public CardViewHolder(Context context, View v) {
		ButterKnife.inject(this, v);
		this.context = context;
		calculationBadgeImgs();
	}

	// 计算徽章的宽高
	private void calculationBadgeImgs() {
		int badgeWidth = DensityUtil.getWidth()
				- context.getResources().getDimensionPixelOffset(
						R.dimen.card_horizontal_margin) * 4;
		cellWidth = (badgeWidth - (TOTAL_BADGE_COUNT - 1) * BADGE_INTERVAL)
				/ TOTAL_BADGE_COUNT;
	}

	public void fill(User user) {
		ImageWorkFactory.getFetcher().loadImage(user.figure, ivUcFigure,
				R.drawable.img_campaign_default);
		tvUcName.setText(user.getNameWithTypeGoldFire(context,false));
		tvUcCompany.setText(user.userCompany + " " + user.userPosition);
		tvUcDes.setText(user.introduce);

		if (user.orgs != null && user.orgs.size() > 0) {
			llBadgeImgs.setVisibility(View.VISIBLE);
			llCardBottomContainer.setVisibility(View.GONE);
			addBadges(user);
		} else {
			llBadgeImgs.setVisibility(View.GONE);
			llCardBottomContainer.setVisibility(View.VISIBLE);
			addTags(user);
			List<User> commFriends = user.commFriends;
			if (commFriends != null) {
				if (commFriends == null || commFriends.size() == 0) {
					flBottom.getChildAt(0).setVisibility(View.VISIBLE);
					flBottom.getChildAt(1).setVisibility(View.INVISIBLE);
				} else {
					flBottom.getChildAt(1).setVisibility(View.VISIBLE);
					flBottom.getChildAt(0).setVisibility(View.GONE);
					tvCommonFriend.setText("共同好友" + commFriends.size() + "位");
					commonFriendView.setSignUpUsers(commFriends);
				}
			} else {
				flBottom.getChildAt(0).setVisibility(View.VISIBLE);
				flBottom.getChildAt(1).setVisibility(View.INVISIBLE);
			}
		}
	}

	// 添加徽章view
	private void addBadges(User user) {
		llBadgeImgs.removeAllViews();
		ArrayList<Organization> orgs = user.orgs;
		ArrayList<String> badgeImgs = new ArrayList<String>();
		for (Organization org : orgs) {
			if (!StringUtil.isNullOrEmpty(org.badgeLevelImg)) {
				badgeImgs.add(org.badgeLevelImg);
			}
		}
		int size = badgeImgs.size() > TOTAL_BADGE_COUNT ? TOTAL_BADGE_COUNT
				: badgeImgs.size();

		for (int i = 0; i < size; i++) {
			ImageView ivBadge = new ImageView(context);
			LinearLayout.LayoutParams params = new LayoutParams(cellWidth,
					cellWidth);
			if (i != size - 1) {
				params.rightMargin = BADGE_INTERVAL;
			}
			ImageWorkFactory.getFetcher().loadImage(badgeImgs.get(i), ivBadge,
					R.drawable.img_badge_default);
			llBadgeImgs.addView(ivBadge, params);
		}
	}

	private void addTags(User user) {
		List<String> tags = new ArrayList<String>();
		ZHDicItem industry = user.industry;
		if (industry != null) {
			tags.add(industry.name);
		}

		List<String> specialtiesTags = user.getSpecialtiesTags();
		for (String specTag : specialtiesTags) {
			if (tags.size() >= 3) {
				break;
			}
			tags.add(specTag);
		}
		List<String> interestTags = user.getInterestTags();
		for (String interTag : interestTags) {
			if (tags.size() >= 3) {
				break;
			}
			tags.add(interTag);
		}

		flowLayout.setMaxSelectCount(0);
		TagAdapter<String> adapter = new TagAdapter<String>(tags) {
			@SuppressLint("InflateParams")
			@Override
			public View getView(
					com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
					int position, String tag) {
				TextView textView = (TextView) LayoutInflater.from(context)
						.inflate(R.layout.tag_text, null);
				int tagHeight = context.getResources().getDimensionPixelSize(
						R.dimen.card_tag_height);
				MarginLayoutParams params = new MarginLayoutParams(
						MarginLayoutParams.WRAP_CONTENT, tagHeight);
				params.rightMargin = DensityUtil.dip2px(10);
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundResource(R.drawable.img_label_light_gold_selected);
				textView.setLayoutParams(params);
				textView.setText(tag);
				textView.setClickable(false);
				textView.setPadding(DensityUtil.dip2px(15), 0,
						DensityUtil.dip2px(15), 0);
				return textView;
			}
		};
		flowLayout.setAdapter(adapter);
	}
}
