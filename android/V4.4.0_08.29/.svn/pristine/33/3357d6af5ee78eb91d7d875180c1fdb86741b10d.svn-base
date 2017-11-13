package com.zhisland.android.blog.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zhisland.android.blog.common.view.KeyboardScrollView;
import com.zhisland.android.blog.common.view.KeyboardScrollView.OnSizeChangedListener;
import com.zhisland.lib.util.ScreenTool;

public class SoftInputUtil {

	public static interface SoftInputListener {

		void showSoftInput();

		void hideSoftInput();
	}

	public static void listenSoftInput(final KeyboardScrollView view,
			final SoftInputListener listener) {

		view.setOnSizeChangedListener(new OnSizeChangedListener() {

			@Override
			public void onChanged(boolean showKeyboard) {
				if (showKeyboard) {
					listener.showSoftInput();
				} else {
					listener.hideSoftInput();
				}
			}
		});

	}

	public static void showKeyboard(View v) {
		if(v == null){
			return;
		}
		Context content = v.getContext();
		if(content == null){
			return;
		}
		InputMethodManager imm = (InputMethodManager) content
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

	}
	
	public static void hideInput(Activity activity,View v){
		((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
	}
	
	public static void hideInput(Activity activity) {
		ScreenTool.HideInput(activity);
	}
}
