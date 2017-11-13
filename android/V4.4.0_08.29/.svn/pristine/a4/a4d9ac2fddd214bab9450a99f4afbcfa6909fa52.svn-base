package com.zhisland.android.blog.im.view;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;

/**
 * 
 * @author
 * 
 */
public class AudioRecorder {

	MediaRecorder recorder;
	private String path;
	public static final int MAX_DURATION = 60 * 1000;
	public static final int MIN_DURATION = 2 * 1000;
	private static final String TAG = "audiorecorder";

	public AudioRecorder(String path) throws Exception {
		recorder = new MediaRecorder();
		if (path != null && path.length() > 0) {
			this.path = sanitizePath(path);
		} else {
			this.path = this.defaultPath();
		}
	}

	public AudioRecorder() throws Exception {
		recorder = new MediaRecorder();
		this.path = this.defaultPath();
	}

	private String defaultPath() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String audioName = UUID.randomUUID().toString() + ".amr";

			File tmpDir = FileMgr.Instance().getDir(DirType.TMP);
			this.path = tmpDir.getAbsolutePath() + File.separator + audioName;

			return path;
		} else {
//			ToastUtil.showShort("请插入SD卡");
			return null;
		}

	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".amr";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ path;
	}

	public void init() {

		try {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(path);
			recorder.setMaxDuration(MAX_DURATION);
		} catch (IllegalStateException e) {
			MLog.e(TAG, e.getMessage(), e);
		}
	}

	public void start() throws Exception {
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted,It is  " + state
					+ ".");
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created");
		}
		init();
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			recorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() throws IOException {
		if (recorder != null) {
			try {
				recorder.stop();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setOnInfoListener(OnInfoListener listener) {
		if (recorder != null) {
			recorder.setOnInfoListener(listener);
		}
	}

	public String getPath() {
		return path;
	}

	public void release() {
		if (recorder != null) {
			recorder.release();
			recorder = null;
		}
	}

	public void reset() throws IllegalStateException, IOException {
		if (recorder != null) {
			// recorder.reset();
			// init();
			// recorder.prepare();
			// recorder.start();
		}
	}

	public int getMaxAmplitude() {
		if (recorder != null) {
			try {
				return recorder.getMaxAmplitude();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public MediaRecorder getRecorder() {
		return recorder;
	}
}
