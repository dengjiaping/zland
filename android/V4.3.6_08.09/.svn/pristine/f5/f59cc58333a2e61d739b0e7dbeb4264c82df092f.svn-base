package com.zhisland.lib.util.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

public class ZHImageSpan extends ImageSpan {
	public int textHeight = 0;

	public ZHImageSpan(Drawable d, int verticalAlignment) {
		super(d, verticalAlignment);
	}

	public ZHImageSpan(Context context, int resourceId) {
		super(context, resourceId);
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {

		Drawable b = getDrawable();
		canvas.save();

		int transY = 0;

		if (textHeight > 0) {
			transY = bottom - textHeight;
		} else {
			transY = bottom - b.getBounds().bottom;
			if (mVerticalAlignment == ALIGN_BASELINE) {
				transY -= paint.getFontMetricsInt().descent;
			}
		}
		transY = transY > 0 ? transY : 0;
		canvas.translate(x, transY);
		b.draw(canvas);
		canvas.restore();
	}
}