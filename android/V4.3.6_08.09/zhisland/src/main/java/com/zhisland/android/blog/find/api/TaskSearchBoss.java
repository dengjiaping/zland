package com.zhisland.android.blog.find.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.find.dto.SearchResult;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 搜索企业家
 * Created by Mr.Tui on 2016/5/20.
 */
public class TaskSearchBoss extends TaskBase<SearchResult<User>, Object> {

    private String maxId;
    private String keyword;
    private String uType;
    private String area;

    public TaskSearchBoss(Object context, String maxId, String keyword, String uType, String area, TaskCallback<SearchResult<User>> responseCallback) {
        super(context, responseCallback);
        this.maxId = maxId;
        this.keyword = keyword;
        this.uType = uType;
        this.area = area;
        this.isPureRest = true;
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "nextId", maxId);
        params = this.put(params, "keyword", keyword);
        params = this.put(params, "uType", uType);
        params = this.put(params, "area", area);
        this.get(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/search/company";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<SearchResult<User>>() {
        }.getType();
    }
}
