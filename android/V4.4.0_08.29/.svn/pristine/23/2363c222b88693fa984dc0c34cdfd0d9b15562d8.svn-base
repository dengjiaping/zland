package com.zhisland.android.blog.im.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.async.MyHandler;
import com.zhisland.lib.async.MyHandler.HandlerListener;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.file.FileUtil;

public class AudioCaptureView extends RelativeLayout implements HandlerListener {

	public static interface OnAudioCaptureListener {
		void onAudioFileSelected(String path, int duration);
	}

	private static final int AUDIO_READY = 1001;
	private static final int AUDIO_RECORDING = 1002;
	private static final int AUDIO_COUNT_DOWN = 1003;
	private static final int AUDIO_FINISH = 1004;
	public static final int AUDIO_PRCANCEL = 1005;

	private static final int RECORDER_TIMER_VALUE = 200;
	private static final String TAG = "zhaudio";

	private View viewAll;
	private View viewRecording;
	private View viewCancel;
	private View viewCountDown;
	private View viewShort;
	private TextView tvCountDown;

	private ImageView ivPower;

	private AudioRecorder recorder = null;

	private int status = AUDIO_READY;
	private int lastStatus;
	private boolean isRecording = false;
	private Timer timer;

	private int currentTime = 0;
	private int remainingTime = 0;

	private String audioPath = "";

	private OnAudioCaptureListener listener;

	private final Handler handler = new MyHandler(this);

	public AudioCaptureView(Context context) {
		super(context);
		initAudioView();
	}

