package com.zhisland.android.blog.find.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.find.dto.SearchResult;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 搜索资源需求
 * Created by Mr.Tui on 2016/5/20.
 */
public class TaskSearchRes extends TaskBase<SearchResult<Resource>, Object> {

    private String maxId;
    private String keyword;
    private String rType;
    private String uType;
    private String area;

    public TaskSearchRes(Object context, String maxId, String keyword, String uType, String rType, String area, TaskCallback<SearchResult<Resource>> responseCallback) {
        super(context, responseCallback);
        this.maxId = maxId;
        this.keyword = keyword;
        this.rType = rType;
        this.uType = uType;
        this.area = area;
        this.isPureRest = true;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "nextId", maxId);
        params = this.put(params, "keyword", keyword);
        params = this.put(params, "rType", rType);
        params = this.put(params, "uType", uType);
        params = this.put(params, "area", area);
        this.get(params, null);
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected String getPartureUrl() {
        return "/search/resource";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<SearchResult<Resource>>() {
        }.getType();
    }
}
