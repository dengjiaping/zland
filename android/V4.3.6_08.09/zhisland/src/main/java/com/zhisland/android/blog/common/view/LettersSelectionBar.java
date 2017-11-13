package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

public class LettersSelectionBar extends LinearLayout implements
		View.OnTouchListener {
	private char[] letters;
	private int viewWith;
	private int viewHeight;
	private int selectIndex = -1;
	private int scale = 0;
	private int COLOR;
	private final int COLOR_FOCUS = 0xff313439;
	private LettersSelectionBar.OnLetterSelectedListener onLetterSelectedListener;

	public LettersSelectionBar(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}

	public LettersSelectionBar(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}

	private void init(Context paramContext) {
		this.setOrientation(VERTICAL);

		COLOR = paramContext.getResources().getColor(R.color.color_dc);
		setOnTouchListener(this);
		setBackground();
		viewWith = getWidth();
		viewHeight = getHeight();
	}

	public void setLetters(char[] letters) {

		this.letters = letters;
		this.removeAllViews();

		if (this.letters != null && this.letters.length > 0) {
			LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			textLayoutParams.weight = 1.0f;
			textLayoutParams.setMargins(0, 0, 8, 0);
			for (char c : letters) {
				TextView label = new TextView(this.getContext());
				label.setLayoutParams(textLayoutParams);
				label.setText(Character.toString(c));
				label.setTextColor(COLOR);
				DensityUtil.setTextSize(label, R.dimen.txt_12);
				label.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.RIGHT);
				label.setTypeface(null, Typeface.BOLD);
				this.addView(label);
			}
		}
	}

	public interface OnLetterSelectedListener {
		public void onLetterSelected(char paramChar);
	}

	private void setBackground() {
		this.setBackgroundColor(Color.TRANSPARENT);
	}

	private void setPressedBackground() {
		this.setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		viewWith = getWidth();
		viewHeight = getHeight();
		if (this.letters != null && this.letters.length > 0) {
			scale = (viewHeight / letters.length);// 将view高度分成27份，26个字母 +
													// A字母上面的@
		} else {
			scale = 0;
		}
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		int action;
		if (scale != 0) {
			int eventX = (int) paramMotionEvent.getX();
			int eventY = (int) paramMotionEvent.getY();
			action = paramMotionEvent.getAction();
			if ((eventX >= 0) && (eventX <= viewWith) && (eventY >= 0)
					&& (eventY <= viewHeight)) {
				eventX = eventY / scale;
				if ((eventX >= 0) && (eventX < letters.length)) {
					if ((selectIndex != eventX) || (action == 0)) {
						if (selectIndex >= 0) {
							TextView old = (TextView) this
									.getChildAt(selectIndex);
							old.setTextColor(COLOR);
						}
						selectIndex = eventX;
						TextView now = (TextView) this.getChildAt(selectIndex);
						now.setTextColor(COLOR_FOCUS);
						setPressedBackground();
						if (onLetterSelectedListener != null) {
							onLetterSelectedListener
									.onLetterSelected(letters[selectIndex]);
						}
					}
				}
			}
			if ((action == 1) || (action == 3)) {
				if (selectIndex >= 0) {
					TextView old = (TextView) this.getChildAt(selectIndex);
					old.setTextColor(COLOR);
				}
				selectIndex = -1;
				setBackground();
			}
		}
		return true;
	}

	public void setHasSearchIcon(boolean paramBoolean) {
		setBackground();
	}

	public void setOnLetterSelectedListener(
			LettersSelectionBar.OnLetterSelectedListener paramOnLetterSelectedListener) {
		this.onLetterSelectedListener = paramOnLetterSelectedListener;
	}
}
