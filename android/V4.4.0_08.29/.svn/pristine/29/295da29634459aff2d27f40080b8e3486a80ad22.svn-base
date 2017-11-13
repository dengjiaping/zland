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
public class CustomInvitationCodeEditText extends LinearLayout {

	/**
	 * 输入框总长度
	 */
	private static int WIDTH = DensityUtil.dip2px(50);

	/**
	 * 高度
	 */
	private static int HEIGHT = DensityUtil.dip2px(50);

	/**
	 * 背景
	 */
	private static int BACKGROUND = R.drawable.bg_item_demand;

	/**
	 * 输入框文字颜色
	 */
	private static int TEXT_COLOR = R.color.color_dc;

	// private EditText editText1;
	// private EditText editText2;
	// private EditText editText3;
	// private EditText editText4;
	// private EditText editText5;
	// private EditText editText6;

	private List<EditText> editList;

	private boolean canFocusPreEdit;
	/**
	 * EditText 焦点
	 */
	private static final int NO_FOCUS = -1;
	private int curFocus = NO_FOCUS;

	private EditTextListener textListener;

	public CustomInvitationCodeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		setOrientation(LinearLayout.HORIZONTAL);

		editList = new ArrayList<EditText>();
		for (int i = 0; i < 6; i++) {
			EditText editView = createEditText(context, i == 5);
			editList.add(editView);
			addView(editView);
		}

		addTextChangeListener();
		addFocusChangeListener();
		if(editList!=null && editList.size() > 0 && editList.get(0)!=null){
			editList.get(0).requestFocus();
			if(curFocus == NO_FOCUS){
				curFocus = 0;
			}
		}
	}

	@SuppressLint("InflateParams")
	private EditText createEditText(Context context, boolean isLast) {
		EditText editText = (EditText) LayoutInflater.from(context).inflate(
				R.layout.custom_edit, null);
		LayoutParams etParams = new LayoutParams(WIDTH, HEIGHT);
		if (!isLast) {
			etParams.rightMargin = DensityUtil.dip2px(5);
		}
		editText.setLayoutParams(etParams);
		editText.setBackgroundResource(R.drawable.bg_invitation_code);
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
					if(curFocus == -1){
						try{
							editList.get(curFocus).requestFocus();
							curFocus = 0;
						}catch(Exception e){
							e.printStackTrace();
							return;
						}
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
			textListener.showNext(len == 6);
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
