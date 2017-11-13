package com.zhisland.android.blog.contacts.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.contacts.dto.IMPageData;
import com.zhisland.android.blog.contacts.push.InviteSuccessHandler;
import com.zhisland.android.blog.im.view.adapter.IMUIUtils;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.Pinyin4jUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 批量获取好友列表
 */
public class TaskGetFriendListByUid extends TaskBase<Object, Object> {

    private static final String TAG = "syncFriend";
    public static final String VERSION_KEY = "version";

    public TaskGetFriendListByUid(Context context, TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        String version = PrefUtil.Instance().getByKey(VERSION_KEY, null);
        if (version == null)
            version = "0";

        MLog.d(TAG, "start sync " + context.toString());
        sendSyncRequest(version);
    }

    @Override
    protected String getUrl() {
        return Config.getHostUrl() + "relation";
    }

    ;

    @Override
    protected Object handleStringProxy(HttpResponse response) throws Exception {
        MLog.d(TAG, "receive sync response: ");
        String responseBody = convertToString(response);
        MLog.d(TAG, responseBody);
        Type listType = this.getDeserializeType();
        IMPageData<User> res = null;
        res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);
        PrefUtil.Instance().setKeyValue(VERSION_KEY, res.version);

        if (res == null || res.data == null) {
            MLog.d(TAG, "同步数据为空");
            return null;
        }

        // store user to DB
        List<IMContact> contacts = new ArrayList<IMContact>();
        long time = System.currentTimeMillis();
        for (User user : res.data) {
            if (StringUtil.isNullOrEmpty(user.name)) {
                continue;
            }
            com.zhisland.android.blog.common.dto.DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
            IMContact ic = new IMContact();
            ic.jid = IMContact.buildJid(user.uid);
            ic.name = user.name;
            ic.c = Pinyin4jUtil.getStringHeadChar(user.name.trim());
            ic.relation = user.relationLevel;
            ic.avatar = user.userAvatar;
            ic.version = user.version;
            ic.desc = user.introduce;
            ic.isFriend = user.isFriend;
            ic.time = time;
            time++;
            contacts.add(ic);

            MLog.d(TAG, String.format("同步好友%s==%d==%d", ic.name, ic.isFriend,
                    ic.relation));
        }
        MLog.d(TAG, "同步好友数：" + contacts.size());
        DBMgr.getHelper().getContactDao().saveFriendContacts(contacts);
        MLog.d(TAG, "当前批次同步结束");
        if (res.syncStatus < 1) {
            sendSyncRequest(res.version);
        } else {
            MLog.d(TAG, "同步全部结束");

            insertInviteSuccessMessage();
        }

        return null;
    }

    /**
     *  邀请成功后自动成为好友，在IM TAB 中加入默认消息
     */
    private void insertInviteSuccessMessage() {
        String value = PrefUtil.Instance().getByKey(InviteSuccessHandler.INVITE_USER_ID, "");
        if (!StringUtil.isNullOrEmpty(value)) {
            String[] uids = value.split(",");
            for (int i = 0; i < uids.length; i++) {
                String uid = uids[i];
                try {
                    long fromUid = Long.parseLong(uid);
                    String jid = IMContact.buildJid(fromUid);
                    IMContact contact = DBMgr.getHelper().getContactDao().getByJid(jid);
                    DBMgr.getHelper().getContactDao().insertInitChatAndMsg(jid, contact.name);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            PrefUtil.Instance().setKeyValue(InviteSuccessHandler.INVITE_USER_ID, "");
            Log.e("aaa", "insert success...");
        }
    }

    @Override
    protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        MLog.d(TAG, "同步好友失败：" + responseBody);
        return super.handleFailureMessage(e, responseBody);
    }

    private void sendSyncRequest(String version) {
        RequestParams params = null;
        params = this.put(params, "version", version);
        this.get(params, null);
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<IMPageData<User>>() {
        }.getType();
    }

}
