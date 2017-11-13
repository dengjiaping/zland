package com.zhisland.android.blog.common.dto;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.lib.util.MLog;

import java.sql.SQLException;

/**
 * 字典 dao
 */
public class DictDao extends BaseDaoImpl<Dict, Long> {

    /**
     * 添加字典
     */
    public void addDict(Dict dict) {
        if (dict == null || dict.type == null) {
            return;
        }
        try {
            this.createOrUpdate(dict);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 获取字典
     */
    public String getDictData(int dictType) {
        try {
            Dict dict = queryForId((long) dictType);
            if (dict != null) {
                return dict.data;
            }
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage(), e);
        }
        return null;

    }

    /**
     * 获取字典版本号
     */
    public long getDictVersion(int dictType) {
        try {
            Dict dict = queryForId((long) dictType);
            if (dict != null) {
                return dict.version;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========constructors=========
    private static final String TAG = "dbmgr";

    public DictDao(Class<Dict> dataClass) throws SQLException {
        super(dataClass);
    }

    public DictDao(ConnectionSource connectionSource, Class<Dict> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public DictDao(ConnectionSource connectionSource,
                   DatabaseTableConfig<Dict> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

}
