package com.zhisland.android.blog.common.api;

import android.content.Context;

import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.find.api.TaskGetBossCustom;
import com.zhisland.android.blog.im.api.TaskGetMessageCount;
import com.zhisland.android.blog.im.dto.MessageCount;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Tui on 2016/5/25.
 */
public class ZHApiCommon extends ApiBase {

    private static class Holder {
        private static ZHApiCommon INSTANCE = new ZHApiCommon();
    }

    public static ZHApiCommon Instance() {
        return Holder.INSTANCE;
    }

    private ZHApiCommon() {

    }

    /**
     * 上传通讯录
     */
    public void UploadContact(Context context, String data, TaskCallback<Object> callback) {
        addTask(context, new TaskUploadContact(context, data, callback));
    }

    /**
     * 获取权限
     */
    public void getPermissions(Context context, TaskCallback<List<String>> callback) {
        addTask(context, new TaskGetPermissions(context, callback));
    }

    /**
     * 获取 新增粉丝/互动消息/系统消息 未读数
     */
    public void getMessageCount(Context context, TaskCallback<MessageCount> callback) {
        addTask(context, new TaskGetMessageCount(context, callback));
    }
}
