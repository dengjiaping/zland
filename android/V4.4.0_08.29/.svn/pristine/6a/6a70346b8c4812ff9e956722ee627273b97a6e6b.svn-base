package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * 自定义验证码
 * 
 * @author hui
 * 
 */
public class CustomVerfiyCodeEditText extends LinearLayout {

	/**
	 * EditText 数目
	 */
	private static int SIZE = 4;

	/**
	 * 输入框总长度
	 */
	private static int WIDTH = DensityUtil.dip2px(220);

	/**
	 * 高度
	 */
	private static int HEIGHT = DensityUtil.dip2px(50);

	/**
	 * 背景
	 */
	private static int BACKGROUND = R.drawable.bg_item_demand;

	/**
	 * 分割线颜色
	 */
	private static int SEG_LINE_COLOR = R.color.color_div;

	/**
	 * 分割线宽度
	 */
	private static int SEG_LINE_WIDTH = DensityUtil.dip2px(1);

	/**
	 * 输入框文字颜色
	 */
	private static int TEXT_COLOR = R.color.color_dc;

	private List<EditText> editList;

	private boolean canFocusPreEdit;
	/**
	 * EditText 焦点
	 */
	private static final int NO_FOCUS = -1;
	private int curFocus = NO_FOCUS;

	private EditTextListener textListener;

	public CustomVerfiyCodeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		setOrientation(LinearLayout.HORIZONTAL);
		setBackgroundResource(BACKGROUND);

		editList = new ArrayList<EditText>();
		for (int i = 0; i < 4; i++) {
			EditText editView = createEditText(context);
			editList.add(editView);
			addView(editView);
			if (i < 3) {
				View setLine = createSegLine(context);
				addView(setLine);
			}
		}
		addFocusChangeListener();
		addTextChangeListener();
	}

	private View createSegLine(Context context) {
		View segLine = new View(context);
		segLine.setBackgroundColor(getResources().getColor(SEG_LINE_COLOR));
		LayoutParams lineParams = new LayoutParams(SEG_LINE_WIDTH, HEIGHT);
		segLine.setLayoutParams(lineParams);
		return segLine;
	}

	@SuppressLint("InflateParams")
	private EditText createEditText(Context context) {
		EditText editText = (EditText) LayoutInflater.from(context).inflate(
				R.layout.custom_edit, null);
		LayoutParams etParams = new LayoutParams(WIDTH / SIZE, HEIGHT);
		editText.setLayoutParams(etParams);
		return editText;
	}

	public String getText() {
		String text = "";
		for (int i = 0; i < editList.size(); i++) {
			text += editList.get(i).getText().toString();
		}
		return text;
	}

	public void delete() {
		if (curFocus == NO_FOCUS) {
			return;
		}
		int curFocus = this.curFocus;		
		if (curFocus > 0) {
			int curLen = editList.get(curFocus).getText().toString().length();		
			if (editList.get(curFocus).getSelectionStart() == 0 && curLen > 0)
				editList.get(curFocus).setText("");
			else if(curLen == 0 && canFocusPreEdit){
				editList.get(curFocus - 1).requestFocus();
				editList.get(curFocus - 1).setText("");
				editList.get(curFocus - 1).setSelection(
						editList.get(curFocus - 1).getText().toString()
								.length());
				canFocusPreEdit = true;
			}
			if (!canFocusPreEdit) {
				editList.get(curFocus).requestFocus();
				canFocusPreEdit = true;
			}
		}

	}

	private void addFocusChangeListener() {
		for (int i = 0; i < editList.size(); i++) {
			final int index = i;
			editList.get(index).setOnFocusChangeListener(
					new OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								curFocus = index;
							} else {
								curFocus = NO_FOCUS;
							}
						}
					});
		}
	}

	private void addTextChangeListener() {
		for (int i = 0; i < editList.size(); i++) {
			final int index = i;
			editList.get(index).addTextChangedListener(new TextWatcher() {

				int sel;

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					sel = start;
					if(curFocus == NO_FOCUS){
						return;
					}
					canFocusPreEdit = (editList.get(curFocus).getText()
							.toString().length() == 0);
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (s.length() > 1) {
						if (sel == 0) {
							editList.get(index).setText(s.subSequence(0, 1));
						} else {
							editList.get(index).setText(s.subSequence(1, 2));
						}
						return;
					}
					if (s.length() > 0) {
						if (index < editList.size() - 1) {
							editList.get(index + 1).requestFocus();
						}
					}
					check();
				}
			});
		}
	}

	public void check() {
		if (textListener != null) {
			int len = getText().length();
			textListener.showNext(len == 4);
		}
	}

	public void setListener(EditTextListener listener) {
		this.textListener = listener;
	}

	public void childFocus() {
		View v = null;
		for (int i = 0; i < editList.size(); i++) {
			v = editList.get(i);
			if (editList.get(i).getText().toString().length() == 0) {
				break;
			}
		}
		if (v != null) {
			v.requestFocus();
		}
	}
}
