package com.zhisland.lib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

public class MLog {

	private final static String TAG = "MLog";

	private static boolean enableLog = true;
	private static boolean enableLogToFile = true;
	public static Level logLevel = Level.Verbose;
	private static ArrayList<String> logs = new ArrayList<String>();

	private static String logPath = null;
	private static PrintStream stream = null;

	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat simpleDateFormater = new SimpleDateFormat(
			"yyyyMMdd", Locale.getDefault());

	private static void flushLogsToFile() {
		if (StringUtil.isNullOrEmpty(logPath))
			return;
		
		Writer writer = null;
		try {
			if (!Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {

			} else {
				File logDir = new File(logPath);

				if (!logDir.exists()) {
					logDir.mkdirs();
				}
				Date date = new Date(System.currentTimeMillis());
				String todayLogName = simpleDateFormater.format(date) + ".txt";
				final File logFile = new File(logDir, todayLogName);
				if (!logFile.exists()) {
					logFile.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(logFile, true));
				for (String logString : logs) {
					writer.append(logString);
					writer.append('\n');
				}
				writer.flush();
			}

		} catch (final Exception ex) {
			Log.e(MLog.TAG, "Error in creating Log File: " + ex.getMessage(),
					ex);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {

			}
		}
	}

	private static void println(final Level level, final String tag,
			final String message, final Throwable exception) {
		if (!MLog.enableLogToFile) {
			return;
		}

		if (MLog.logLevel.value <= level.value) {
			try {
				final Date now = new Date(System.currentTimeMillis());
				Writer stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);

				printWriter.println(MLog.dateFormater.format(now) + "\t"
						+ level + "\t" + tag + "\t" + message);

				printWriter.println();

				if (exception != null) {
					printWriter.println(exception.getMessage());
					exception.printStackTrace(printWriter);

				}

				String s = stringWriter.toString();
				logs.add(s);

				if (logs.size() > 10) {
					flushLogsToFile();
					logs.clear();
				}

			} catch (final Exception ex) {

			}
		}
	}

	public static void d(final String tag, final String msg) {
		if (enableLog) {
			Log.d(tag, msg);
		}
		if (enableLogToFile) {
			MLog.println(Level.Debug, tag, msg, null);
		}
	}

	public static void d(final String tag, final String msg, final Throwable tr) {
		if (enableLog) {
			Log.d(tag, msg, tr);
		}
		MLog.println(Level.Debug, tag, msg, tr);
	}

	public static void e(final String tag, final String msg) {
		if (enableLog) {
			Log.e(tag, msg);
		}
		if (enableLogToFile) {
			MLog.println(Level.Error, tag, msg, null);
		}
	}

	public static void e(final String tag, final String msg, final Throwable tr) {
		if (enableLog) {
			Log.e(tag, msg, tr);
		}
		if (enableLogToFile) {
			MLog.println(Level.Error, tag, msg, tr);
		}
	}

	public static void i(final String tag, final String msg) {
		if (enableLog) {
			Log.i(tag, msg);
		}
		if (enableLogToFile) {
			MLog.println(Level.Information, tag, msg, null);
		}
	}

	public static void i(final String tag, final String msg, final Throwable tr) {
		if (enableLog) {
			Log.i(tag, msg, tr);
		}
		if (enableLogToFile) {
			MLog.println(Level.Information, tag, msg, tr);
		}
	}

	public static void close() {
		if (MLog.stream != null) {
			try {
				MLog.stream.flush();
				MLog.stream.close();
			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage(), ex);
			}
		}
	}

	public static void v(final String tag, final String msg) {
		if (enableLog) {
			Log.v(tag, msg);
		}
		if (enableLogToFile) {
			MLog.println(Level.Verbose, tag, msg, null);
		}
	}

	public static void v(final String tag, final String msg, final Throwable tr) {
		if (enableLog) {
			Log.v(tag, msg, tr);
		}
		if (enableLogToFile) {
			MLog.println(Level.Verbose, tag, msg, tr);
		}
	}

	public static void w(final String tag, final String msg) {
		if (enableLog) {
			Log.w(tag, msg);
		}
		if (enableLogToFile) {
			MLog.println(Level.Warning, tag, msg, null);
		}
	}

	public static void w(final String tag, final String msg, final Throwable tr) {
		if (enableLog) {
			Log.w(tag, msg, tr);
		}
		if (enableLogToFile) {
			MLog.println(Level.Warning, tag, msg, tr);
		}
	}

	public static void w(String tag, Throwable ex) {
		if (enableLog) {
			Log.w(tag, ex);
		}
		if (enableLogToFile) {
			MLog.println(Level.Warning, tag, null, ex);
		}
	}

	public static enum Level {
		Verbose(1), Debug(2), Information(3), Warning(4), Error(5);

		protected int value;

		private Level(final int value) {
			this.value = value;
		}
	}

	public static void setStatus(boolean enablelog, boolean enableLogToFile) {
		MLog.enableLog = enablelog;
		MLog.enableLogToFile = enableLogToFile;
	}

	public static void setLogPath(String logPath) {
		MLog.logPath = logPath;
	}

}