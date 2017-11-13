package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.PayRequest;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.lang.reflect.Type;

/**
 * 获取支付请求PayReq。
 */
public class TaskGetWXPayReq extends TaskBase<PayRequest, Object> {

    private long eventId;

    public TaskGetWXPayReq(Object context, long eventId,
                           TaskCallback<PayRequest> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.eventId = eventId;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.post(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/event/pay/" + eventId;
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<PayRequest>() {
        }.getType();
    }

}
