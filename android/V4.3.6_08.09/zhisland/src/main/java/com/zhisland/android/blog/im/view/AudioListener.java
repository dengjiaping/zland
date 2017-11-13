package com.zhisland.android.blog.im.view;

import android.media.MediaPlayer;

public interface AudioListener {

	boolean onError(MediaPlayer mp, int what, int extra);

	void onCompletion(MediaPlayer mp);
}
