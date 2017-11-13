package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhisland.android.blog.R;

/**
 * 根据宽按比例确定高的ImageView。
 * scaleWidth和scaleHeight在xml中设置，如果有任意一个未设置，那么该View就等于ImageView
 * */
public class ScaleImageView extends ImageView {

	private int scaleWidth = 0;
	private int scaleHeight = 0;

	public ScaleImageView(Context context) {
		this(context, null);
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray t = getContext().obtainStyledAttributes(attrs,
					R.styleable.ScaleImageView);
			scaleWidth = t.getInt(R.styleable.ScaleImageView_scaleWidth, 0);
			scaleHeight = t.getInt(R.styleable.ScaleImageView_scaleHeight, 0);
			t.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (scaleWidth == 0 || scaleHeight == 0) {
			return;
		}
		int speSize = MeasureSpec.getSize(heightMeasureSpec);
		int speMode = MeasureSpec.getMode(heightMeasureSpec);

		int height = speSize;
		int viewWidth = getMeasuredWidth();

		int intentHeight = viewWidth * scaleHeight / scaleWidth;

		if (speMode == MeasureSpec.AT_MOST) {
			height = intentHeight < speSize ? intentHeight : speSize;
		}
		if (speMode == MeasureSpec.UNSPECIFIED) {
			height = intentHeight;
		}
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
	}
}
