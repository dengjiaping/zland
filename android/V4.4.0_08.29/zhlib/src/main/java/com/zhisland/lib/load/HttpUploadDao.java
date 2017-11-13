package com.zhisland.lib.load;

import java.sql.SQLException;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class HttpUploadDao extends BaseLoadDao<HttpUploadInfo> {

	private static final String TAG = "httpupload";

	public int update(long token, int finishedBlock) {
		UpdateBuilder<HttpUploadInfo, Long> builder = this.updateBuilder();
		try {
			builder.where().eq(HttpUploadInfo.COL_TOKEN, token);
			builder.updateColumnValue(HttpUploadInfo.COL_CUR_BLOCK,
					finishedBlock);
			int i = builder.update();
			return i;
		} catch (SQLException e) {
			return -1;
		}

	}

	// ===============contructors===============
	public HttpUploadDao(Class<HttpUploadInfo> dataClass) throws SQLException {
		super(dataClass);
	}

	public HttpUploadDao(ConnectionSource connectionSource,
			Class<HttpUploadInfo> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public HttpUploadDao(ConnectionSource connectionSource,
			DatabaseTableConfig<HttpUploadInfo> tableConfig)
			throws SQLException {
		super(connectionSource, tableConfig);
	}

}
