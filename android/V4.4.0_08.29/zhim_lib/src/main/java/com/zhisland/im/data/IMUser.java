package com.zhisland.im.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.im.BeemApplication;
import com.zhisland.lib.util.StringUtil;

@DatabaseTable(tableName = IMUser.TABLE_NAME, daoClass = IMUserDao.class)
public class IMUser extends IMBase {

    public static final String TABLE_NAME = "imuser";

    public static final String COL_JID = "jid";
    public static final String COL_NAME = "name";
    public static final String COL_DESC = "desc";
    public static final String COL_CHAR = "char";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_TIME = "time";
    public static final String COL_JSON = "userjson";
    public static final String COL_RELATION = "relation";
    public static final String COL_VERSION = "version";


    @DatabaseField(columnName = IMUser.COL_JID, id = true)
    public String jid;// 用户的xmpp jid

    @DatabaseField(columnName = IMUser.COL_NAME)
    public String name;// 用户名称

    @DatabaseField(columnName = IMUser.COL_DESC)
    public String desc;// 好友描述

    @DatabaseField(columnName = IMUser.COL_CHAR)
    public String c;// 用户首字母

    @DatabaseField(columnName = IMUser.COL_AVATAR)
    public String avatar;// 用户头像

    @DatabaseField(columnName = IMUser.COL_TIME)
    public long time;

    @DatabaseField(columnName = IMUser.COL_VERSION)
    public String version;// 好友版本号

    @DatabaseField(columnName = IMUser.COL_JSON)
    public String userJson;// 用户信息的json字符串

    @DatabaseField(columnName = IMUser.COL_RELATION)
    public String relation;

    /**
     * 获取用户ID
     *
     * @return
     */
    public long getUserId() {
        return parseUid(jid);
    }

    /**
     * 根据jid解析uid
     *
     * @param jid
     * @return
     */
    public static long parseUid(String jid) {
        long result = -1;
        try {
            String uid = jid.split("@")[0];
            result = Long.parseLong(uid);
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 根据用户ID构造jid
     *
     * @param uid
     * @return
     */
    public static String buildJid(long uid) {
        return uid + "@"
                + BeemApplication.getInstance().getXmppConfig().getDomain();
    }

    /**
     * 将jid中的resource去掉
     *
     * @param jid
     * @return
     */
    public static String ParseJid(String jid) {
        if (StringUtil.isNullOrEmpty(jid))
            return jid;

        int index = jid.indexOf("/");
        if (index < 0)
            return jid;

        String zhJid = jid.substring(0, index);
        return zhJid;

    }

}
