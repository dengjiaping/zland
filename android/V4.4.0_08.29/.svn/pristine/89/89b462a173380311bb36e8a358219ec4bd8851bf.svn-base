package com.zhisland.android.blog.common.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.AnimUtils;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.tab.TabBarOnCreateListener;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.ZHTabInfo;
import com.zhisland.lib.view.title.OnTitleClickListner;
import com.zhisland.lib.view.title.TitleBar;

public abstract class FragBaseTabPageActivity extends TabFragActivity implements
		TabBarOnCreateListener, OnTitleClickListner {

	private TitleBarProxy titleBar;
	private DefaultTitleBarClickListener defaultTitleBarListener;

	public TitleBarProxy getTitleBar() {
		return titleBar;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);

		tabBar.setBottomIndicator(false);
		// 初始化titlebar
		titleBar = new TitleBarProxy();
		defaultTitleBarListener = new DefaultTitleBarClickListener(this);
		titleBar.configTitle(this.getWindow().getDecorView(), titleType(), this);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		try {
			switch (tagId) {
			case TitleBarProxy.TAG_BACK:
				defaultTitleBarListener.onBack();
				break;
			case TitleBar.TAG_TITLE_TXT: {
				defaultTitleBarListener.onTitle();
				break;
			}
			default:
				break;
			}
		} catch (final Throwable e) {
			MLog.e("FragBaseActivity", "exception happened", e);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);

		ComponentName comClass = intent.getComponent();
		if (comClass == null)
			return;// not zhisland's component or not an activty

		String className = comClass.getClassName();
		if (className.equals(CommonFragActivity.class.getName())) {
			className = CommonFragActivity.INCOME_FRAG_NAME;
		}
		AnimUtils.configAnim(this, className, true);

	}

	@Override
	protected TabBarOnCreateListener getCreateTabListener() {
		return this;
	}

	@Override
	public TabButton createTabView(TabBarView view, ZHTabInfo tab, int atIndex) {
		TabButton tb = new TabButton(this, tab.tabId);
		tb.icon.setImageResource(tab.arg1);
		tb.text.setText(tab.name);
		return tb;
	}

	@Override
	public void selectTabView(View view, ZHTabInfo tabInfo) {
		TabButton tb = (TabButton) view;
		tb.icon.setSelected(true);
		tb.text.setTextColor(getResources().getColor(R.color.color_dc));
		tb.hideRedDot();
	}

	@Override
	public void unSelectTabView(View view) {
		TabButton tb = (TabButton) view;
		tb.icon.setSelected(false);
		tb.text.setTextColor(getResources().getColor(
				R.color.tabbar_text_unsel_color));
	}

	public void showRedDot(int position) {
		if (getCurIndex() != position) {
			View tabView = tabBar.getTabView(position);
			TabButton tb = (TabButton) tabView;
			tb.showRedDot();
		}
	}

	public static class TabButton extends RelativeLayout {

		public final ImageView icon;
		public final TextView text;
		public final ImageView ivRedDot;

		public TabButton(Context context, int tabId) {
			super(context);

			icon = new ImageView(getContext());
			icon.setId(tabId);
			RelativeLayout.LayoutParams iconpl = new RelativeLayout.LayoutParams(
					DensityUtil.dip2px(20), DensityUtil.dip2px(20));
			iconpl.topMargin = DensityUtil.dip2px(7);
			iconpl.addRule(RelativeLayout.CENTER_HORIZONTAL);
			this.addView(icon, iconpl);

			text = new TextView(getContext());
			RelativeLayout.LayoutParams textpl = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			DensityUtil.setTextSize(text, R.dimen.txt_10);
			text.setTextColor(0xFF8F9498);
			textpl.addRule(RelativeLayout.BELOW, tabId);
			textpl.addRule(RelativeLayout.CENTER_HORIZONTAL);
			textpl.topMargin = DensityUtil.dip2px(2);
			this.addView(text, textpl);

			ivRedDot = new ImageView(getContext());
			ivRedDot.setImageResource(R.drawable.red_dot);
			ivRedDot.setVisibility(View.GONE);
			RelativeLayout.LayoutParams redDotParams = new RelativeLayout.LayoutParams(
					DensityUtil.dip2px(7), DensityUtil.dip2px(7));
			redDotParams.rightMargin = DensityUtil.dip2px(-2);
			redDotParams.topMargin = DensityUtil.dip2px(-2);
			redDotParams.addRule(RelativeLayout.ALIGN_RIGHT, tabId);
			redDotParams.addRule(RelativeLayout.ALIGN_TOP, tabId);
			this.addView(ivRedDot, redDotParams);
		}

		public void showRedDot() {
			ivRedDot.setVisibility(View.VISIBLE);
		}

		public void hideRedDot() {
			ivRedDot.setVisibility(View.GONE);
		}
	}

}
