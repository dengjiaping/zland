package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class RedDotView extends TextView {

	private ViewGroup.LayoutParams vlp;
	private int dotCount;
	public RedDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		if (w <= 0 || h <= 0) {
			return;
		}
		if (w < h) {
			w = h;
			vlp = getLayoutParams();
			vlp.width = vlp.height = w;
			this.setLayoutParams(vlp);
		}
		try{
			dotCount = Integer.valueOf(this.getText().toString());
			if(dotCount > 99){
				this.setText("99+");
			}
		}catch(Exception e){
			if("99+".equals(this.getText().toString())){
				this.setText("99+");
			}else{
				this.setText("...");
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
