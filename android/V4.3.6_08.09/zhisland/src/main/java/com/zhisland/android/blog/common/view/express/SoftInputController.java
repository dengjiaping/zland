package com.zhisland.android.blog.common.view.express;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhisland.lib.util.DensityUtil;

public class SoftInputController {

	View root;

	View elseView;

	View toolTop;

	EditText et;

	Activity activity;

	private int softInputH = 50;

	boolean softIsShow = false;

	private int toolTopH = 0;

	private int position;

	private boolean notifyComeOut = false;

	public SoftInputController(Activity activity, View root, View elseView,
			View toolTop, EditText et, CallBack callBack) {
		this.activity = activity;
		this.root = root;
		this.elseView = elseView;
		this.toolTop = toolTop;
		this.et = et;
		this.callBack = callBack;
		root.addOnLayoutChangeListener(rootLayoutChangeListener);

		toolTop.addOnLayoutChangeListener(toolLayoutChangeListener);

		et.setOnKeyListener(onKeyListener);

		et.setOnTouchListener(onTouchListener);
	}

	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN && !softIsShow
					&& elseViewIsShow()) {
				hideElseAndShowSoft();
				return true;
			}
			return false;
		}
	};

	private OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				return true;
			}
			return false;
		}
	};

	private OnLayoutChangeListener rootLayoutChangeListener = new OnLayoutChangeListener() {

		@Override
		public void onLayoutChange(View v, int left, int top, int right,
				int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			if (oldBottom - bottom > DensityUtil.dip2px(150)) {
				softIsShow = true;
				softInputH = oldBottom - bottom;
				callBackSoftOrElseShow();
			} else {

			}
			if (bottom - oldBottom > DensityUtil.dip2px(150) && oldBottom > 0) {
				softIsShow = false;
				if (elseViewIsShow()) {

				} else {
					hideToolTop();
				}
			}
		}
	};

	private void callBackSoftOrElseShow() {
		if (callBack != null) {
			if (notifyComeOut && softOrElseIsShow()) {
				callBack.softOrElseShow(position, softInputH);
				notifyComeOut = false;
			}
		}
	}

	private OnLayoutChangeListener toolLayoutChangeListener = new OnLayoutChangeListener() {

		@Override
		public void onLayoutChange(View v, int left, int top, int right,
				int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			int height = toolTop.getHeight();
			if (height > 0 && 0 == toolTopH) {
				if (callBack != null) {
					callBack.toolTopShow();
				}
				notifyComeOut = true;
				callBackSoftOrElseShow();
			} else if (height > 0 && height != toolTopH) {
				if (callBack != null) {
					callBack.changed(position);
				}
			} else if (height == 0 && toolTopH > 0) {
				if (callBack != null) {
					callBack.hide();
				}
			}
			toolTopH = height;
		}
	};

	public void setOnKeyListener(OnKeyListener onKeyListener) {
		this.onKeyListener = onKeyListener;
		et.setOnKeyListener(onKeyListener);
	}

	private void hideElseAndShowSoft() {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		showSoftInput();
		hideElseView();
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	public boolean softOrElseIsShow() {
		return softIsShow || elseViewIsShow();
	}

	private void showSoftInput() {
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
				et, 0);
	}

	public void showSoftInput(int position) {
		this.position = position;
		showToolTop();
		et.requestFocus();
		showSoftInput();
	}

	public void hideSoftInput() {
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	public boolean elseViewIsShow() {
		ViewGroup.LayoutParams lp = elseView.getLayoutParams();
		if (lp.height == softInputH) {
			return true;
		}
		return false;
	}

	public void hideAllView() {
		if (softIsShow) {
			hideSoftInput();
		}
		if (elseViewIsShow()) {
			hideElseView();
			hideToolTop();
			elseView.requestLayout();
		}
	}

	private void showElseView() {
		ViewGroup.LayoutParams lp = elseView.getLayoutParams();
		lp.height = softInputH;
	}

	private void hideElseView() {
		ViewGroup.LayoutParams lp = elseView.getLayoutParams();
		lp.height = 0;
	}

	private void showToolTop() {
		ViewGroup.LayoutParams lp = toolTop.getLayoutParams();
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
	}

	private void hideToolTop() {
		ViewGroup.LayoutParams lp = toolTop.getLayoutParams();
		lp.height = 0;
		et.clearFocus();
	}

	public void switchClick() {
		if (elseViewIsShow()) {
			hideElseAndShowSoft();
		} else {
			showElseView();
			hideSoftInput();
		}
	}

	CallBack callBack;

	public interface CallBack {

		void hide();

		void softOrElseShow(int position, int softH);

		void toolTopShow();

		void changed(int position);
	}
}
