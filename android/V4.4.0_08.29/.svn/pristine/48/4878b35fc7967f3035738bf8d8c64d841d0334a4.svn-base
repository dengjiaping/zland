package com.zhisland.lib.view.icon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhisland.lib.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * T is the main view type and V is icon view's type
 * 
 */
public abstract class TwoViews<T extends View, V extends View> extends
		RelativeLayout {

	private static final int PADDING_HOR = DensityUtil.dip2px(14);
	private static final int PADDING_VER = DensityUtil.dip2px(8);
	protected T mainView;
	protected V ivIcon;

	public TwoViews(Context context) {
		super(context);
		init(context, null);
	}

	public TwoViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TwoViews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public void showIcon() {
		if (ivIcon != null) {
			ivIcon.setVisibility(View.VISIBLE);
		}
	}

	public void hideIcon() {
		if (ivIcon != null) {
			ivIcon.setVisibility(View.GONE);
		}
	}

	protected void init(Context context, AttributeSet attrs) {

		mainView = this.createMainView(context, attrs);
		mainView.setId(R.id.first_view);
		RelativeLayout.LayoutParams mainPrams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setMainViewLayoutParams(mainPrams);
		this.addView(mainView, mainPrams);

		ivIcon = createIconView(context, attrs);
		ivIcon.setId(R.id.second_view);
		RelativeLayout.LayoutParams iconPrams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setIconLayoutParams(iconPrams);
		ivIcon.setDuplicateParentStateEnabled(true);
		this.addView(ivIcon, iconPrams);
	}

	protected abstract V createIconView(Context context, AttributeSet attrs);

	protected abstract T createMainView(Context context, AttributeSet attrs);

	protected void setMainViewLayoutParams(LayoutParams mainPrams) {

	}

	protected void setIconLayoutParams(RelativeLayout.LayoutParams iconPrams) {
	}

	public T getMainView() {
		return mainView;
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		mainView.setTag(tag);
	}

	public V getIconView() {
		return ivIcon;
	}

	/**
	 * set paddings
	 */
	protected void commonConfig() {
		this.setPadding(PADDING_HOR, PADDING_VER, PADDING_HOR, PADDING_VER);

	}
}
