package com.zhisland.android.blog.info.model.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.lib.OrmDto;

/**
 * 资讯cache
 */
@DatabaseTable(tableName = ZHInfoCache.TB_NAME, daoClass = ZHInfoCacheDao.class)
public class ZHInfoCache extends OrmDto {
    private static final long serialVersionUID = 1L;
    public static final String TB_NAME = "info_cache_dto";

    public static final String COL_ID = "news_id";
    public static final String COL_JSON_BODY = "json_body";

    @DatabaseField(columnName = COL_ID, id = true)
    public long newsId;

    @DatabaseField(columnName = COL_JSON_BODY)
    public String jsonBody;
}
