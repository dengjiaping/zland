package com.zhisland.android.blog.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

public class CommonDialog extends Dialog {
	public static boolean SOFT_KEY_UP = true;
	public static boolean SOFT_KEY_DOWN = false;

	@InjectView(R.id.tvDlgTitle)
	TextView tvDlgTitle;

	@InjectView(R.id.tvDlgContent)
	TextView tvDlgContent;

	@InjectView(R.id.etDlg)
	public EditText etDlg;

	@InjectView(R.id.tvDlgCancel)
	public TextView tvDlgCancel;

	@InjectView(R.id.tvDlgConfirm)
	public TextView tvDlgConfirm;

	@InjectView(R.id.vline)
	public View vline;
	boolean isPop;
	InputMethodManager imm;

	public CommonDialog(Context context, boolean flag) {
		super(context, R.style.PROGRESS_DIALOG);
		isPop = flag;

		if (flag) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			imm = (InputMethodManager) getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
		}
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
			}
		});

	}

	public CommonDialog(Context context) {
		super(context, R.style.PROGRESS_DIALOG);
	}

	public CommonDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window dialogWindow = getWindow();
		dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		dialogWindow.getDecorView().setPadding(DensityUtil.dip2px(30), 0,
				DensityUtil.dip2px(30), 0);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.alpha = 0.95f; // 透明度
		dialogWindow.setAttributes(lp);

		this.setContentView(R.layout.zh_com_dialog);
		ButterKnife.inject(this);

	}

	@OnClick(R.id.tvDlgCancel)
	public void onClickCancel() {
		this.dismiss();
	}

	@OnClick(R.id.tvDlgConfirm)
	public void onClickConfirm() {
		this.dismiss();
	}

	/**
	 * 设置标题
	 */
	public void setTitle(String text) {
		tvDlgTitle.setVisibility(View.VISIBLE);
		tvDlgTitle.setText(text);
	}

	/**
	 * 设置内容
	 */
	public void setContent(String text) {
		tvDlgContent.setVisibility(View.VISIBLE);
		tvDlgContent.setText(text);
	}

	public void setContent(Spanned text) {
		tvDlgContent.setVisibility(View.VISIBLE);
		tvDlgContent.setText(text);
	}

	public void setContent(SpannableString text) {
		tvDlgContent.setVisibility(View.VISIBLE);
		tvDlgContent.setText(text);
	}

	/**
	 * 设置编辑框内容
	 */
	public void setEditHint(String text) {
		etDlg.setVisibility(View.VISIBLE);
		etDlg.setHint("");
		etDlg.requestFocus();
	}

	public void setMaxEditCount(final int maxCount) {
		etDlg.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxCount + 1) });
	}

	/**
	 * 获取编辑框内容
	 */
	public String getEditTextContent() {
		return etDlg.getText().toString().trim();
	}

	/**
	 * 清除EditText
	 */
	public void clearEditText() {
		etDlg.setText("");
	}

	/**
	 * 设置左边按钮
	 */
	public void setLeftBtnContent(String text) {
		tvDlgCancel.setText(text);
	}

	public void setLeftBtnColor(int colorId) {
		tvDlgCancel.setTextColor(colorId);
	}

	public void setLeftBtnGone() {
		tvDlgCancel.setVisibility(View.GONE);
		vline.setVisibility(View.GONE);
	}

	public void setLeftBtnVisiable() {
		tvDlgCancel.setVisibility(View.VISIBLE);
		vline.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置右边按钮
	 */
	public void setRightBtnContent(String text) {
		tvDlgConfirm.setText(text);
	}

	public void setRightBtnColor(int colorId) {
		tvDlgConfirm.setTextColor(colorId);
	}

	public void setRightBtnGone() {
		tvDlgConfirm.setVisibility(View.GONE);
		vline.setVisibility(View.GONE);
	}

	public void setRightBtnVisiable() {
		tvDlgConfirm.setVisibility(View.VISIBLE);
		vline.setVisibility(View.VISIBLE);
	}
	
	public void setEditTextVisibility(int visibility) {
		etDlg.setVisibility(visibility);
	}

	// 重置dialog
	public void resetDialog() {
		tvDlgTitle.setVisibility(View.GONE);
		tvDlgContent.setVisibility(View.GONE);
		etDlg.setVisibility(View.GONE);
		tvDlgCancel.setVisibility(View.VISIBLE);
		tvDlgConfirm.setVisibility(View.VISIBLE);
		vline.setVisibility(View.VISIBLE);
	}
}
