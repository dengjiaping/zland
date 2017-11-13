package com.zhisland.android.blog.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

public class CommonToast extends Dialog {

	@InjectView(R.id.llToast)
	LinearLayout llToast;

	@InjectView(R.id.ivToast)
	ImageView ivToast;

	@InjectView(R.id.tvToastOne)
	TextView tvToastOne;

	@InjectView(R.id.tvToastTwo)
	public TextView tvToastTwo;

	public CommonToast(Context context) {
		super(context, R.style.ActionDialog);
	}

	public CommonToast(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window dialogWindow = getWindow();
		dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		dialogWindow.getDecorView().setPadding(DensityUtil.dip2px(30), 0,
				DensityUtil.dip2px(30), 0);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.alpha = 0.95f; // 透明度
		dialogWindow.setAttributes(lp);

		this.setContentView(R.layout.zh_com_toast);
		ButterKnife.inject(this);
	}

	public void setImage(int resId) {
		ivToast.setImageResource(resId);
	}

	public void setTextOne(String text) {
		tvToastTwo.setVisibility(View.GONE);
		tvToastOne.setText(text);
	}

	public void setTextTwo(String text) {
		tvToastTwo.setVisibility(View.VISIBLE);
		tvToastTwo.setText(text);
	}

	@OnClick(R.id.llToast)
	public void onClickToast() {
		this.dismiss();
	}
}
