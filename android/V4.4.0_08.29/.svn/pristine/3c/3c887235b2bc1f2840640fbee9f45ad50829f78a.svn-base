package com.zhisland.lib.load;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class HttpDownloadDao extends BaseLoadDao<HttpDownloadInfo> {

	private static final String TAG = "download";
 
	// ===============contructors===============
	public HttpDownloadDao(Class<HttpDownloadInfo> dataClass)
			throws SQLException {
		super(dataClass);
	}

	public HttpDownloadDao(ConnectionSource connectionSource,
			Class<HttpDownloadInfo> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public HttpDownloadDao(ConnectionSource connectionSource,
			DatabaseTableConfig<HttpDownloadInfo> tableConfig)
			throws SQLException {
		super(connectionSource, tableConfig);
	}

}
