package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.StringUtil;

public class CountEditText extends RelativeLayout {

	private static final int DEFAULT_MAX_COUNT = 60;

	private static final int TEXT_VIEW_ID = 101;
	private EditText editText;

	private TextView tvCount;

	private int maxCount = DEFAULT_MAX_COUNT;

	public CountEditText(Context context) {
		super(context);
		initView(context);
	}

	public CountEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tvCount = new TextView(context);
		tvCount.setId(TEXT_VIEW_ID);
		tvCount.setTextColor(context.getResources().getColor(R.color.color_f3));
		tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tvCount.setText(String.valueOf(DEFAULT_MAX_COUNT));
		tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.addView(tvCount, tvParams);

		LayoutParams etParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		editText = new EditText(context);
		editText.setGravity(Gravity.BOTTOM);
		editText.setTextColor(Color.BLACK);
		editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxCount) });
		editText.setPadding(0, 0, 0, 0);
		editText.setBackgroundColor(getResources()
				.getColor(R.color.transparent));
		etParams.addRule(RelativeLayout.LEFT_OF, TEXT_VIEW_ID);
		this.addView(editText, etParams);

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int len = s.toString().length();
				int remainLen = maxCount - len;
				tvCount.setText(String.valueOf(remainLen));
			}
		});
		initEditTextScroll();
	}

	/**
	 * 输入框 EditText
	 */
	public EditText getEditText() {
		return editText;
	}

	/**
	 * 设置默认值
	 */
	public void setDefaultText(String text) {
		editText.setText(text);
	}

	/**
	 * 计数 TextView
	 */
	public TextView getTVCount() {
		return tvCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		tvCount.setText(String.valueOf(maxCount));
		String intoStr = "";
		tvCount.setText(String.valueOf(maxCount));
		if (StringUtil.isNullOrEmpty(intoStr = editText.getText().toString())) {
			if (intoStr.length() > maxCount) {
				intoStr.substring(0, maxCount);
				editText.setText(intoStr);
				tvCount.setText(String.valueOf(maxCount - intoStr.length()));
			}
		}
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxCount) });

	}

	public String getText() {
		return editText.getText().toString().trim();
	}

	public void setGravity(int type) {
		editText.setGravity(type);
	}

	public void setHint(String hint) {
		editText.setHint(hint);
	}

	public void setEditTextColor(int color) {
		editText.setTextColor(color);
	}
	
	public void setText(String text){
		editText.setText(text);
	}
	/**
	 * 当edittext在scrollview中时，对其滑动做出一些操作
	 */
	private void initEditTextScroll() {
		editText.setOnTouchListener(new OnTouchListener() {
			float orgY, newY, height;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 这句话说的意思告诉父View我自己的事件我自己处理
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					orgY = event.getY();
					height = (int) (editText.getLineCount()
							* editText.getTextSize() + editText.getLineCount()
							* editText.getLineSpacingMultiplier()
							+ editText.getPaddingTop() + editText
							.getPaddingBottom());
					break;
				case MotionEvent.ACTION_MOVE:
					newY = event.getY();
					// 向下滑
					if (newY - orgY > 0) {
						if (editText.getScrollY() != 0) {
							v.getParent().requestDisallowInterceptTouchEvent(
									true);
						}
					}
					// 向上滑
					else if (newY - orgY < 0) {
						if (height > editText.getHeight())
							if (height > editText.getScrollY()
									+ editText.getHeight()) {
								v.getParent()
										.requestDisallowInterceptTouchEvent(
												true);
							}
					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

}
