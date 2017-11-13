package com.zhisland.android.blog.contacts.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.view.RedDotView;
import com.zhisland.lib.component.act.TitleType;

public class ActCard extends FragBaseActivity {
	/**
	 * 0： 开机卡片 1：推荐人脉 2: 想认识我的人
	 */
	public static final String INK_FROM = "ink_from";
	public static final int FROM_BOOT_CARD = 0;
	public static final int FROM_RECOMMEND = 1;
	public static final int FROM_ADD_FRIEND = 2;

	private int from;

	private FragCard fragCard;

	@InjectView(R.id.llActCardRoot)
	public LinearLayout llActCardRoot;

	@InjectView(R.id.rlCardTitle)
	public RelativeLayout rlCardTitle;

	@InjectView(R.id.ivCardBack)
	public ImageView ivCardBack;

	@InjectView(R.id.tvCardRight)
	public TextView tvRight;

	@InjectView(R.id.tvCardTitle)
	public TextView tvCardTitle;

	@InjectView(R.id.tvCardCount)
	public RedDotView tvCardCount;

	@InjectView(R.id.vLine)
	public View vLine;

	public static void invoke(Context context, int from) {
		Intent intent = new Intent(context, ActCard.class);
		Bundle bundle = new Bundle();
		bundle.putInt(INK_FROM, from);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);
		setContentView(R.layout.act_card);
		ButterKnife.inject(this);

		from = getIntent().getExtras().getInt(INK_FROM);
		showTitle();

		fragCard = new FragCard();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.flContainer, fragCard);
		ft.commit();
	}

	private void showTitle() {
		switch (from) {
		case FROM_BOOT_CARD:
			tvCardTitle.setText("结识新朋友");
			vLine.setVisibility(View.GONE);
			llActCardRoot.setBackgroundResource(R.drawable.bg_card);
			rlCardTitle.setBackgroundColor(Color.TRANSPARENT);
			ivCardBack.setVisibility(View.GONE);
			break;
		case FROM_RECOMMEND:
			tvCardTitle.setText("推荐人脉");
			llActCardRoot.setBackgroundDrawable(null);
			rlCardTitle.setBackgroundColor(getResources().getColor(
					R.color.white));
			tvRight.setVisibility(View.GONE);
			break;
		case FROM_ADD_FRIEND:
			tvCardTitle.setText("好友请求");
			llActCardRoot.setBackgroundDrawable(null);
			rlCardTitle.setBackgroundColor(getResources().getColor(
					R.color.white));
			tvRight.setVisibility(View.GONE);
			break;
		}
	}

	public void showTitleCount(int count) {
		if (count == 0) {
			tvCardCount.setVisibility(View.GONE);
		} else {
			tvCardCount.setVisibility(View.VISIBLE);
			tvCardCount.setText(String.valueOf(count));
		}
	}

	@OnClick(R.id.ivCardBack)
	public void onClickBack() {
		finish();
	}

	@OnClick(R.id.tvCardRight)
	public void onClickRight() {
		this.finish();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (fragCard != null) {
			fragCard.onActivityResult(requestCode, resultCode, data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
