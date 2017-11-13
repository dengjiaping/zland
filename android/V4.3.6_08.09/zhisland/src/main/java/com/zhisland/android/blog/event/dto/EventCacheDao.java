package com.zhisland.android.blog.event.dto;

import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.StringUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class EventCacheDao extends BaseDaoImpl<EventCache, Long> {

    // ========constructors=========
    private static final String TAG = "dbmgr";

    public Event getEventById(long eventId) {
        Event event = null;
        try {
            EventCache eventCache = this.queryForId(eventId);
            if (eventCache != null) {
                event = GsonHelper.GetCommonGson().fromJson(eventCache.jsonBody, Event.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    /**
     * 活动详情接口返回的数据保存到数据库
     *
     * @param event 活动
     */
    public void cachEventDetail(Event event) {
        if (event == null) {
            return;
        }
        EventCache eventCache = new EventCache();
        eventCache.eventId = event.eventId;
        eventCache.jsonBody = GsonHelper.GetCommonGson().toJson(event);
        try {
            createOrUpdate(eventCache);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public EventCacheDao(Class<EventCache> dataClass) throws SQLException {
        super(dataClass);
    }

    public EventCacheDao(ConnectionSource connectionSource, Class<EventCache> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public EventCacheDao(ConnectionSource connectionSource,
                         DatabaseTableConfig<EventCache> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

}
