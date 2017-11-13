package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineProgressView extends View {
	private int width;
	private int progress;
	private int height;
	int color = -1;
	Paint paint;

	public LineProgressView(Context context) {
		super(context);
	}

	public LineProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void update(int progress) {
		this.progress = progress;
		if (width == 0) {
			color = getResources().getColor(
					com.zhisland.android.blog.R.color.color_dc);
			width = getWidth();
			height = getHeight();
			paint = new Paint();
			paint.setStrokeWidth(2);
			paint.setColor(color);
		}
		postInvalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		//Log.i("zxdxp8l",progress+"");
		if(progress == 100){
			paint.setColor(Color.WHITE);
		}
		try {
			canvas.drawRect(0, 0, progress * 1f / 100f * width, height, paint);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
