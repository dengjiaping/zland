package com.zhisland.android.blog.tabhome.model.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.DictResult;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 获取客户端所有字典
 */
public class TaskGetAllDict extends TaskBase<DictResult, Object> {

    public TaskGetAllDict(Context context,
                          TaskCallback<DictResult> responseCallback) {
        super(context, responseCallback);
    }

    @Override
    public void execute() {
        RequestParams params = null;
        String dictVersion = Dict.getInstance().getDictVersion();
        params = this.put(params, "version", dictVersion);
        this.post(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/dict";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<DictResult>() {
        }.getType();
    }

    @Override
    protected DictResult handleStringProxy(HttpResponse response)
            throws Exception {
        handleHeaderInfo(response);

        String responseBody = convertToString(response);
        Type listType = this.getDeserializeType();

        DictResult res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);
        if (res != null) {
            if (res.notEmpty) {
                List<Dict> dicts = res.dicts;
                for (Dict dict : dicts) {
                    DBMgr.getMgr().getDictDao().addDict(dict);
                }
            }
        }

        return res;
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected Throwable handleFailureMessage(Throwable e, String responseBody) {
        return super.handleFailureMessage(e, responseBody);
    }
}
