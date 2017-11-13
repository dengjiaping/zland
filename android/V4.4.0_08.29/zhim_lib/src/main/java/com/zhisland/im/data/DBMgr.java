package com.zhisland.im.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhisland.im.BeemApplication;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 */
public class DBMgr extends OrmLiteSqliteOpenHelper {

    // public static final String AUTHORITY = "com.zhisland.im";
    private static final String DATABASE_NAME_PREFIX = "zhislandim";
    private static final String DATABASE_NAME_EXT = ".db";

    private static final int VERSION_0310 = 4;
    private static final int VERSION_160931 = 5;//好友关系变为关注管系
    private static final int DATABASE_VERSION = VERSION_160931;
    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    private static final String TAG = "DatabaseHelper";

    private IMMessageDao msgDao;
    private IMChatDao chatDao;
    private IMUserDao userDao;

    private static DBMgr helper = null;

    public static DBMgr getHelper() {
        return getHelper(BeemApplication.getInstance().getContext());
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to
     * this method, there should be 1 and only 1 call to {@link #close()}.
     */
    public static synchronized DBMgr getHelper(Context context) {
        if (helper == null) {
            helper = new DBMgr(context);
        }
        usageCounter.incrementAndGet();
        return helper;
    }

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
            msgDao = null;
            chatDao = null;
            userDao = null;
            helper = null;
        }
    }

    private DBMgr(Context context) {
        super(context, getDataBaseName(), null, DATABASE_VERSION,
                com.zhisland.im.R.raw.ormlite_im);
        getMsgDao().resetMsgLoadStatus();
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase,
                         ConnectionSource connectionSource) {
        to160931(sqliteDatabase);
    }

    private void to160931(SQLiteDatabase sqliteDatabase) {
        to0310();
        from0310To160931(sqliteDatabase);
    }

    private void to0310() {
        try {
            TableUtils.createTable(connectionSource, IMMessage.class);
            TableUtils.createTable(connectionSource, IMChat.class);
//            TableUtils.createTable(connectionSource, IMContact.class);好友关系改为关注关系版本时删除
        } catch (SQLException e) {
            Log.e("db", "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase,
                          ConnectionSource connectionSource, int oldVer, int newVer) {

        switch (oldVer) {
            case VERSION_0310: {
                from0310To160931(sqliteDatabase);
                break;
            }
            case VERSION_160931: {
                //下一次数据库升级时写在这里
                break;
            }
        }
    }

    private void from0310To160931(SQLiteDatabase sqliteDatabase) {
        try {
            sqliteDatabase.execSQL("DROP TABLE imcontact;");
        } catch (Exception e) {
            Log.d("db", e.getMessage(), e);
        }
        try {
            TableUtils.createTable(connectionSource, IMUser.class);
        } catch (SQLException e) {
            Log.e("db", "Unable to create datbases", e);
        }
    }


    public IMMessageDao getMsgDao() {
        if (msgDao == null) {
            try {
                msgDao = getDao(IMMessage.class);
            } catch (SQLException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return msgDao;
    }

    public IMChatDao getChatDao() {
        if (chatDao == null) {
            try {
                chatDao = getDao(IMChat.class);
            } catch (SQLException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return chatDao;
    }

    public IMUserDao getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(IMUser.class);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return userDao;
    }

    private static String getDataBaseName() {
        String name = DATABASE_NAME_PREFIX
                + BeemApplication.getInstance().getXmppAccount().getUserId()
                + DATABASE_NAME_EXT;
        return name;
    }
}
