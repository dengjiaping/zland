package com.zhisland.android.blog.find.api;

import android.content.Context;

import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.event.api.TaskGetSceneNotifyList;
import com.zhisland.android.blog.event.dto.SceneNotify;
import com.zhisland.android.blog.find.dto.SearchResult;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/5/20.
 */
public class ZHApiFind extends ApiBase {
    private static class Holder {
        private static ZHApiFind INSTANCE = new ZHApiFind();
    }

    public static ZHApiFind Instance() {
        return Holder.INSTANCE;
    }

    private ZHApiFind() {

    }

    /**
     * 获取搜索企业家的个性标签
     */
    public void getBossCustom(Context context, TaskCallback<ArrayList<ZHDicItem>> callback) {
        addTask(context, new TaskGetBossCustom(context, callback));
    }

    /**
     * 获取搜索企业家的热门标签
     */
    public void getBossHot(Context context, TaskCallback<ArrayList<ZHDicItem>> callback) {
        addTask(context, new TaskGetBossHot(context, callback));
    }

    /**
     * 获取搜索资源类别标签
     */
    public void getResCategory(Context context, TaskCallback<ArrayList<ZHDicItem>> callback) {
        addTask(context, new TaskGetResCategory(context, callback));
    }

    /**
     * 搜索企业家
     */
    public void searchBoss(Object context, String maxId, String keyword, String uType, String area, TaskCallback<SearchResult<User>> callback) {
        addTask(context, new TaskSearchBoss(context, maxId, keyword, uType, area,
                callback));
    }

    /**
     * 搜索资源需求
     */
    public void searchRes(Object context, String maxId, String keyword, String uType, String rType, String area, TaskCallback<SearchResult<Resource>> callback) {
        addTask(context, new TaskSearchRes(context, maxId, keyword, uType, rType, area,
                callback));
    }

    /**
     * 获取可能认识的人
     */
    public void getContactFriend(Context context, String nextId, int count, TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetContactFriend(context, nextId, count, callback));
    }

    /**
     * 获取知名岛邻
     */
    public void getFamousDaolin(Context context, TaskCallback<ArrayList<User>> callback) {
        addTask(context, new TaskGetFamousDaolin(context, callback));
    }

    /**
     * 获取最新岛邻
     */
    public void getGetNewDaolin(Context context, TaskCallback<ArrayList<User>> callback) {
        addTask(context, new TaskGetNewDaolin(context, callback));
    }
}
