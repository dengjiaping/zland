package com.zhisland.android.blog.im.view;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import com.zhisland.lib.util.MLog;

public class AudioPlayer implements OnCompletionListener, OnErrorListener {

	private static final String TAG = "audioplayer";
	private static AudioPlayer _instance = null;
	private static final Object lockObj = new Object();

	private final MediaPlayer player;
	private String curFilepath = null;
	private AudioListener listener = null;

	public static AudioPlayer instance() {
		if (_instance == null) {
			synchronized (lockObj) {
				if (_instance == null) {
					_instance = new AudioPlayer();
				}
			}
		}
		return _instance;
	}

	private AudioPlayer() {
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public void play(String filePath, AudioListener listener) {
		try {
			if (player.isPlaying()) {
				reset();
			}
			this.curFilepath = filePath;
			this.listener = listener;

			player.setDataSource(curFilepath);
			player.prepare();
			player.start();

		} catch (IllegalArgumentException e) {
			MLog.e(TAG, e.getMessage(), e);
			if (listener != null) {
				listener.onError(player, 0, 0);
			}
		} catch (IllegalStateException e) {
			MLog.e(TAG, e.getMessage(), e);
			if (listener != null) {
				listener.onError(player, 0, 0);
			}
		} catch (IOException e) {
			MLog.e(TAG, e.getMessage(), e);
			if (listener != null) {
				listener.onError(player, 0, 0);
			}
		}
	}

	public void removeListener(AudioListener listener) {
		if (listener != null && this.listener == listener) {
			synchronized (this) {
				this.listener = null;
			}
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (listener != null) {
			listener.onError(mp, what, extra);
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		this.curFilepath = null;
		player.stop();
		player.reset();
		if (this.listener != null) {
			this.listener.onCompletion(mp);
		}
	}

	private void reset() {
		this.curFilepath = null;
		if (this.listener != null) {
			this.listener.onCompletion(player);
		}
		this.listener = null;
		player.stop();
		player.reset();
	}

	public void stop() {
		try {
			if (player.isPlaying()) {
				reset();
			}
		} catch (Exception ex) {
            ex.printStackTrace();
		}
	}

}
