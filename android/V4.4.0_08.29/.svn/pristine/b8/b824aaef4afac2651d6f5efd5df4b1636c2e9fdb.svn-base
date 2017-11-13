package com.zhisland.im.data;

import android.util.Log;

import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.lib.util.Pinyin4jUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class IMUserDao extends IMBaseDao<IMUser, Long> {


    //region 用户操作

    /**
     * 获取互粉列表
     *
     * @return
     */
    public List<IMUser> fetchByRelation(String relation, String order, boolean ascend) {
        QueryBuilder<IMUser, Long> qb = queryBuilder();
        try {
            qb.where().eq(IMUser.COL_RELATION, relation);
            qb.orderBy(order, ascend);
            List<IMUser> users = qb.query();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按照首字母分组获取所有的联系人
     *
     * @return
     */
    public ArrayList<UserGroup> fetchContactGroups(String relation, String order, boolean ascend) {
        ArrayList<UserGroup> groups = new ArrayList<>();

        List<IMUser> contacts = fetchByRelation(relation, order, ascend);
        if (contacts != null && contacts.size() > 0) {
            String curChar;
            String lastChar = null;
            UserGroup group = null;
            for (IMUser c : contacts) {
                curChar = (c.c == null ? "" : c.c);
                if (!curChar.equals(lastChar)) {
                    group = new UserGroup();
                    group.letter = curChar;
                    groups.add(group);
                }
                group.addChild(c);
                lastChar = curChar;
            }
        }

        return groups;
    }

    /**
     * 获取好友数量
     *
     * @return
     */
    public long getCountByRelation(String relation) {
        QueryBuilder<IMUser, Long> qb = queryBuilder();
        try {
            qb.where().eq(IMUser.COL_RELATION, relation);
            return qb.countOf();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据用户ID查询通讯录信息
     *
     * @param uid
     * @return
     */
    public IMUser getIMUserByUid(long uid) {
        String jid = IMUser.buildJid(uid);
        return getByJid(jid);
    }

    /**
     * 根据jid查询通讯录信息
     *
     * @param jid
     * @return
     */
    public IMUser getByJid(String jid) {
        QueryBuilder<IMUser, Long> qb = queryBuilder();
        try {
            qb.where().eq(IMUser.COL_JID, jid);
            return qb.queryForFirst();
        } catch (Exception e) {
            Log.d("db", e.getMessage(), e);
            return null;
        }
    }


    /**
     * 批量存储好友列表
     *
     * @param data
     */
    public void saveUsers(final List<IMUser> data) {
        if (data == null || data.size() < 1)
            return;

        try {
            TransactionManager.callInTransaction(getConnectionSource(),
                    new Callable<Void>() {

                        @Override
                        public Void call() throws Exception {
                            QueryBuilder<IMUser, Long> qb = queryBuilder();
                            IMUser ic = null;
                            for (IMUser user : data) {
                                qb.where().eq(IMUser.COL_JID, user.jid);
                                ic = qb.queryForFirst();
                                if (ic == null) {
                                    user.c = Pinyin4jUtil.getStringHeadChar(user.name);
                                    create(user);
                                } else {
                                    ic.name = user.name;
                                    ic.c = Pinyin4jUtil.getStringHeadChar(user.name);
                                    ic.avatar = user.avatar;
                                    ic.relation = user.relation;
                                    ic.version = user.version;
                                    ic.desc = user.desc;
                                    ic.time = user.time;
                                    update(ic);
                                }
                            }
                            notifyAdd(ic);
                            return null;
                        }
                    });
        } catch (Exception e) {
            Log.d("db", e.getMessage(), e);
        }
    }

    /**
     * 判断与用户是否是这个关系
     *
     * @param uid
     * @param relation
     * @return
     */
    public boolean isRelation(long uid, String relation) {
        IMUser user = getIMUserByUid(uid);
        return user != null && user.relation != null && user.relation.equals(relation);
    }

    //endregion


    //region 构造函数
    private static final String TAG = "contact_dao";

    public IMUserDao(ConnectionSource connectionSource,
                     DatabaseTableConfig<IMUser> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    public IMUserDao(ConnectionSource connectionSource,
                     Class<IMUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public IMUserDao(Class<IMUser> dataClass) throws SQLException {
        super(dataClass);
    }


    //endregion

}
