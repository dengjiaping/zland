package com.zhisland.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ResizeView extends RelativeLayout {

	public static interface ResizeListener {

		public void onSizeChanged(int w, int h, int oldw, int oldh);
	}

	private ResizeListener listener;

	public void setListener(ResizeListener listener) {
		this.listener = listener;
	}

	public ResizeView(Context context) {
		super(context);
	}

	public ResizeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (listener != null) {
			listener.onSizeChanged(w, h, oldw, oldh);
			// int diff = oldh - h;
			// boolean keyboardShown = diff > 0
			// && Math.abs(diff) > DensityUtil.getHeight() / 6;
			// listener.onSoftKeyboardShown(keyboardShown);
		}
	}
}