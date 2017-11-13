package com.zhisland.android.blog.common.uri;

import java.util.Locale;

import android.content.Context;
import android.net.Uri;

import com.zhisland.lib.util.StringUtil;

/**
 * uri 的base 类，如果要实现一个新的URI，只需实现此类的一个子类，并调用AUriMgr注册即可
 */
public abstract class AUriBase {

	private String path;

	/**
	 * 浏览uri代表的资源
	 */
	public abstract void viewRes(final Context context, Uri uri, String arg1,
			Object arg2);

	public static long getQuery(Uri uri, String key, long defaultValue) {
		String s = uri.getQueryParameter(key);
		try {
			return Long.parseLong(s.trim());
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	public static String getQuery(Uri uri, String key, String defaultValue) {
		String s = uri.getQueryParameter(key);
		try {
			return s.trim();
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	public static float getQuery(Uri uri, String key, float defaultValue) {
		String s = uri.getQueryParameter(key);
		try {
			return Float.parseFloat(s.trim());
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	protected String getUriString(long id, String name) {
		String resName = getUriPath();
		if (StringUtil.isNullOrEmpty(name)) {
			return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
					+ "://%s/%s?id=%d", AUriMgr.AUTHORITY, resName, id);
		} else {
			return String.format(Locale.getDefault(), AUriMgr.SCHEMA_BLOG()
					+ "://%s/%s?id=%d&name=%s", AUriMgr.AUTHORITY, resName, id,
					Uri.encode(name));
		}
	}

	/**
	 * 返回URI的path部分
	 */
	public final String getUriPath() {
		return path;
	}

	/**
	 * 设置当前的hangler实例处理的是什么path
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
