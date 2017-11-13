package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.android.blog.im.view.adapter.ImSessAdapter.AudioMgr;
import com.zhisland.im.data.IMMessage;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.util.DensityUtil;

public class RowAudio extends BaseRowView {

	private final int MAX_LENGTH = BaseRowView.MAX_LENGTH
			- DensityUtil.dip2px(35);
	private final int STATUS_WIDTH = DensityUtil.dip2px(5);
	private TextView tvTime;
	protected ImageView ivReadStatus;
	protected ImageView iv;
	private final LayoutParams statusParam;
	private final LinearLayout.LayoutParams ivParam;

	protected AudioMgr audioMgr;

	public RowAudio(Context context) {
		super(context, CONTENT_TYPE_NORMAL);
		statusParam = new LayoutParams(STATUS_WIDTH, STATUS_WIDTH);
		statusParam.leftMargin = STATUS_WIDTH;
		statusParam.rightMargin = STATUS_WIDTH;
		ivParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	public void setAudioMgr(AudioMgr audioMgr) {
		this.audioMgr = audioMgr;
	}

	public void refreshPlayStatus(IMMessage msgPlaying) {
		if (msg == msgPlaying) {
			startAnimation();
		} else {
			stopAnimation();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == iv) {
			IMMessage oldMsg = audioMgr.curMsg;
			audioMgr.stop();
			if (oldMsg != msg) {
				audioMgr.playAudio(msg);
				if (msg.isRead) {
					ivReadStatus.setVisibility(View.GONE);
				} else {
					ivReadStatus.setVisibility(View.VISIBLE);
				}
			}

		} else {
			super.onClick(v);
		}
	}

	@Override
	public void performOnClick() {
		iv.performClick();
	}

	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {

		container.setGravity(Gravity.CENTER_VERTICAL);

		iv = new ImageView(context);
		ivReadStatus = new ImageView(context);
		ivReadStatus.setImageResource(R.drawable.bg_num);
		tvTime = new TextView(context);
		int padding = DensityUtil.dip2px(5);
		tvTime.setPadding(padding, 0, padding, 0);
		tvTime.setTextColor(getResources().getColor(R.color.color_f2));
		DensityUtil.setTextSize(tvTime, R.dimen.txt_14);
		iv.setOnClickListener(this);
	}

	@Override
	public void configMyView() {
		super.configMyView();

		BaseRowUtil.cleanSelfOtherBackground(container);
		container.removeAllViews();
		container.addView(tvTime, 0);
		container.addView(ivReadStatus, 0, statusParam);
		container.addView(iv, ivParam);

		BaseRowUtil.configSelfBackground(iv, contentType);
		iv.setScaleType(ScaleType.FIT_END);
		iv.setImageResource(R.drawable.audio_play_my3);
	}

	@Override
	public void configOtherView() {
		super.configOtherView();

		container.setBackgroundDrawable(null);
		container.setPadding(0, 0, 0, 0);
		container.removeAllViews();
		container.addView(iv, ivParam);
		container.addView(tvTime);
		container.addView(ivReadStatus, statusParam);

		BaseRowUtil.configOtherBackground(iv, contentType);
		iv.setScaleType(ScaleType.FIT_START);
		iv.setImageResource(R.drawable.audio_play_their3);
	}

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);

		if (msg == null)
			return;

		fillMenu(iv);

		tvTime.setText(msg.duration + "\"");
		int width = (int) (DensityUtil.dip2px(65) + msg.duration
				* DensityUtil.dip2px(5));
		ivParam.width = width > MAX_LENGTH ? MAX_LENGTH : width;

		if (audioMgr != null) {
			refreshPlayStatus(audioMgr.curMsg);
		}

		if (msg.isRead) {
			ivReadStatus.setVisibility(View.GONE);
		} else {
			ivReadStatus.setVisibility(View.VISIBLE);
		}
		refresh(msg.status, msg.progress);
	}

	@Override
	public void recycle() {
		super.recycle();
		cleanMenu(iv);
	}

	public void startAnimation() {
		//TODO 如何解决音频的动画
		if (msg.direction == Constants.MSG_INCOMING) {
			iv.setImageResource(R.drawable.audio_play_their);
			final AnimationDrawable audioAnimation = (AnimationDrawable) iv
					.getDrawable();
			audioAnimation.start();
		} else {
			iv.setImageResource(R.drawable.audio_play_my);
			final AnimationDrawable audioAnimation = (AnimationDrawable) iv
					.getDrawable();
			audioAnimation.start();
		}
	}

	public void stopAnimation() {
		if (msg.direction == Constants.MSG_INCOMING) {
			Object obj = iv.getDrawable();
			if (!(obj instanceof AnimationDrawable))
				return;
			final AnimationDrawable audioAnimation = (AnimationDrawable) iv
					.getDrawable();
			if (audioAnimation != null && audioAnimation.isRunning()) {
				audioAnimation.stop();
			}
			iv.setImageResource(R.drawable.audio_play_their3);
		} else {
			Object obj = iv.getDrawable();
			if (!(obj instanceof AnimationDrawable))
				return;
			final AnimationDrawable audioAnimation = (AnimationDrawable) iv
					.getDrawable();
			if (audioAnimation != null && audioAnimation.isRunning()) {
				audioAnimation.stop();
			}
			iv.setImageResource(R.drawable.audio_play_my3);
		}
	}

	@Override
	public void refresh(int status, int progress) {
		if (msg.direction == Constants.MSG_INCOMING && !msg.isRead) {
			ivReadStatus.setVisibility(View.VISIBLE);
		} else {
			ivReadStatus.setVisibility(View.GONE);
		}
		switch (status) {
		case Constants.MSG_STATUS_ERROR: {
			ivError.setVisibility(View.VISIBLE);
			pbSending.setVisibility(View.GONE);
			break;
		}
		case Constants.MSG_STATUS_SENDING: {
			pbSending.setVisibility(View.GONE);
			ivError.setVisibility(View.GONE);
			break;
		}
		case Constants.MSG_STATUS_NORMAL:
		default: {
			pbSending.setVisibility(View.GONE);
			ivError.setVisibility(View.GONE);
			break;
		}
		}
	}

}
