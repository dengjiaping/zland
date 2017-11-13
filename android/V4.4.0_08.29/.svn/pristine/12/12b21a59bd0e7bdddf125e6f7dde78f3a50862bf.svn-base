package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class KeyboardScrollView extends ScrollView {
	private OnSizeChangedListener mChangedListener;
	private OnScrollChangedListener onScrollChangedListener;
	private boolean mShowKeyboard = false;

	public KeyboardScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public KeyboardScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KeyboardScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (null != mChangedListener && 0 != oldw && 0 != oldh) {
			if (oldh - h > 300) {
				mShowKeyboard = true;
				mChangedListener.onChanged(mShowKeyboard);
			} else if (h - oldh > 300) {
				mShowKeyboard = false;
				mChangedListener.onChanged(mShowKeyboard);
			}
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(onScrollChangedListener != null){
			onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void setOnSizeChangedListener(OnSizeChangedListener listener) {
		mChangedListener = listener;
	}
	
	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		onScrollChangedListener = listener;
	}

	public interface OnSizeChangedListener {
		void onChanged(boolean showKeyboard);
	}
	
	public interface OnScrollChangedListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
}
