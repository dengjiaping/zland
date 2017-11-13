package com.zhisland.lib.image;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.lib.util.DensityUtil;

/**
 * @See MultiImgPickerActivity 自定义 已选择图片/可选择总数按钮 View
 */
public class FinishBtn extends LinearLayout {
	private TextView tv;

	public FinishBtn(Context context) {
		super(context);

	}

	public FinishBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		setPadding(0, 0, DensityUtil.dip2px(5), 0);
		tv = new TextView(context);
		addView(tv);
		tv.setTextSize(16);
	}

	void update(int max, int curvalue) {
		String text = curvalue == 0 ? "完成" : String.format("(%d/%d)完成",
				curvalue, max);
		tv.setText(text);
		if (curvalue <= 0) {
			tv.setTextColor(Color.LTGRAY);
			setEnabled(false);
		} else {
			tv.setTextColor(Color.DKGRAY);
			setEnabled(true);
		}
	}
}
