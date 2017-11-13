package com.zhisland.android.blog.tracker.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.android.blog.tracker.model.local.impl.TrackerDao;
import com.zhisland.lib.async.http.task.GsonHelper;

/**
 * Created by 向飞 on 2016/6/12.
 */
@DatabaseTable(tableName = TrackerDto.TB_NAME, daoClass = TrackerDao.class)
public class TrackerDto {

    public static final String TB_NAME = "tb_tracker";

    public TrackerDto() {
    }

    public TrackerDto(TrackerEvent event) {
        this.sessionId = event.sessionId;
        this.ctime = event.time;
        this.jsonBody = GsonHelper.GetCommonGson().toJson(event);
    }


    public static final String COL_SESSION = "session_id";
    public static final String COL_CTIME = "ctime";
    public static final String COL_BODY = "json_body";

    @DatabaseField(columnName = COL_CTIME, id = true)
    public long ctime;

    @DatabaseField(columnName = COL_SESSION)
    public String sessionId;


    @DatabaseField(columnName = COL_BODY)
    public String jsonBody;
}
