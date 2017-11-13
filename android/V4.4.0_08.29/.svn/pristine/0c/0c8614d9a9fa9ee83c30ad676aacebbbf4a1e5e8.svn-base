package com.zhisland.android.blog.common.util;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.zhisland.android.blog.aa.dto.PhoneContact;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhoneContactUtil {

    private static boolean isSyncVCardIng = false;

    public static final int DEFAULT_SEND_SIZE = 100;

    /**
     * 上传通讯录的时间戳。时间戳为上传的通讯录中contact_last_updated_timestamp字段最大的值。
     */
    private static final String KEY_LAST_UPDATED_TIME = "contact_last_updated_timestamp";

    /**
     * 要查询的列
     */
    private static final String[] CONTACT_COL = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP
    };

    /**
     * 获取最后一次上传的时间戳
     */
    public static synchronized Long getLastTimestamp() {
        return PrefUtil.Instance().getByKey(KEY_LAST_UPDATED_TIME + PrefUtil.Instance().getUserId(), new Long(0));
    }

    /**
     * 设置最后一次上传的时间戳
     */
    public static synchronized void setLastTimestamp(Long timestamp) {
        if (timestamp != null) {
            PrefUtil.Instance().setKeyValue(KEY_LAST_UPDATED_TIME + PrefUtil.Instance().getUserId(), timestamp);
        }
    }

    /**
     * 上传VCard， 24小时之内上传一次
     */
    public static void syncVCard(final int sendSize) {
        long uploadTime = PrefUtil.Instance().getVCardUploadTime();
        if (TimeUtil.isBeyond24H(uploadTime)) {
            if (isSyncVCardIng()) {
                return;
            }
            PrefUtil.Instance().setVcardUploadTime(System.currentTimeMillis());
            //设置正在上传tag为真，在上传完成后需要重置为false；
            setIsSyncVCardIng(true);
            ThreadManager.instance().execute(
                    new PriorityFutureTask<>(new Runnable() {

                        @Override
                        public void run() {
                            List<ContactResult<String>> allVCard = getAllVCard(sendSize);
                            uploadVCard(allVCard);
                        }
                    }, null, ThreadManager.THREAD_PRIORITY_LOWER), null);
        }
    }

    /**
     * 查出新增联系人
     */
    public static ContactResult<String> getChangeContact() {
        ContactResult<String> result = new ContactResult<String>();
        Cursor cursor = null;
        try {
            ContentResolver cr = ZHApplication.APP_CONTEXT.getContentResolver();
            Long timestamp = getLastTimestamp();
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP + " > " + timestamp;
            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    CONTACT_COL, selection, null, ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
            if (cursor != null && cursor.moveToFirst()) {
                final int contactIdIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int displayNameIndex = cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int phoneIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int timestampIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
                HashMap<String, PhoneContact> map = new HashMap();
                do {
                    String contactIdString = cursor.getString(contactIdIndex);
                    String name = cursor.getString(displayNameIndex);
                    String phone = cursor.getString(phoneIndex);
                    timestamp = cursor.getLong(timestampIndex);
                    if (map.containsKey(contactIdString)) {
                        map.get(contactIdString).phones.add(phone);
                    } else {
                        PhoneContact pc = new PhoneContact();
                        pc.name = name;
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(phone);
                        pc.phones = list;
                        map.put(contactIdString, pc);
                    }
                } while (cursor.moveToNext());
                Gson gson = new Gson();
                result.timestamp = timestamp;
                result.result = gson.toJson(map.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    /**
     * 获取所有联系人（用于主动邀请注册）
     */
    public static ArrayList<InviteUser> getAllContact() {
        Cursor cursor = null;
        ContentResolver cr = ZHApplication.APP_CONTEXT.getContentResolver();
        ArrayList<InviteUser> list = new ArrayList<InviteUser>();
        try {
            cursor = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    CONTACT_COL, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);

            if (cursor != null && cursor.moveToFirst()) {
                final int contactIdIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int displayNameIndex = cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int phoneIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                do {
                    InviteUser pc = new InviteUser();
                    pc.user = new User();
                    pc.user.name = cursor.getString(displayNameIndex);
                    pc.user.userMobile = handlePhone(cursor.getString(phoneIndex));
                    if (!StringUtil.isMobileNumber(pc.user.userMobile)) {
                        //对座机号进行过滤
                        continue;
                    }
                    pc.state = new CustomState();
                    pc.state.setIsOperable(CustomState.CAN_OPERABLE);
                    pc.state.setStateName("邀请");
                    list.add(pc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    private static String handlePhone(String str) {
        if (StringUtil.isNullOrEmpty(str) || "null".equals(str.trim().toLowerCase())) {
            return "";
        }
        str = str.trim().replaceAll("[_;+*＋＊－()\\-\\s ]", "");

        // 特殊处理：中国
        if ((str.length() == 11 + 5) && str.startsWith("17951")) {
            str = str.substring(5);
        } else if ((str.length() == 11 + 2) && str.startsWith("86")) {
            str = str.substring(2);
        } else if ((str.length() == 11 + 4) && str.startsWith("0086")) {
            str = str.substring(4);
        }
        return str;
    }

    private static synchronized boolean isSyncVCardIng() {
        return isSyncVCardIng;
    }

    private static synchronized void setIsSyncVCardIng(boolean isSyncVCardIng) {
        PhoneContactUtil.isSyncVCardIng = isSyncVCardIng;
    }

    /**
     * 获取所有Vcard
     */
    private static List<ContactResult<String>> getAllVCard(int sendSize) {
        Cursor cursor = null;
        List<ContactResult<String>> result = null;
        try {
            Long timestamp = getLastTimestamp();
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP + " > " + timestamp;
            cursor = ZHApplication.APP_CONTEXT.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP},
                    selection, null, ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
            if (cursor != null && cursor.moveToFirst()) {
                result = new ArrayList<ContactResult<String>>();
                StringBuilder dataSb = new StringBuilder();
                int count = 0;
                int lookUpIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                int timestampIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
                do {
                    String lookupKey = cursor.getString(lookUpIndex);
                    timestamp = cursor.getLong(timestampIndex);
                    String vCardStr = getVcardStr(lookupKey);
                    if (!StringUtil.isNullOrEmpty(vCardStr)) {
                        dataSb.append(vCardStr);
                        count++;
                    }
                    if (count >= sendSize) {
                        ContactResult<String> contactResult = new ContactResult<String>();
                        contactResult.timestamp = timestamp;
                        contactResult.result = dataSb.toString();
                        result.add(contactResult);
                        dataSb = new StringBuilder();
                        count = 0;
                    }
                } while (cursor.moveToNext());

                //处理sendSize的零头余数
                if (dataSb != null && dataSb.length() > 0) {
                    ContactResult<String> contactResult = new ContactResult<String>();
                    contactResult.timestamp = timestamp;
                    contactResult.result = dataSb.toString();
                    result.add(contactResult);
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 上传vcard task
     *
     * @param vCard
     */
    private static void uploadVCard(List<ContactResult<String>> vCard) {
        if (vCard == null || vCard.size() == 0) {
            return;
        }
        ZHApis.getUserApi().syncVcard(null, vCard, new TaskCallback<Object>() {

            @Override
            public void onFinish() {
                setIsSyncVCardIng(false);
            }

            @Override
            public void onSuccess(Object content) {

            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    /**
     * 根据lookupKey获取该key的Vcard。
     */
    private static String getVcardStr(String lookupKey) {
        String result = null;
        Uri uri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd = null;
        FileInputStream fis = null;
        try {
            fd = ZHApplication.APP_CONTEXT.getContentResolver().openAssetFileDescriptor(uri, "r");
            fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            result = new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fd.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 通讯录结果结构
     */
    public static class ContactResult<T> {

        /**
         * 结果数据对应的时间戳。联系人表中contact_last_updated_timestamp字段
         */
        public Long timestamp;

        /**
         * 结果数据
         */
        public T result;

    }

    /**
     * 查询联系人数据库，检查是否能查到数据，如果没查到返回false。目前暂用没查到数据的情况代表，app没有通讯论访问权限。
     */
    public static boolean hasContactData() {
        Cursor cursor = null;
        try {
            cursor = ZHApplication.APP_CONTEXT.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    public static ArrayList<String> getAllPhoneNum() {
        Cursor cursor = null;
        ContentResolver cr = ZHApplication.APP_CONTEXT.getContentResolver();
        ArrayList<String> list = new ArrayList<String>();
        try {
            cursor = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);

            if (cursor != null && cursor.moveToFirst()) {
                final int phoneIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                do {
                    list.add(handlePhone(cursor.getString(phoneIndex)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }
}
