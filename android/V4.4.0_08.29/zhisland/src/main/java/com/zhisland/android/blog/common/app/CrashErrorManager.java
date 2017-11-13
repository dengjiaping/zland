package com.zhisland.android.blog.common.app;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;

import com.zhisland.lib.component.application.UEHandler;

public class CrashErrorManager {

	public static void trySendErrorlog(Context context) {
		File file = new File(context.getFilesDir(), UEHandler.fileName);
		if (file.exists()) {
			String content = null;
			FileInputStream fis = null;
			try {
				byte[] data = new byte[(int) file.length()];
				fis = new FileInputStream(file);
				fis.read(data);
				content = new String(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
					if (file.exists()) {
						file.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (content != null) {
				// TODO send crash report
				// ZHBlogEngineFactory.getZHIslandEngineAPI().sendException(
				// PreferenceUtil.getUserID(), content);
			}
		}
	}
}
