package com.zhisland.android.blog.common.webview;

import java.io.File;

import com.zhisland.lib.view.web.WebviewDataListener;
import com.zhisland.lib.view.web.ZHWebviewCache;

public class DefaultDataListener implements WebviewDataListener {

	private static DefaultDataListener instance = new DefaultDataListener();

	private DefaultDataListener() {

	}

	public static DefaultDataListener instance() {
		return instance;
	}

	@Override
	public File getCacheDir() {
//		 return WebviewUtil.getCacheDir();
		return null;
	}

	@Override
	public void cleanData() {
		// WebviewUtil.clear();
		return;
	}

	@Override
	public void insertCache(String key, String filePath, long contentLength) {
		// WebviewUtil.insertCache(key, filePath, contentLength);
	}

	@Override
	public ZHWebviewCache getCache(String key) {
		// return WebviewUtil.getCache(key);
		return null;
	}

}
