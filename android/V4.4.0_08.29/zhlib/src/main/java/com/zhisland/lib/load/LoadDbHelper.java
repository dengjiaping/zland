package com.zhisland.lib.load;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhisland.lib.R;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 */
public class LoadDbHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "zhisland_load_new.db";
	private static final int VER_20140314 = 20;
	private static final int DATABASE_VERSION = VER_20140314;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private static final String TAG = "loaddb";

	private HttpUploadDao httpUpDao;
	private HttpDownloadDao httpDownDao;
	private UploadMultiDao upmultiDao;

	private static LoadDbHelper helper = null;

	public static LoadDbHelper getHelper() {
		return getHelper(ZHApplication.APP_CONTEXT);
	}

	/**
	 * Get the helper, possibly constructing it if necessary. For each call to
	 * this method, there should be 1 and only 1 call to {@link #close()}.
	 */
	public static synchronized LoadDbHelper getHelper(Context context) {
		if (helper == null) {
			helper = new LoadDbHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	public static synchronized void release() {
		if (helper != null) {
			helper.close();
			helper = null;
		}
	}

	/**
	 * Close the database connections and clear any cached DAOs. For each call
	 * to {@link #getHelper(Context)}, there should be 1 and only 1 call to this
	 * method. If there were 3 calls to {@link #getHelper(Context)} then on the
	 * 3rd call to this method, the helper and the underlying database
	 * connections will be closed.
	 */
	@Override
	public void close() {
		if (usageCounter.decrementAndGet() == 0) {
			super.close();
			helper = null;
			upmultiDao = null;
			httpDownDao = null;
			httpUpDao = null;
		}
	}

	private LoadDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config_load_new);
		this.getHttpDownDao().resetData();
		this.getHttpUpDao().resetData();
		this.getUpmultiDao().resetData();
	}

	/************************************************
	 * Suggested Copy/Paste Done
	 ************************************************/

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, HttpUploadInfo.class);
			TableUtils.createTable(connectionSource, HttpDownloadInfo.class);
			TableUtils.createTable(connectionSource, UploadMultiInfo.class);
		} catch (SQLException e) {
			MLog.e(TAG, "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, HttpUploadInfo.class, true);
			TableUtils
					.dropTable(connectionSource, HttpDownloadInfo.class, true);
			TableUtils.dropTable(connectionSource, UploadMultiInfo.class, true);

		} catch (SQLException e) {
			MLog.e(TAG, "Unable to upgrade database from version " + oldVer
					+ " to new " + newVer, e);
		}

		onCreate(sqliteDatabase, connectionSource);

	}

	public UploadMultiDao getUpmultiDao() {
		if (upmultiDao == null) {
			try {
				upmultiDao = getDao(UploadMultiInfo.class);
			} catch (SQLException e) {
				MLog.d(TAG, e.getMessage());
			}
		}
		return upmultiDao;
	}

	public HttpUploadDao getHttpUpDao() {
		if (httpUpDao == null) {
			try {
				httpUpDao = getDao(HttpUploadInfo.class);
			} catch (SQLException e) {
				MLog.d(TAG, e.getMessage());
			}
		}
		return httpUpDao;
	}

	public HttpDownloadDao getHttpDownDao() {
		if (httpDownDao == null) {
			try {
				httpDownDao = getDao(HttpDownloadInfo.class);
			} catch (SQLException e) {
				MLog.d(TAG, e.getMessage());
			}
		}
		return httpDownDao;
	}

}
