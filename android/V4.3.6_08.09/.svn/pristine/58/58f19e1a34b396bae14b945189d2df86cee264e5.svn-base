package com.zhisland.im.data;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.greenrobot.event.EventBus;

public abstract class IMBaseDao<T extends IMBase, ID> extends
		BaseDaoImpl<T, ID> {

	protected IMBaseDao(Class<T> dataClass) throws SQLException {
		super(dataClass);
	}

	protected IMBaseDao(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	protected IMBaseDao(ConnectionSource connectionSource,
			DatabaseTableConfig<T> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	/**
	 * 通知新增变更
	 * 
	 * @param item
	 */
	protected void notifyAdd(T item) {
		if (item == null)
			return;

		item.action = IMBase.ADD;
		EventBus.getDefault().post(item);
	}

	/**
	 * 通知新增变更
	 * 
	 * @param item
	 */
	protected void notifyUpdate(T item) {
		if (item == null)
			return;

		item.action = IMBase.UPDATE;
		EventBus.getDefault().post(item);
	}

	/**
	 * 通知新增变更
	 * 
	 * @param item
	 */
	protected void notifyDelete(T item) {
		if (item == null)
			return;

		item.action = IMBase.DELETE;
		EventBus.getDefault().post(item);
	}

}
