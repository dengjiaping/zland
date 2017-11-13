package com.zhisland.android.blog.common.upapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.ToastUtil;

public final class UpdateUtil {

	/** 记录自动升级时选择忽略的时间 */
	public static final String CLIENT_UPDATE_IGNORE_TIME = "client_update_ignore_time";

	/** 记录客户端升级下载的安装包本地地址 */
	public static final String CLIENT_UPDATE_APK_PATH = "client_update_apk_path";
	/** 记录客户端升级下载的安装包对应的版本号 */
	public static final String CLIENT_UPDATE_APK_VCODE = "client_update_apk_vcode";

	/**
	 * 取消快速安装时在状态栏显示的正在安装提示。 同时也用于取消下载完成和下载失败的notification,因其使用同一个id.
	 * 
	 * @param context
	 *            context
	 * @param appKey
	 *            appKey
	 */
	public static void cancelSilentInstallingNotification(Context context,
			String appKey) {
		if (!TextUtils.isEmpty(appKey)) {
			NotificationManager notifyManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notifyManager.cancel(appKey.hashCode());
		}
	}

	/**
	 * 通过解析APk文件包，获取AndroidManifest.xml，来判断是否是正常的APK文件。如果找到则认为是正常的，否则认为是错误的。
	 * 
	 * @param filename
	 *            文件名字
	 * @return true表示正常,false 表示不正常。
	 */
	public static boolean isAPK(String filename) {

		boolean isApk = false;
		FileInputStream fi = null;
		ZipInputStream zin = null;
		try {
			if (!TextUtils.isEmpty(filename) && (new File(filename).exists())) {
				fi = new FileInputStream(filename);
			} else {
				return false;
			}
			zin = new ZipInputStream(fi);
			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null) {
				if (entry.isDirectory()
						|| entry.getName().equals("AndroidManifest.xml")) {

					isApk = true;
					break;
				}
			}
		} catch (IOException e) {
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (Exception ex) {
				}
			}
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
				} finally {
				}
			}
		}
		return isApk;
	}

	/**
	 * 记录本次自动升级时间
	 * 
	 * @param ctx
	 *            Context
	 * @param time
	 *            本次自动升级时间
	 */
	public static void setClientUpdateTime(Context ctx, long time) {
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		Editor edit = preference.edit();
		edit.putLong(CLIENT_UPDATE_IGNORE_TIME, time);
		edit.commit();
	}

	/**
	 * 获得上次升级下载的安装包所在地址
	 * 
	 * @param ctx
	 *            Context
	 * @return String 本地路径
	 */
	public static String[] getClientUpdateApkPathAndVcode(Context ctx) {
		String[] str = new String[] { null, null };
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		str[0] = prefs.getString(CLIENT_UPDATE_APK_PATH, "");
		str[1] = prefs.getString(CLIENT_UPDATE_APK_VCODE, "0");
		return str;
	}

	/**
	 * 记录本次升级下载的安装包本地地址
	 * 
	 * @param ctx
	 *            Context
	 * @param path
	 *            String
	 * @param vcode
	 *            String 版本号
	 */
	public static void setClientUpdateApkPathAndVcode(Context ctx, String path,
			String vcode) {
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		Editor edit = preference.edit();
		edit.putString(CLIENT_UPDATE_APK_PATH, path);
		edit.putString(CLIENT_UPDATE_APK_VCODE, vcode);
		edit.commit();
	}

	/**
	 * 安装客户端更新apk
	 * 
	 * @param ctx
	 *            Context
	 * @param filepath
	 *            String
	 */
	public static void installClientUpdateApk(Context ctx, String filepath) {

		if (!haveSDCard()) {
			ToastUtil.showShort(ctx.getResources().getString(R.string.download_sdcard_busy_dlg_title));
		} else {
			if (filepath == null) {
				return;
			}
			File apkfile = new File(filepath);
			if (apkfile.exists()) {
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType((Uri.fromFile(apkfile)),
						"application/vnd.android.package-archive");
				installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(installIntent);
			} else {
				ToastUtil.showShort(ctx.getResources().getString(R.string.install_no_file_found));
			}
		}
	}

	/**
	 * 是否安装了sdcard。
	 * 
	 * @return true表示有，false表示没有
	 */
	public static boolean haveSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * Get total size of the SD card.
	 * 
	 * @return 获取sd卡的大小
	 */
	public static long getSDCardTotalSize() {
		long total = 0;
		if (haveSDCard()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			long blocSize = statfs.getBlockSize();
			long totalBlocks = statfs.getBlockCount();
			total = totalBlocks * blocSize;
		} else {
			total = -1;
		}
		return total;
	}

	/**
	 * Get available size of the SD card.
	 * 
	 * @return available size
	 */
	public static long getAvailableSize() {
		long available = 0;
		if (haveSDCard()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			long blocSize = statfs.getBlockSize();
			long availaBlock = statfs.getAvailableBlocks();

			available = availaBlock * blocSize;
		} else {
			available = -1;
		}
		return available;
	}

}
