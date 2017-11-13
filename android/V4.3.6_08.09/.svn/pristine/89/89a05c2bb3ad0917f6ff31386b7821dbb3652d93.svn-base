package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MultilineTextView extends TextView {

	private boolean calculatedLines = false;

	public MultilineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!calculatedLines) {
			calculateLines();
			calculatedLines = true;
		}

		super.onDraw(canvas);
	}

	private void calculateLines() {
		int mHeight = getMeasuredHeight();
		int lHeight = getLineHeight();
		int lines = mHeight / lHeight;
		setLines(lines);
	}
}
