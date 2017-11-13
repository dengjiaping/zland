package com.zhisland.android.blog.common.view.express;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.ResizeView;
import com.zhisland.lib.view.ResizeView.ResizeListener;

public class SimpleController {

	private static final int FULLSCREEN_INPUT_HEIGHT = DensityUtil.dip2px(100);

	@InjectView(R.id.root_view)
	public ResizeView rv;

	@InjectView(R.id.rl_chat_bottom)
	public View bottomView;

	@InjectView(R.id.fl_chat)
	public View expressContainer;

	private Activity activity;
	private EditText et;

	private static final int intervalPix = DensityUtil.dip2px(10);
	private static final int paddingPix = DensityUtil.dip2px(10);

	protected SimpleAttachController attachController;
	private LinearLayout toolbar;
	private Button btnExpress;

	protected boolean fullScreenInput = false;
	protected int inputType = 0;
	private static final int iconWidth = DensityUtil.dip2px(32);

	public boolean showExpress = false;;

	public SimpleController(Activity activity, View rootView, EditText etFeed) {

		this.activity = activity;
		this.et = etFeed;

		ButterKnife.inject(this, rootView);

		initBottom(activity, rootView);

		initViews();
	}

	private void initViews() {
		bottomView.setVisibility(View.GONE);

		toolbar = (LinearLayout) rv.findViewById(R.id.rl_chat_tool);
		toolbar.setPadding(0, paddingPix, 0, paddingPix);

		// 键盘icon
		this.btnExpress = new Button(activity);
		this.btnExpress.setBackgroundResource(R.drawable.sel_post_emotion);
		LayoutParams textParam = new LayoutParams(iconWidth, iconWidth);
		textParam.leftMargin = intervalPix;
		this.toolbar.addView(btnExpress, textParam);

		btnExpress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showExpress) {
					hideExpress();
				} else {
					showExpress();
				}
			}
		});

		FrameLayout container = (FrameLayout) rv.findViewById(R.id.fl_chat);
		attachController = new SimpleAttachController(activity, this,
				container, et);
		hideSoftIput();
	}

	private void initBottom(Activity activity, View rootView) {
		ChatResizeListener listener = new ChatResizeListener();
		rv.setListener(listener);
		rv.getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

	public void showExpress() {
		bottomView.setVisibility(View.VISIBLE);
		SimpleController.this.btnExpress
				.setBackgroundResource(R.drawable.sel_post_keyboard);
		hideSoftIput();
		showExpress = true;
	}

	private void hideExpress() {
		SimpleController.this.btnExpress
				.setBackgroundResource(R.drawable.sel_post_emotion);
		// expressContainer.setVisibility(View.VISIBLE);
		//
		// bottomView.setVisibility(View.GONE);
		showExpress = false;
		showSoftInput();
	}

	public void hideAll() {
		bottomView.setVisibility(View.GONE);
		showExpress = false;
		SimpleController.this.btnExpress
				.setBackgroundResource(R.drawable.sel_post_emotion);
		hideSoftIput();
	}

	public void showSoftInput() {
		adjustSoftInputMode();
		et.requestFocus();
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
				et, 0);

		showExpress = false;
		SimpleController.this.btnExpress
				.setBackgroundResource(R.drawable.sel_post_emotion);
	}

	/**
	 * ` 根据当前的attachcontroller 状态设置软键盘模式
	 */
	private void adjustSoftInputMode() {
		if (fullScreenInput) {
			activity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		} else {
			activity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}
	}

	protected void hideSoftIput() {
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	private class ChatResizeListener implements OnGlobalLayoutListener,
			ResizeListener {

		private int viewHeight;

		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (this == null)
				return;
			if (oldh <= 0) {
				viewHeight = h;
				return;
			}
			int diff = viewHeight - h;
			if (diff < 0)
				return;
			if (Math.abs(diff) > DensityUtil.getHeight() * 2 / 5) {
				// 正常输入法
				activity.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				attachController.refreshAttachHeight(diff);
				fullScreenInput = false;
				PrefUtil.Instance().setInputHeight(diff);
			} else if (Math.abs(diff) > FULLSCREEN_INPUT_HEIGHT) {
				// 全屏手写
				fullScreenInput = true;
			}
		}

		@Override
		public void onGlobalLayout() {

		}
	}
}
