package com.zhisland.lib.load;

import java.sql.SQLException;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class UploadMultiDao extends BaseLoadDao<UploadMultiInfo> {

	@Override
	public void resetData() {
		Object obj = null;
		try {
			obj = this.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (obj != null) {
			String s = obj.toString();
		}
		super.resetData();
	}

	public UploadMultiInfo getLoadInfoByUploadToken(long uploadToken) {
		QueryBuilder<UploadMultiInfo, Long> builder = this.queryBuilder();

		try {
			builder.where().eq(UploadMultiInfo.COL_UPLOAD_TOKEN, uploadToken);
			UploadMultiInfo res = builder.queryForFirst();
			return res;
		} catch (SQLException e) {
			return null;
		}
	}

	// ===============contructors===============
	public UploadMultiDao(Class<UploadMultiInfo> dataClass) throws SQLException {
		super(dataClass);
	}

	public UploadMultiDao(ConnectionSource connectionSource,
			Class<UploadMultiInfo> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public UploadMultiDao(ConnectionSource connectionSource,
			DatabaseTableConfig<UploadMultiInfo> tableConfig)
			throws SQLException {
		super(connectionSource, tableConfig);
	}

}
