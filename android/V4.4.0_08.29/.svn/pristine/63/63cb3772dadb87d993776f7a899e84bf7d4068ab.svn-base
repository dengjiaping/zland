package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.media.AudioManager;

import com.zhisland.android.blog.common.app.ZhislandApplication;

public class AudioUtil {

	private static AudioUtil mInstance = null;
	private AudioManager audioManager = null;

	public static AudioUtil getInstance() {
		if (mInstance == null) {
			mInstance = new AudioUtil();
		}
		return mInstance;
	}

	protected AudioUtil() {
		audioManager = (AudioManager) ZhislandApplication.APP_CONTEXT
				.getSystemService(Context.AUDIO_SERVICE);
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public void setVolum(int vol) {
		if (audioManager != null) {
			int max = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			if (vol > max || vol < 0) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
			} else {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
			}
		}
	}

	public void setSpeaker(boolean on) {
		if (audioManager != null) {
			audioManager.setSpeakerphoneOn(on);
		}
	}

	public void setMaxSpeaker(boolean on) {
		if (audioManager != null) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
					0);
			audioManager.setSpeakerphoneOn(on);
		}

	}

}
