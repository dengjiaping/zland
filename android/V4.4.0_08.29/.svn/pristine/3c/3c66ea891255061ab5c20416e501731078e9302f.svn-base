package com.zhisland.android.blog.common.dto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.event.dto.EventCache;
import com.zhisland.android.blog.event.dto.EventCacheDao;
import com.zhisland.android.blog.info.model.dto.ZHInfoCache;
import com.zhisland.android.blog.info.model.dto.ZHInfoCacheDao;
import com.zhisland.android.blog.profile.dto.UserDeprecated;
import com.zhisland.android.blog.profile.dto.UserDeprecatedDao;
import com.zhisland.android.blog.tracker.bean.TrackerDto;
import com.zhisland.android.blog.tracker.model.local.impl.TrackerDao;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.MLog;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class DBMgr extends OrmLiteSqliteOpenHelper {

    private UserDao userDao;
    private UserDeprecatedDao userDeprecatedDao;
    private ZHInfoCacheDao infoCacheDao;
    private EventCacheDao eventCacheDao;
    private CacheDao cacheDao;
    private TrackerDao trackerDao;
    private DictDao dictDao;

    public DictDao getDictDao() {
        if (dictDao == null) {
            try {
                dictDao = getDao(Dict.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return dictDao;
    }

    public TrackerDao getTrackerDao() {
        if (trackerDao == null) {
            try {
                trackerDao = getDao(TrackerDto.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return trackerDao;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return userDao;
    }

    public UserDeprecatedDao getUserDeprecatedDao() {
        if (userDeprecatedDao == null) {
            try {
                userDeprecatedDao = getDao(UserDeprecated.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return userDeprecatedDao;
    }

    public ZHInfoCacheDao getInfoCacheDao() {
        if (infoCacheDao == null) {
            try {
                infoCacheDao = getDao(ZHInfoCache.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return infoCacheDao;
    }

    public EventCacheDao getEventCacheDao() {
        if (eventCacheDao == null) {
            try {
                eventCacheDao = getDao(EventCache.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return eventCacheDao;
    }

    public CacheDao getCacheDao() {
        if (cacheDao == null) {
            try {
                cacheDao = getDao(CacheDto.class);
            } catch (SQLException e) {
                MLog.e(TAG, e.getMessage(), e);
            }
        }
        return cacheDao;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        createTable(arg1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int before,
                          int current) {
        // 做数据库升级操作
        DBUpdateUtil.getInstance().update(db, arg1);
        updateData(before);
    }

    public void createTable(ConnectionSource cs) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource,
                    UserDeprecated.class);
            TableUtils.createTableIfNotExists(connectionSource, EventCache.class);
            TableUtils.createTableIfNotExists(connectionSource, ZHInfoCache.class);
            TableUtils.createTableIfNotExists(connectionSource, CacheDto.class);
            TableUtils.createTableIfNotExists(connectionSource, TrackerDto.class);
            TableUtils.createTableIfNotExists(connectionSource, Dict.class);
        } catch (SQLException e) {
            MLog.e(TAG, "create table", e);
        }
    }

    /**
     * 数据库更新后的后续操作，如数据转移。
     */
    private void updateData(final int before) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (before < DB_VERSION_66) {
                    updateToV66();
                }
            }
        }).start();
    }

    private void updateToV66() {
        // 将登陆用户的信息转到新表
        UserDeprecated user = getUserDeprecatedDao()
                .getUserFirstVersion(PrefUtil.Instance().getUserId());
        if (user != null) {
            String jsonStr = GsonHelper.GetCommonGson().toJson(user);
            getUserDao().createOrUpdateJson(user.uid, jsonStr);
        }
        // 对于之前的版本User 数据不全，设置flag刷新self User。
        PrefUtil.Instance().setNeedRefrshSelf(true);


        // 将UserDeprecated表中和联系人相关的数据保存到新表中。
        // 好友关系变成了关注管系，这个用户信息不存储也可以
//        try {
//            List<IMContact> all = com.zhisland.im.data.DBMgr.getHelper()
//                    .getContactDao().queryForAll();
//            for (IMContact contact : all) {
//                long uid = IMContact.parseUid(contact.jid);
//                user = getUserDeprecatedDao().getUserFirstVersion(uid);
//                if (user != null) {
//                    String jsonStr = GsonHelper.GetCommonGson().toJson(user);
//                    getUserDao().createOrUpdateJson(user.uid, jsonStr);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    // =========================

    private static final String TAG = "dbmgr";
    private static final AtomicInteger usageCounter = new AtomicInteger(0);
    private static DBMgr helper = null;

    /**
     * db版本66，对user进行重构
     */
    private static final int DB_VERSION_66 = 66;
    /**
     * db版本 event改为EventCache，用json保存。
     */
    private static final int DB_VERSION_67 = 67;
    /**
     * 增加客户端统计事件保存
     */
    private static final int DB_VERSION_TRACKER = 68;
    /**
     * db版本 info改为ZHInfoCache，用json保存。
     */
    private static final int DB_VERSION_INFO = 69;
    /**
     * 增加字典
     */
    private static final int DB_VERSION_DICT = 70;

    // 当前版本的DB version
    private static final int DB_VERSION = DB_VERSION_DICT;

    public static DBMgr getMgr() {
        return getHelper(ZhislandApplication.APP_CONTEXT);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to
     * this method, there should be 1 and only 1 call to {@link #close()}.
     */
    private static synchronized DBMgr getHelper(Context context) {
        if (helper == null) {
            helper = new DBMgr(context);
        }

        usageCounter.incrementAndGet();
        return helper;
    }

    /**
     * for data base helper release.
     */
    public static synchronized void release() {
        if (helper != null) {
            usageCounter.set(1);
            helper.close();
            helper = null;
        }
    }

    /**
     * Close the database connections and clear any cached DAOs. For each call
     * to {@link #getHelper(Context)}, there should be 1 and only 1 call to this
     * method. If there were 3 calls to {@link #getHelper(Context)} then on the
     * 3rd call to this method, the helper and the underlying database
     * connections will be closed.
     */
    @Override
    public void close() {
        if (usageCounter.decrementAndGet() == 0) {
            super.close();
            helper = null;
        }
    }

    private DBMgr(Context context) {
        super(context, buildDBName(), null, DB_VERSION, R.raw.ormlite_config);
    }

    public static String buildDBName() {
        String dbName = String.format("zhisland.db");
        MLog.d(TAG, dbName);
        return dbName;
    }
}
