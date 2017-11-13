package com.zhisland.android.blog.profile.dto;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.StringUtil;

@Deprecated
public class UserDeprecatedDao extends BaseDaoImpl<UserDeprecated, Long> {

	ArrayList<String> columnNameList;

	public UserDeprecated getUserById(long uid) {
		try {
			return queryForId(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized ArrayList<String> getColumnwithOutSerializable() {
		if (columnNameList == null) {
			columnNameList = new ArrayList<String>();
			Field[] fields = UserDeprecated.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					DatabaseField databaseField = field
							.getAnnotation(DatabaseField.class);
					if (databaseField != null) {
						String columnName = databaseField.columnName();
						DataType dataType = databaseField.dataType();

						if (!StringUtil.isNullOrEmpty(columnName)) {
							if (!(dataType != null && dataType == DataType.SERIALIZABLE)) {
								columnNameList.add(columnName);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return columnNameList;
	}

	/**
	 * 获取废弃的user，select的列为第一版本的列
	 * */
	public UserDeprecated getUserFirstVersion(long uid) {
		try {
			QueryBuilder<UserDeprecated, Long> qb = this.queryBuilder();
			List<UserDeprecated> query = qb
					.selectColumns(getColumnwithOutSerializable()).where()
					.eq(UserDeprecated.COL_ID, uid).query();
			if (query != null && query.size() > 0) {
				return query.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 查询数据库中当前User对象，如果为空，则使用PrefUtil中当前用户的关键字段，并向接口查询更新user。 如果PrefUtil
	 * 中用户字段为空，则踢出用户
	 */
	public UserDeprecated getSelfUser() {
		long uid = PrefUtil.Instance().getUserId();
		if (uid > 0) {
			UserDeprecated user = getUserById(uid);
			if (user == null) {
				String userJsonString = PrefUtil.Instance().getUserJsonString();
				if (!StringUtil.isNullOrEmpty(userJsonString)) {
					// 反序列号 user json串
					try {
						user = GsonHelper.GetCommonGson().fromJson(
								userJsonString, UserDeprecated.class);
					} catch (Exception e) {
						e.printStackTrace();
						user = UserDeprecated.combinationUser();
					}
				} else {
					user = UserDeprecated.combinationUser();
				}
				// 请求接口获取当前user
				ZHApis.getUserApi().getUserDetail(null, uid, null);
			}
			return user;
		} else {
			// 本地已没有用户信息，需踢出登录
			HomeUtil.logout();
		}
		return null;
	}

	public UserDeprecatedDao(Class<UserDeprecated> dataClass)
			throws SQLException {
		super(dataClass);
	}

	public UserDeprecatedDao(ConnectionSource connectionSource,
			Class<UserDeprecated> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public UserDeprecatedDao(ConnectionSource connectionSource,
			DatabaseTableConfig<UserDeprecated> tableConfig)
			throws SQLException {
		super(connectionSource, tableConfig);
	}

}
