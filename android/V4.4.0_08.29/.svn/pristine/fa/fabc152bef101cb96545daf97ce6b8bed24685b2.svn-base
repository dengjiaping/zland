package com.zhisland.android.blog.find.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取搜索企业家的热门标签。
 * Created by Mr.Tui on 2016/5/20.
 */
public class TaskGetBossHot extends TaskBase<ArrayList<ZHDicItem>, Object> {

    private static final String KEY_CACHE_BOSS_HOT = "key_cache_boss_hot";

    public TaskGetBossHot(Object context, TaskCallback<ArrayList<ZHDicItem>> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.get(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/recommend/tag/corpHot";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ArrayList<ZHDicItem>>() {
        }.getType();
    }

    @Override
    protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        return super.handleFailureMessage(e, responseBody);
    }

    @Override
    protected ArrayList<ZHDicItem> handleStringProxy(HttpResponse response)
            throws Exception {
        ArrayList<ZHDicItem> result = super.handleStringProxy(response);
        if (result != null && result.size() > 0) {
            DBMgr.getMgr().getCacheDao().set(KEY_CACHE_BOSS_HOT, result);
        }
        return result;
    }

    public static ArrayList<ZHDicItem> getCacheData() {
        Object obj = DBMgr.getMgr().getCacheDao().get(KEY_CACHE_BOSS_HOT);
        if (obj != null) {
            return (ArrayList<ZHDicItem>) obj;
        }
        return null;
    }
}
