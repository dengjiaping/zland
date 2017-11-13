package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * 数据为空时的界面
 */
public class EmptyView extends LinearLayout {

    private static final int BTN_TXT_TOP_MARGIN = DensityUtil.dip2px(15);
    private static final int BTN_TXT_HEIGHT = DensityUtil.dip2px(45);

	ImageView img;

	TextView tvPrompt;

	TextView tvBtn;

	public EmptyView(Context context) {
		this(context, null);
	}

	public EmptyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		img = new ImageView(getContext());
		LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		img.setLayoutParams(imgParams);
		addView(img);

		tvPrompt = new TextView(getContext());
		LinearLayout.LayoutParams promptParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		promptParams.topMargin = DensityUtil.dip2px(15);
		tvPrompt.setLayoutParams(promptParams);
        tvPrompt.setGravity(Gravity.CENTER);
        tvPrompt.setLineSpacing(0, 1.1f);
		DensityUtil.setTextSize(tvPrompt, R.dimen.txt_20);
		tvPrompt.setTextColor(getResources().getColor(R.color.color_f2));
		addView(tvPrompt);

		tvBtn = new TextView(getContext());
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, BTN_TXT_HEIGHT);
		btnParams.topMargin = BTN_TXT_TOP_MARGIN;
		tvPrompt.setLayoutParams(promptParams);
		tvBtn.setLayoutParams(btnParams);
		tvBtn.setVisibility(View.INVISIBLE);
		tvBtn.setPadding(DensityUtil.dip2px(25), DensityUtil.dip2px(10),
				DensityUtil.dip2px(25), DensityUtil.dip2px(10));
		tvBtn.setGravity(Gravity.CENTER);
		tvBtn.setBackgroundResource(R.drawable.rect_sdiv_clarge);
		DensityUtil.setTextSize(tvPrompt, R.dimen.txt_16);
		tvBtn.setTextColor(getResources().getColor(R.color.color_f3));
		addView(tvBtn);
	}

	public void setImgRes(int resId) {
		img.setImageResource(resId);
	}

	public void setPrompt(String txt) {
		tvPrompt.setText(txt);
	}

    public void setPrompt(Spannable spannable) {
        tvPrompt.setText(spannable);
    }

	public void setBtnText(String txt) {
		tvBtn.setText(txt);
	}

    public void setBtnTextColor(int color){
        tvBtn.setTextColor(getResources().getColor(color));
    }

    public void setBtnTextBackgroundResource(int resourceId){
        tvBtn.setBackgroundResource(resourceId);
    }

    public void setBtnTextWidth(int width){
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                width, BTN_TXT_HEIGHT);
        btnParams.topMargin = BTN_TXT_TOP_MARGIN;
        tvBtn.setLayoutParams(btnParams);
    }

	public void setBtnClickListener(View.OnClickListener listener) {
		tvBtn.setOnClickListener(listener);
	}

	public void setBtnVisibility(int visibility) {
		tvBtn.setVisibility(visibility);
	}

}