	public AudioCaptureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAudioView();
	}

	public AudioCaptureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAudioView();
	}

	@SuppressLint("InflateParams")
	private void initAudioView() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View audioView = inflater.inflate(R.layout.chat_audio_capture_view, null);

		viewAll = audioView.findViewById(R.id.view_all);
		viewRecording = audioView
				.findViewById(R.id.view_audio_capture_recording);
		viewCancel = audioView.findViewById(R.id.view_audio_capture_cancel);
		viewCountDown = audioView.findViewById(R.id.view_audio_countdown);
		tvCountDown = (TextView) audioView.findViewById(R.id.tvCountDown);
		viewShort = audioView.findViewById(R.id.view_audio_short);

		ivPower = (ImageView) audioView.findViewById(R.id.ivPower);

		status = AUDIO_RECORDING;

		refreshView();

		initRecord();

		this.addView(audioView);
	}

	private void initRecord() {
		try {
			recorder = new AudioRecorder();
		} catch (Exception e) {
			recorder = null;
			return;
		}
		if (audioPath.length() == 0) {
			audioPath = recorder.getPath();
		}
		isRecording = false;
	}

	public void startRecording() {
		viewAll.setVisibility(View.VISIBLE);
		if (recorder == null) {
			return;
		}

		if (!isRecording) {
			isRecording = true;
			recorder.setOnInfoListener(new OnInfoListener() {
				@Override
				public void onInfo(MediaRecorder mr, int what, int extra) {

					if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
						status = AUDIO_FINISH;
						cancelRecord();
					}
				}
			});

			try {
				recorder.start();
			} catch (Exception e) {
				MLog.e(TAG, e.getMessage(), e);
			}

			startTimer();
		}
	}

	public void cancelRecord() {
		if (recorder == null) {
			return;
		}

		if (isRecording) {
			isRecording = false;
			recorder.setOnInfoListener(null);
			if (currentTime / 5 * 1000 < AudioRecorder.MIN_DURATION) {
				if (status == AUDIO_PRCANCEL) {
					status = AUDIO_FINISH;
					gone();
				} else {
					status = AUDIO_FINISH;
					showShort();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							gone();
						}
					}, 1000);
				}
				try {
					recorder.stop();
				} catch (Exception e) {
					MLog.e(TAG, e.getMessage(), e);
				}

				reRecord();
				return;
			}
			int duration = currentTime / 5;

			stopTimer();
			audioPath = recorder.getPath();
			try {
				recorder.stop();
				recorder.release();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (listener != null && status != AUDIO_PRCANCEL) {
				listener.onAudioFileSelected(audioPath, duration);
			} else if (status != AUDIO_PRCANCEL) {
				releaseAllFile();
			}
			status = AUDIO_FINISH;
			refreshView();
			resetView();
		}
	}

	public boolean isInitSuccess() {
		return recorder != null;
	}

	public void setAudioListener(OnAudioCaptureListener l) {
		if (l != null) {
			listener = l;
		}
	}

	public String getRecorderPath() {
		return audioPath;
	}

	public void release() {
		if (recorder != null) {
			recorder.release();
		}
	}

	private void setPeakPower(int power) {
		MLog.d(TAG, "power: " + power);
		power = power / 1000;
		switch (power) {
		case 0:
			ivPower.setVisibility(View.INVISIBLE);
			break;
		case 1:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_i);
			break;
		case 2:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_h);
			break;
		case 3:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_g);
			break;
		case 4:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_f);
			break;
		case 5:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_e);
			break;
		case 6:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_d);
			break;
		case 7:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_c);
			break;
		case 8:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_b);
			break;
		default:
			ivPower.setVisibility(View.VISIBLE);
			ivPower.setImageResource(R.drawable.img_volume_a);
			break;
		}
		// TODO　change power
	}

	private void resetView() {
		releaseAllFile();
		status = AUDIO_RECORDING;

		refreshView();

		initRecord();
	}

	private void startTimer() {
		currentTime = 0;
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}, 0, RECORDER_TIMER_VALUE);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (isRecording) {
			int remainingTime = AudioRecorder.MAX_DURATION / 1000 - currentTime
					/ 5;
			if (remainingTime <= 10) {
				this.remainingTime = remainingTime;
				status = AUDIO_COUNT_DOWN;
			} else {
				setPeakPower(recorder.getMaxAmplitude());
				status = AUDIO_RECORDING;
			}
			refreshView();
			currentTime++;
		}
		return true;
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
		currentTime = 0;
	}

	private void reRecord() {
		stopTimer();
		try {
			recorder.reset();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void releaseAllFile() {
		if (audioPath != null && audioPath.length() > 0) {
			FileUtil.deleteFile(audioPath);
		}
	}

	public void prepareCancel() {
		if (status != AUDIO_PRCANCEL) {
			lastStatus = status;
			status = AUDIO_PRCANCEL;
			refreshView();
		}
	}

	public void gobackCapture() {
		if (status == AUDIO_PRCANCEL) {
			status = lastStatus;
			refreshView();
		}
	}

	private void refreshView() {
		switch (status) {
		case AUDIO_RECORDING: {
			showRecording();
		}
			break;
		case AUDIO_COUNT_DOWN: {
			showCountDown();
		}
			break;
		case AUDIO_PRCANCEL: {
			showCancelRecording();
		}
			break;
		case AUDIO_FINISH: {
			gone();
		}
			break;
		default:
			break;
		}
	}

	private void showRecording() {
		if (!viewRecording.isShown()) {
			viewRecording.setVisibility(View.VISIBLE);
			viewCancel.setVisibility(View.GONE);
			viewCountDown.setVisibility(View.GONE);
			viewShort.setVisibility(View.GONE);
		}
	}

	private void showCancelRecording() {
		if (!viewCancel.isShown()) {
			viewCancel.setVisibility(View.VISIBLE);
			viewRecording.setVisibility(View.GONE);
			viewCountDown.setVisibility(View.GONE);
			viewShort.setVisibility(View.GONE);
		}
	}

	private void showCountDown() {
		if (!viewCountDown.isShown()) {
			viewCountDown.setVisibility(View.VISIBLE);
			viewRecording.setVisibility(View.GONE);
			viewCancel.setVisibility(View.GONE);
			viewShort.setVisibility(View.GONE);
		}
		tvCountDown.setText(String.valueOf(remainingTime));
	}

	private void showShort() {
		if (!viewShort.isShown()) {
			viewShort.setVisibility(View.VISIBLE);
			viewCountDown.setVisibility(View.GONE);
			viewRecording.setVisibility(View.GONE);
			viewCancel.setVisibility(View.GONE);
		}
	}

	private void gone() {
		if (status == AUDIO_FINISH) {
			viewAll.setVisibility(View.GONE);
		}
	}
}