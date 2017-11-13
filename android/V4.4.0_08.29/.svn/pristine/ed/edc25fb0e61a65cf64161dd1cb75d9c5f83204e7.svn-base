package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * 与scroll一起使用的titlebar，有渐变效果 <code>
 * ScrollViewExTitleListener titleAlphaListener = new ScrollViewExTitleListener();
		titleAlphaListener.setTitledOffset(headerHeight - titleHeight);
		titleAlphaListener.setTitleView(rlTitle);
		rlTitle.setLeftRes(R.drawable.sel_btn_feed_back,
				R.drawable.sel_btn_back);
		rlTitle.setRightRes(R.drawable.sel_btn_feed_more,
				R.drawable.sel_btn_more);
		scrollView.addOnScrollChangedListener(titleAlphaListener);
 * </code>
 * 
 * @author 向飞
 *
 */
public class ScrollTitleBar extends RelativeLayout {

	private ImageView ivLeft;
	private ImageView ivRight;
	private TextView tvTitle;
	private View line;

	private LayoutParams ivLeftLP;
	private LayoutParams ivRightLP;
	private LayoutParams tvTitleLP;

	private int leftAlphaRes = R.drawable.sel_btn_back_white;
	private int leftSolidRes = R.drawable.sel_btn_back;
	private int rightAlphaRes = R.drawable.sel_btn_feed_more;
	private int rightSolidRes = R.drawable.sel_btn_more;

	private float curAlpha;// 滑动值，1：实体，0：透明

	public ScrollTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.pageTitleBar);
		int indexCount = a.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int index = a.getIndex(i);
			switch (index) {
			case R.styleable.pageTitleBar_titleTextSize:
				tvTitle.setTextSize(DensityUtil.px2sp(a.getDimensionPixelSize(
						index, R.dimen.app_title_text_size)));
				break;
			case R.styleable.pageTitleBar_titleTextColor:
				tvTitle.setTextColor(a.getColor(index, 0));
				break;
			case R.styleable.pageTitleBar_titleTextLimitSize:
				InputFilter[] filters = { new LengthFilter(a.getInt(index, 0)) };
				tvTitle.setFilters(filters);
				break;
			case R.styleable.pageTitleBar_titleTextContent:
				tvTitle.setText(a
						.getString(R.styleable.pageTitleBar_titleTextContent));
				break;
			}
		}
		a.recycle();
	}

	public ScrollTitleBar(Context context) {
		super(context);
		initView();
	}

	private void initView() {

		ivLeft = new ImageView(getContext());
		ivRight = new ImageView(getContext());
		tvTitle = new TextView(getContext());
		int size = DensityUtil.dip2px(50);
		ivLeftLP = new LayoutParams(size, size);
		ivRightLP = new LayoutParams(size, size);
		tvTitleLP = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		ivLeft.setScaleType(ScaleType.CENTER);
		ivRight.setScaleType(ScaleType.CENTER);
		ivLeftLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		ivLeftLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		ivRightLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		tvTitleLP.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		ivLeft.setLayoutParams(ivLeftLP);
		ivRight.setLayoutParams(ivRightLP);
		tvTitle.setLayoutParams(tvTitleLP);

        DensityUtil.setTextSize(tvTitle, R.dimen.app_title_text_size);
		tvTitle.setTextColor(getResources().getColor(R.color.color_f1));
		tvTitle.setSingleLine(true);
		tvTitle.setEllipsize(TruncateAt.END);
		InputFilter[] filters = { new LengthFilter(6) };
		tvTitle.setFilters(filters);
		tvTitle.setVisibility(View.INVISIBLE);
		addView(ivLeft);
		addView(ivRight);
		addView(tvTitle);

		line = new View(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				getResources()
						.getDimensionPixelSize(R.dimen.app_divider_height));
		line.setBackgroundColor(getResources().getColor(R.color.color_div));
		line.setLayoutParams(lp);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		addView(line);
		line.setVisibility(View.GONE);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.tvTitle.setText(title);
	}

	/**
	 * 设置左边阿牛的背景
	 * 
	 * @param alphaRes
	 *            渐变背景
	 * @param solidRes
	 *            实背景
	 */
	public void setLeftRes(int alphaRes, int solidRes) {
		this.leftAlphaRes = alphaRes;
		this.leftSolidRes = solidRes;
		if (curAlpha < 1) {
			ivLeft.setImageResource(alphaRes);
		} else {
			ivLeft.setImageResource(solidRes);
		}
	}

	/**
	 * 设置右边按钮的背景
	 * 
	 * @param alphaRes
	 *            渐变背景
	 * @param solidRes
	 *            实背景
	 */
	public void setRightRes(int alphaRes, int solidRes) {
		this.rightAlphaRes = alphaRes;
		this.rightSolidRes = solidRes;
		if (curAlpha < 1) {
			ivRight.setImageResource(alphaRes);
		} else {
			ivRight.setImageResource(solidRes);
		}
	}

	public void updateAlpha(float alpha) {
		this.curAlpha = alpha;

		if (curAlpha < 1) {
			tvTitle.setVisibility(View.GONE);
			ivRight.setImageResource(rightAlphaRes);
			ivLeft.setImageResource(leftAlphaRes);
			line.setVisibility(View.GONE);
		} else {
			tvTitle.setVisibility(View.VISIBLE);
			ivRight.setImageResource(rightSolidRes);
			ivLeft.setImageResource(leftSolidRes);
			line.setVisibility(View.VISIBLE);
		}

		alpha = alpha > 1 ? 1 : alpha;
		setTitleBackground(alpha);
	}

	/**
	 * 设置左边按钮的点击监听
	 * 
	 * @param listener
	 */
	public void setLeftClickListener(OnClickListener listener) {
		ivLeft.setOnClickListener(listener);
	}

	/**
	 * 设置右边按钮的点击监听
	 * 
	 * @param listener
	 */
	public void setRightClickListener(OnClickListener listener) {
		ivRight.setOnClickListener(listener);
	}

	public void hideRightButton(){
		ivRight.setVisibility(View.GONE);
	}
	
	public void showRightButton(){
		ivRight.setVisibility(View.VISIBLE);
	}
	
	public void hideLeftButton(){
		ivLeft.setVisibility(View.GONE);
	}
	
	public void showLeftButton(){
		ivLeft.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 更新背景色
	 * 
	 * @param progress
	 */
	private void setTitleBackground(float progress) {
		if (progress < 0) {
			progress = 0;
		}
		int color = Color.argb((int) (255 * progress), 255, 255, 255);
		ColorDrawable drawable = new ColorDrawable();
		drawable.setColor(color);
		this.setBackgroundDrawable(drawable);
	}

}
