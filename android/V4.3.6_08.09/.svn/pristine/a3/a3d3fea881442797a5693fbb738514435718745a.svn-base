package com.zhisland.lib.load;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

public class BaseLoadDao<T extends BaseLoadInfo> extends BaseDaoImpl<T, Long> {

	private static final String TAG = "loaddao";

	public void resetData() {
		MLog.d(TAG, "reset loading status data to error");
		UpdateBuilder<T, Long> builder = this.updateBuilder();
		try {
			builder.where().eq(BaseLoadInfo.COL_STATUS, LoadStatus.LOADING);
			builder.updateColumnValue(BaseLoadInfo.COL_STATUS, LoadStatus.ERROR);
			int i = builder.update();
			MLog.d(TAG, "reset " + i + " download items");
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
		}
	}

	public List<Long> fetchLoadInfo(int count, ArrayList<Long> curLoads) {
		QueryBuilder<T, Long> builder = this.queryBuilder();

		MLog.d(TAG, "fetch load infos");
		try {
			builder.where().eq(BaseLoadInfo.COL_STATUS, LoadStatus.WAIT);
			builder.orderBy(BaseLoadInfo.COL_TOKEN, true);
			builder.limit((long) count);
			List<T> res = builder.query();
			if (res == null || res.size() < 1) {
				return null;
			}
			List<Long> tokens = new ArrayList<Long>();
			for (BaseLoadInfo info : res) {
				tokens.add(info.token);
			}
			return tokens;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return null;
		}
	}

	public T getLoadInfo(long token) {
		MLog.d(TAG, "get load info " + token);
		try {
			T info = this.queryForId(token);
			return info;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	public long insertLoadInfo(T info) {

		try {
			MLog.d(TAG, "insert load info " + info.toString());
			this.create(info);
			return info.token;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
		}
		return -1;
	}

	public int updateLoadInfo(T info) {

		if (info == null)
			return -1;
		MLog.d(TAG, "update load info " + info.toString());
		try {
			int i = super.update(info);
			return i;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
		}
		return -1;
	}

	/**
	 * when uid < 0 , stop all
	 * 
	 * @param uid
	 * @return
	 */
	public int stopLoad(long uid) {
		MLog.d(TAG, "stop with " + uid);
		UpdateBuilder<T, Long> builder = this.updateBuilder();
		try {
			if (uid >= 0) {
				builder.where().eq(BaseLoadInfo.COL_OWNER_ID, uid);
			}
			builder.updateColumnValue(BaseLoadInfo.COL_STATUS, LoadStatus.ERROR);
			int updateCount = builder.update();
			return updateCount;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return -1;
		}
	}

	/**
	 * when uid < 0 , delete all
	 * 
	 * @param uid
	 * @return
	 */
	public int deleteLoad(long uid) {
		MLog.d(TAG, "delete with uid " + uid);
		QueryBuilder<T, Long> builder = this.queryBuilder();
		try {
			if (uid >= 0) {
				builder.where().eq(BaseLoadInfo.COL_OWNER_ID, uid);
			}
			List<T> infos = builder.query();

			DeleteBuilder<T, Long> delBuilder = this.deleteBuilder();
			if (uid >= 0) {
				delBuilder.where().eq(BaseLoadInfo.COL_OWNER_ID, uid);
			}
			int delCount = delBuilder.delete();

			if (infos != null) {
				for (BaseLoadInfo info : infos) {
					if (StringUtil.isNullOrEmpty(info.filePath)) {
						continue;
					}
					try {
						File file = new File(info.filePath);
						if (file.exists()) {
							file.delete();
						}
					} catch (Exception ex) {
					}
				}
			}

			return delCount;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return -1;
		}
	}

	/**
	 * 删除任务跟token
	 * 
	 * @param token
	 * @return
	 */
	public int deleteByToken(long token) {
		MLog.d(TAG, "delete with token " + token);
		try {
			QueryBuilder<T, Long> builder = this.queryBuilder();
			builder.where().eq(BaseLoadInfo.COL_TOKEN, token);
			T info = builder.queryForFirst();

			DeleteBuilder<T, Long> delBuilder = this.deleteBuilder();
			delBuilder.where().eq(BaseLoadInfo.COL_TOKEN, token);
			int delCount = delBuilder.delete();

			if (info != null && !StringUtil.isNullOrEmpty(info.filePath)) {
				try {
					File file = new File(info.filePath);
					if (file.exists()) {
						file.delete();
					}
				} catch (Exception ex) {
					MLog.e(TAG, ex.getMessage(), ex);
				}
			}

			return delCount;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return -1;
		}
	}

	public int updateLoadStatus(long token, int status) {
		MLog.d(TAG,
				String.format("update token %d with status %d", token, status));
		UpdateBuilder<T, Long> builder = this.updateBuilder();
		try {
			builder.where().eq(BaseLoadInfo.COL_TOKEN, token);
			builder.updateColumnValue(BaseLoadInfo.COL_STATUS, status);
			int i = builder.update();
			return i;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return -1;
		}

	}

	public int updateLoadStatusByOwner(long ownerId, int status) {
		MLog.d(TAG,
				String.format("update uid %d with status %d", ownerId, status));
		UpdateBuilder<T, Long> builder = this.updateBuilder();
		try {
			builder.where().eq(BaseLoadInfo.COL_OWNER_ID, ownerId);
			builder.updateColumnValue(BaseLoadInfo.COL_STATUS, status);
			int i = builder.update();
			return i;
		} catch (SQLException e) {
			MLog.e(TAG, e.getMessage(), e);
			return -1;
		}

	}

	// ===============contructors===============
	public BaseLoadDao(Class<T> dataClass) throws SQLException {
		super(dataClass);
	}

	public BaseLoadDao(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	public BaseLoadDao(ConnectionSource connectionSource,
			DatabaseTableConfig<T> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

}
