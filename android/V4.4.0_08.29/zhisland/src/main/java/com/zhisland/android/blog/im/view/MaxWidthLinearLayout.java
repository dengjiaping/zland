package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MaxWidthLinearLayout extends LinearLayout {

	private int mMaxWidth = Integer.MAX_VALUE;
 
	public MaxWidthLinearLayout(Context context) {

		super(context);
	}

	public MaxWidthLinearLayout(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int newWidthMeasureSpec = widthMeasureSpec;
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		if (mMaxWidth > 0 && mMaxWidth < measuredWidth) {
			int measureMode = MeasureSpec.getMode(widthMeasureSpec);
			newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth,
					measureMode);
		}
		super.onMeasure(newWidthMeasureSpec, heightMeasureSpec);
	}

	public void setMaxWidth(int width) {
		mMaxWidth = width;
	}
}
