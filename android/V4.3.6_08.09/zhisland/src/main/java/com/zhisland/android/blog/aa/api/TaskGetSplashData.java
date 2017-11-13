package com.zhisland.android.blog.aa.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.aa.dto.SplashData;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.util.LoadDataFromAssert;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 获取行业列表
 */
public class TaskGetSplashData extends TaskBase<ArrayList<SplashData>, Object> {

    public TaskGetSplashData(Object context,
                             TaskCallback<ArrayList<SplashData>> responseCallback) {
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
        return "/recommend/motto/boot";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ArrayList<SplashData>>() {
        }.getType();
    }

    @Override
    protected ArrayList<SplashData> handleStringProxy(HttpResponse response) throws Exception {
        ArrayList<SplashData> result = super.handleStringProxy(response);
        SplashData.cache(result);
        return result;
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }
}
