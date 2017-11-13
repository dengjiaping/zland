package com.zhisland.android.blog.im.view;

import java.io.File;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.im.view.AudioCaptureView.OnAudioCaptureListener;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.SwipeView.InterceptListener;

/**
 * 用来控制聊天页面的底部操作栏
 * 
 * @author arthur
 * 
 */
public class SessBottomController implements OnClickListener,
		OnAudioCaptureListener, OnTouchListener {

	private static final String TAG = "SessBottomController";
	private static final int intervalPix = DensityUtil.dip2px(10);
	private static final int paddingPix = DensityUtil.dip2px(10);
	private static final String RECORD = "按住大声说";
	private static final String FINISH_RECORD = "手指上滑，取消发送";
	private static final int INPUT_TYPE_NONE = 0;
	private static final int INPUT_TYPE_SOFT = 1;
	private static final int INPUT_TYPE_ATTACH = 2;
	private static final int INPUT_TYPE_EXPTRESS = 3;

	private final Activity activity;
	private final View rootView;
	private final Handler handler;
	private final ISessController controller;
	private final boolean isFromGroup;

	private View audioContainer;
	private AudioCaptureView audioView;

	protected AttachController attachController;

	private LinearLayout toolbar;
	private final int totalWidth;

	private Button btnText;
	private Button btnAudio;
	private Button btnAdd;
	private Button btnExpress;
	private Button btnSend;
	private Button btnRecord;
	private IMEditText etText;

	private View attachPlaceHolder;// 用来在聊天页面进行高度占位
	private PopupWindow popAttach;// 用来显示attach
	protected boolean isKeyBoardVisible;// 是否弹出了键盘

	private int smallWidth = -1;
	private int largeWidth = -1;
	private int btnSendWidth = -1;
	private int btnHeight;
	private int iconWidth, iconHeight;

	/**
	 * 当前输入法是不是全屏手写
	 */
	protected boolean fullScreenInput = false;
	protected int inputType = 0;

	public SessBottomController(Activity activity, View rootView,
			Handler handler, ISessController controller, boolean isFromGroup) {
		this.activity = activity;
		this.rootView = rootView;
		this.handler = handler;
		this.controller = controller;
		this.isFromGroup = isFromGroup;

		this.totalWidth = DensityUtil.getWidth();

		initSize();
		initViews();
	}

	public void appendAt(String username) {
		if (this.etText != null) {
			this.etText.append("@" + username);
		}
	}

	public void setHint(String username) {
		if (this.etText != null) {
			this.etText.setHint("@" + username);
		}
	}

	public void setEditText(CharSequence text) {
		if (this.etText != null) {
			this.etText.setText(text);
			if (text != null && text.length() > 0) {
				etText.setSelection(text.length() - 1);
			}
		}
	}

	public void insertEditText(CharSequence text) {
		if (this.etText != null) {
			int start = etText.getSelectionStart();
			int end = etText.getSelectionEnd();
			etText.getText().replace(Math.min(start, end),
					Math.max(start, end), text, 0, text.length());
		}
	}

	public String getEditText() {
		if (this.etText != null) {
			return this.etText.getText().toString();
		}

		return null;
	}

	public void setEditTextChangedListener(TextWatcher watcher) {
		if (this.etText != null) {
			this.etText.addTextChangedListener(watcher);
		}
	}

	private void initViews() {
		buildToolBar();
		buildAttach();
		buildAudio();
	}

	/**
	 * 刷新键盘的高度
	 * 
	 * @param diff
	 */
	public void refreshAttachHeight(int diff) {
		attachController.refreshAttachHeight(diff);
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) attachPlaceHolder
				.getLayoutParams();
		params.height = diff;
		attachPlaceHolder.setLayoutParams(params);
	}

	/**
	 * 构造附件栏
	 */
	private void buildAttach() {
		attachPlaceHolder = rootView.findViewById(R.id.fl_chat);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View attachContentView = inflater.inflate(R.layout.chat_session_attach,
				null);
		attachController = new AttachController(activity, this,
				attachContentView, etText, controller, isFromGroup);
		// attachController.hide();
		int totalHeight = PrefUtil.Instance().getInputHeight();
		if (totalHeight <= 0) {
			totalHeight = AttachController.getAttachDefaultHeight();
		}
		popAttach = new PopupWindow(attachContentView,
				LayoutParams.MATCH_PARENT, (int) totalHeight, false);
		// popAttach.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
		// popAttach.setOutsideTouchable(true);
		popAttach.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				attachPlaceHolder.setVisibility(View.GONE);
			}
		});
		if (PrefUtil.Instance().getInputHeight() > 0) {
			this.refreshAttachHeight(PrefUtil.Instance().getInputHeight());
		} else {
			this.refreshAttachHeight(AttachController.getAttachDefaultHeight());
		}
	}

	/**
	 * 构造录音
	 */
	private void buildAudio() {
		audioContainer = rootView.findViewById(R.id.rl_chat_audio);
		audioView = (AudioCaptureView) rootView
				.findViewById(R.id.av_chat_audio);

		if (audioView != null && audioView.isInitSuccess()) {
			audioView.setAudioListener(this);
		} else {
			ToastUtil.showShort("设备未就绪");
		}
	}

	/**
	 * 构造聊天页面的工具栏
	 */
	private void buildToolBar() {
		toolbar = (LinearLayout) rootView.findViewById(R.id.rl_chat_tool);
		toolbar.setPadding(0, paddingPix, 0, paddingPix);

		// 语音icon
		this.btnAudio = new Button(activity);
		this.btnAudio.setBackgroundResource(R.drawable.sel_chat_audio_record);
		LayoutParams audioParam = new LayoutParams(iconWidth, iconHeight);
		audioParam.leftMargin = intervalPix;
		this.toolbar.addView(btnAudio, audioParam);

		// 键盘icon
		this.btnText = new Button(activity);
		this.btnText.setBackgroundResource(R.drawable.sel_chat_text);
		LayoutParams textParam = new LayoutParams(iconWidth, iconHeight);
		textParam.leftMargin = intervalPix;
		this.toolbar.addView(btnText, textParam);

		// 录音 按钮
		this.btnRecord = new Button(activity);
		btnRecord.setTextColor(activity.getResources().getColor(
				R.color.color_f2));
		DensityUtil.setTextSize(btnRecord, R.dimen.txt_16);
		btnRecord.setOnTouchListener(SessBottomController.this);
		btnRecord.setText(RECORD);
		btnRecord.setBackgroundResource(R.drawable.bg_chat_text);
		btnRecord.setPadding(0, 0, 0, 0);
		LayoutParams recordParams = new LayoutParams(largeWidth, btnHeight);
		recordParams.leftMargin = intervalPix;
		this.toolbar.addView(btnRecord, recordParams);

		// 输入框
		this.etText = (IMEditText) LayoutInflater.from(activity).inflate(
				R.layout.chat_edittext, null);
		this.etText.setHideOtherListener(new HideOther() {

			@Override
			public void hideToolsbar() {
				showText(true);
			}
		});
		this.etText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (popAttach.isShowing())
						popAttach.dismiss();
				}
				return false;
			}
		});
		this.etText.setOnClickListener(this);

		this.etText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String text = s.toString().replace("\n", "").replace(" ", "");
				if (text.length() > 0
						&& btnSend.getVisibility() != View.VISIBLE) {
					ValueAnimator va = ValueAnimator.ofFloat(0.4f, 1.0f);
					va.setDuration(150);
					va.setInterpolator(new AccelerateDecelerateInterpolator());
					va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							Float value = (Float) animation.getAnimatedValue();
							btnAdd.setAlpha(0.8f - value);
							btnAdd.setScaleX(1.4f - value);
							btnAdd.setScaleY(1.4f - value);

							btnSend.setAlpha(value);
							btnSend.setScaleX(value);
							btnSend.setScaleY(value);

							btnAdd.setVisibility(View.VISIBLE);
							btnSend.setVisibility(View.VISIBLE);

							if (value >= 0.95f) {
								btnAdd.setVisibility(View.INVISIBLE);
								btnAdd.setAlpha(1);
								btnAdd.setScaleX(1);
								btnAdd.setScaleY(1);
								btnSend.setVisibility(View.VISIBLE);
							}
						}
					});
					va.start();

				} else if (text.length() <= 0
						&& btnAdd.getVisibility() != View.VISIBLE) {
					ValueAnimator va = ValueAnimator.ofFloat(0.4f, 1.0f);
					va.setDuration(150);
					va.setInterpolator(new AccelerateDecelerateInterpolator());
					va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							Float value = (Float) animation.getAnimatedValue();
							btnSend.setAlpha(0.8f - value);
							btnSend.setScaleX(1.4f - value);
							btnSend.setScaleY(1.4f - value);

							btnAdd.setAlpha(value);
							btnAdd.setScaleX(value);
							btnAdd.setScaleY(value);

							btnAdd.setVisibility(View.VISIBLE);
							btnSend.setVisibility(View.VISIBLE);

							if (value >= 0.95f) {
								btnAdd.setVisibility(View.VISIBLE);
								btnSend.setVisibility(View.INVISIBLE);
							}
						}
					});
					va.start();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		this.etText.setId(R.id.chat_edit);
		etText.setTag("edit");
		this.etText.setBackgroundResource(R.drawable.bg_chat_text);
		int padding = DensityUtil.dip2px(5);
		this.etText.setPadding(padding, padding, padding, padding);
		this.etText.setTextColor(activity.getResources().getColor(
				R.color.txt_dark_gray));
		etText.setMaxLines(4);
		LayoutParams editParams = new LayoutParams(smallWidth,
				LayoutParams.WRAP_CONTENT);
		editParams.leftMargin = intervalPix;
		this.toolbar.addView(etText, editParams);

		// 表情 按钮
		this.btnExpress = new Button(activity);
		this.btnExpress.setBackgroundResource(R.drawable.sel_chat_express);
		LayoutParams expParams = new LayoutParams(iconWidth, iconHeight);
		expParams.leftMargin = intervalPix;
		this.toolbar.addView(btnExpress, expParams);

		// Add图标 发送按钮
		RelativeLayout llPost = new RelativeLayout(activity);
		LayoutParams postParam = new LayoutParams(iconWidth + intervalPix * 2,
				btnHeight);
		this.toolbar.addView(llPost, postParam);

		// add icon
		this.btnAdd = new Button(activity);
		this.btnAdd.setBackgroundResource(R.drawable.sel_chat_add);
		RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(
				iconWidth, iconHeight);
		addParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.btnAdd.setPadding(0, 0, 0, 0);
		llPost.addView(btnAdd, addParams);

		this.btnSend = new Button(activity);
		DensityUtil.setTextSize(btnSend, R.dimen.txt_14);
		this.btnSend.setText("发送");
		this.btnSend.setBackgroundResource(R.drawable.sel_chat_send);
		this.btnSend.setTextColor(Color.WHITE);
		this.btnSend.setPadding(0, 0, 0, 0);
		RelativeLayout.LayoutParams sendParams = new RelativeLayout.LayoutParams(
				btnSendWidth, btnHeight);
		sendParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		llPost.addView(btnSend, sendParams);
		btnSend.setTag("button");

		btnText.setVisibility(View.GONE);
		btnRecord.setVisibility(View.GONE);
		btnSend.setVisibility(View.INVISIBLE);

		btnText.setOnClickListener(this);
		btnAudio.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
	}

	/**
	 * ` 根据当前的attachcontroller 状态设置软键盘模式
	 */
	private void adjustSoftInputMode() {
		// if (attachController.isCollapsed || fullScreenInput) {
		// activity.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// attachController.hide();
		// } else {
		// activity.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// }
	}

	private void initSize() {
		iconWidth = DensityUtil.dip2px(32);
		iconHeight = DensityUtil.dip2px(32);

		btnHeight = iconHeight;
		btnSendWidth = iconWidth + intervalPix;

		smallWidth = totalWidth - iconWidth * 3 - intervalPix * 5;
		largeWidth = smallWidth + iconWidth + intervalPix;
	}

	@Override
	public void onClick(View v) {
		if (v == btnAudio) {
			showAudio();
		} else if (v == btnText || v == etText) {// 展示软键盘输入
			showText(true);
		} else if (v == btnExpress) {// 展示表情
			if (!popAttach.isShowing()) {
				if (isKeyBoardVisible) {
					attachPlaceHolder.setVisibility(LinearLayout.GONE);
				} else {
					attachPlaceHolder.setVisibility(LinearLayout.VISIBLE);
				}
				int h = PrefUtil.Instance().getInputHeight();
				if (h > 0)
					popAttach.setHeight(h);
				attachController.showExpress();
				popAttach.showAtLocation(attachPlaceHolder, Gravity.BOTTOM, 0,
						0);
			} else {
				if (attachController.isAttachShowing()) {
					attachController.showExpress();
				} else {
					popAttach.dismiss();
				}
			}
			this.handler.sendEmptyMessageAtTime(
					FragChatViewController.POP_SHOW,
					SystemClock.uptimeMillis() + 100);
		} else if (v == btnAdd) {// 展示attach
			if (!popAttach.isShowing()) {
				if (isKeyBoardVisible) {
					attachPlaceHolder.setVisibility(LinearLayout.GONE);
				} else {
					attachPlaceHolder.setVisibility(LinearLayout.VISIBLE);
				}
				int h = PrefUtil.Instance().getInputHeight();
				if (h > 0)
					popAttach.setHeight(h);
				attachController.showAttach();
				popAttach.showAtLocation(attachPlaceHolder, Gravity.BOTTOM, 0,
						0);
				this.handler.sendEmptyMessageAtTime(
						FragChatViewController.POP_SHOW, 55);
			} else {
				if (!attachController.isAttachShowing()) {
					attachController.showAttach();
				} else {
					popAttach.dismiss();
				}
			}
			this.handler.sendEmptyMessageAtTime(
					FragChatViewController.POP_SHOW,
					SystemClock.uptimeMillis() + 100);
		} else if (v == btnSend) {// 发送
			String s = etText.getText().toString();
			if (StringUtil.isNullOrEmpty(s))
				return;
			etText.setText(null);
			controller.onSendText(s);
			this.handler.sendEmptyMessageAtTime(
					FragChatViewController.POP_SHOW,
					SystemClock.uptimeMillis() + 100);
		}
	}

	public boolean collapse() {
		if (!attachController.isCollapsed) {
			attachController.hide();
			inputType = INPUT_TYPE_NONE;
			return true;
		}
		return false;
	}

	public boolean collapseAll() {
		if (popAttach.isShowing()) {
			popAttach.dismiss();
			return true;
		}
		return false;
	}

	public boolean isCollapsed() {
		return attachController.isCollapsed;
	}

	private void showAudio() {
		attachController.hide();
		hideSoftIput();
		btnSend.setVisibility(View.INVISIBLE);
		btnAudio.setVisibility(View.GONE);
		btnExpress.setVisibility(View.GONE);
		etText.setVisibility(View.GONE);
		btnText.setVisibility(View.VISIBLE);
		btnRecord.setVisibility(View.VISIBLE);
		btnAdd.setVisibility(View.VISIBLE);
	}

	protected void showText(boolean isShowInput) {
		btnText.setVisibility(View.GONE);
		btnRecord.setVisibility(View.GONE);
		btnAudio.setVisibility(View.VISIBLE);
		btnExpress.setVisibility(View.VISIBLE);
		etText.setVisibility(View.VISIBLE);
		if (etText.getText().toString().trim().length() > 0) {
			btnAdd.setVisibility(View.INVISIBLE);
			btnSend.setVisibility(View.VISIBLE);
		} else {
			btnAdd.setVisibility(View.VISIBLE);
			btnSend.setVisibility(View.INVISIBLE);
		}
		if (popAttach.isShowing())
			popAttach.dismiss();

		if (isShowInput) {
			showInput();
		} else {
			hideSoftIput();
		}
	}

	private void showInput() {
		inputType = INPUT_TYPE_SOFT;
		adjustSoftInputMode();
		etText.requestFocus();
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
				etText, 0);
	}

	protected void hideSoftIput() {
		inputType = INPUT_TYPE_NONE;
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(etText.getWindowToken(), 0);
	}

	public void setExpressInterceptListener(InterceptListener interceptListner) {
		attachController.setExpressInterceptListener(interceptListner);
	}

	// ===========audio==============

	@Override
	public void onAudioFileSelected(String path, int time) {
		audioContainer.setVisibility(View.GONE);
		File file = new File(path);
		if (file.exists()) {
			controller.onSendAudio(path, time);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			btnRecord.setText(RECORD);
			btnRecord.setSelected(false);
			audioView.cancelRecord();
			break;
		case MotionEvent.ACTION_DOWN:
			audioContainer.setVisibility(View.VISIBLE);
			btnRecord.setText(FINISH_RECORD);
			btnRecord.setSelected(true);
			controller.onStartRecordAudio();
			audioView.startRecording();
			break;
		case MotionEvent.ACTION_MOVE: {
			Rect rect = new Rect();
			float x = event.getRawX();
			float y = event.getRawY();

			btnRecord.getGlobalVisibleRect(rect);

			MLog.d(TAG, "rect:(" + rect.left + "," + rect.top + ","
					+ rect.right + "," + rect.bottom + ")");
			MLog.d(TAG, "raw: (" + event.getRawX() + "," + event.getRawY()
					+ ")");

			if (y < rect.top - DensityUtil.dip2px(80)) {
				audioView.prepareCancel();
			} else {
				audioView.gobackCapture();
			}
		}
			break;
		}
		return true;
	}

	public void setVisibility(int visibility) {
		toolbar.setVisibility(visibility);
	}

	public static interface HideOther {
		void hideToolsbar();
	}

}
