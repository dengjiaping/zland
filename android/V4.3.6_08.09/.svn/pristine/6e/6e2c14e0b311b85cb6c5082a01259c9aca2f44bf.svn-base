package com.zhisland.android.blog.info.model.dto;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.async.http.task.GsonHelper;

import java.sql.SQLException;

public class ZHInfoCacheDao extends BaseDaoImpl<ZHInfoCache, Long> {

    // ========constructors=========
    private static final String TAG = "dbmgr";

    public ZHInfo getInfoById(long newsId) {
        ZHInfo info = null;
        try {
            ZHInfoCache infoCache = this.queryForId(newsId);
            if (infoCache != null) {
                info = GsonHelper.GetCommonGson().fromJson(infoCache.jsonBody, ZHInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public boolean hasInfo(long newsId) {
        try {
            ZHInfoCache infoCache = this.queryForId(newsId);
            if (infoCache != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 资讯详情接口返回的数据保存到数据库
     *
     * @param info 资讯
     */
    public void cachInfoDetail(ZHInfo info) {
        if (info == null) {
            return;
        }
        ZHInfoCache infoCache = new ZHInfoCache();
        infoCache.newsId = info.newsId;
        infoCache.jsonBody = GsonHelper.GetCommonGson().toJson(info);
        try {
            createOrUpdate(infoCache);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public ZHInfoCacheDao(Class<ZHInfoCache> dataClass) throws SQLException {
        super(dataClass);
    }

    public ZHInfoCacheDao(ConnectionSource connectionSource, Class<ZHInfoCache> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public ZHInfoCacheDao(ConnectionSource connectionSource,
                          DatabaseTableConfig<ZHInfoCache> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

}
