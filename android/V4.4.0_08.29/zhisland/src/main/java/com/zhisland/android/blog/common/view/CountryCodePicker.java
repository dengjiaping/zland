package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.lib.view.NumberPicker;
import com.zhisland.lib.view.NumberPicker.Drawer;
import com.zhisland.lib.view.NumberPicker.Formatter;
import com.zhisland.lib.view.NumberPicker.OnValueChangeListener;
/**
 * 国家编码 选择器
 * @author zhangxiang
 *
 */
public class CountryCodePicker extends LinearLayout {

	static final String TAG = "CountryCodePicker";

	private List<Country> datas;

	private DictListener cityFormatter;
	private NumberPicker npDict;

	public CountryCodePicker(Context context) {
		super(context);
		initView(context);
	}

	public CountryCodePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CountryCodePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {

		setGravity(Gravity.CENTER_HORIZONTAL);
		npDict = new NumberPicker(context);
		this.addView(npDict, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		cityFormatter = new DictListener();

		npDict.setDrawer(cityFormatter);
		npDict.setOnValueChangedListener(cityFormatter);
		npDict.setEditOnTouch(false);
		npDict.setFocusable(true);
		npDict.setFocusableInTouchMode(true);
	}

	class DictListener implements Formatter, Drawer, OnValueChangeListener {

		@Override
		public void draw(Canvas canvas, String text, float x, float y,
				Paint large, Paint largeSmall) {
			if (text.length() < 20) {
				canvas.drawText(text, x, y, large);
			} else {
				canvas.drawText(text, x, y, largeSmall);
			}
		}

		@Override
		public String format(int value) {
			if (datas == null)
				return "unknown";

			Country dict = datas.get(value);
			return dict.name + " " + dict.showCode;
		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		}

	}

	public void dictSelector(int index) {
		int maxValue = datas.size() - 1;
		int minValue = 0;
		npDict.setMaxValue(maxValue);
		npDict.setMinValue(minValue);
		npDict.setValue(index);
	}
	public void initData(List<Country> datas) {
		this.datas = datas;

		npDict.setFormatter(cityFormatter);

		dictSelector(0);
	}

	public void setDict(Country dict) {
		for (int i = 0; i < datas.size(); i++) {
			if (dict.code != null && dict.code.equals(datas.get(i))) {
				npDict.setValue(i);
			}
		}

	}

	public Country getCurrentDict() {
		int dictIndex = npDict.getValue();
		Country dict = datas.get(dictIndex);
		return dict;
	}

	public String getDictCode() {
		return datas.get(npDict.getValue()).code;
	}

	public String getDictName() {
		return datas.get(npDict.getValue()).name;
	}
}
