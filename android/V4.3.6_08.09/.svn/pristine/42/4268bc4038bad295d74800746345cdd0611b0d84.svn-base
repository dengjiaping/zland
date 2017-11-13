package com.zhisland.lib.component.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

public class UEHandler implements Thread.UncaughtExceptionHandler {

	static interface ExceptionSender {
		void sendException(String exception);
	}

	private Context context;
	private ExceptionSender sender;
	private File fileErrorMLog;
	public static final String fileName = "errorlog";

	public UEHandler(Context context, ExceptionSender sender) {

		this.context = context;
		this.sender = sender;
		fileErrorMLog = new File(ZHApplication.APP_CONTEXT.getFilesDir(),
				fileName);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		Log.d(ZHApplication.TAG, "uncatch exception happened");
		MobclickAgent.onKillProcess(context);
		// fetch Excpetion Info
		String info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// print
		long threadId = thread.getId();
		Log.e(ZHApplication.TAG, "Thread.getName()=" + thread.getName()
				+ " id=" + threadId + " state=" + thread.getState());
		Log.e(ZHApplication.TAG, "Error[" + info + "]");

		if (ZHApplication.APP_CONFIG.getEnvType() == EnvType.ENV_RELEASE) {
			if (threadId == 1) {
				write2ErrorMLog(fileErrorMLog, info);
				// kill App Progress
				android.os.Process.killProcess(android.os.Process.myPid());
			} else {
				sender.sendException(info);
			}
		}
	}

	private void write2ErrorMLog(File file, String content) {
		FileOutputStream fos = null;
		try {
			if (file.exists()) {
				// 娓呯┖涔嬪墠鐨勮褰�
				file.delete();
			} else {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
