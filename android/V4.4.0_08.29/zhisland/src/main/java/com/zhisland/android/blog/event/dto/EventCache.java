package com.zhisland.android.blog.event.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.lib.OrmDto;

/**
 * 活动
 */
@DatabaseTable(tableName = EventCache.TB_NAME, daoClass = EventCacheDao.class)
public class EventCache extends OrmDto {
    private static final long serialVersionUID = 1L;
    public static final String TB_NAME = "event_cache_dto";

    public static final String COL_ID = "event_id";
    public static final String COL_JSON_BODY = "json_body";

    @DatabaseField(columnName = COL_ID, id = true)
    public long eventId;

    @DatabaseField(columnName = COL_JSON_BODY)
    public String jsonBody;
}